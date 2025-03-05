package com.zhoujh.lvtu.IM.controller;

import com.zhoujh.lvtu.IM.modle.UserConversation;
import com.zhoujh.lvtu.IM.serviceImpl.UserConversationServiceImpl;
import com.zhoujh.lvtu.common.model.User;
import com.zhoujh.lvtu.common.model.UserInfo;
import com.zhoujh.lvtu.common.serviceImpl.HmsPushTokenServiceImpl;
import com.zhoujh.lvtu.common.serviceImpl.UserServiceImpl;
import com.zhoujh.lvtu.utils.HuanXinUtils;
import com.zhoujh.lvtu.utils.HuaweiPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/conversation")
public class UserConversationController {
    @Autowired
    private UserConversationServiceImpl userConversationServiceImpl;
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private HmsPushTokenServiceImpl hmsPushTokenServiceImpl;
    @Autowired
    private HuaweiPushService huaweiPushService;

    @GetMapping("/getByUserId")
    public List<UserConversation> getByUserId(@RequestParam String userId) {
        return userConversationServiceImpl.getByUserId(userId);
    }

    @PostMapping("/createChat")
    public List<UserConversation> createChat(@RequestBody UserConversation userConversation) {
        List<UserConversation> userConversationList = new ArrayList<>();
        UserConversation isExist = userConversationServiceImpl.getByConversationIdAndUserId(userConversation.getConversationId(), userConversation.getUserId());
        if (isExist == null) {
            userConversationServiceImpl.save(userConversation);
            userConversationList.add(userConversation);
        } else {
            userConversationList.add(isExist);
        }
        for (String member : userConversation.getMembers()) {
            isExist = userConversationServiceImpl.getByConversationIdAndUserId(HuanXinUtils.createHXId(userConversation.getUserId()), member);
            if (isExist == null) {
                UserConversation conversation = new UserConversation();
                conversation.setUserId(member);
                conversation.setConversationId(HuanXinUtils.createHXId(userConversation.getUserId()));
                conversation.setConversationType(userConversation.getConversationType());
                List<String> members = new ArrayList<>();
                members.add(userConversation.getUserId());
                conversation.setMembers(members);
                userConversationServiceImpl.save(conversation);
                userConversationList.add(conversation);
            } else {
                userConversationList.add(isExist);
            }
        }
        return userConversationList;
    }

    @PostMapping("/createGroupChat")
    public List<UserConversation> createGroupChat(@RequestBody UserConversation userConversation) {
        List<UserConversation> userConversationList = new ArrayList<>();
        for (int i = 0; i < userConversation.getMembers().size(); i++) {
            String member = userConversation.getMembers().get(i);
            UserConversation isExist = userConversationServiceImpl.getByConversationIdAndUserId(userConversation.getConversationId(), member);
            if (isExist == null) {
                UserConversation conversation = new UserConversation();
                conversation.setUserId(member);
                conversation.setGroupName(userConversation.getGroupName());
                conversation.setConversationId(userConversation.getConversationId());
                conversation.setConversationType(userConversation.getConversationType());
                List<String> members = new ArrayList<>(userConversation.getMembers());
                members.remove(member);
                conversation.setMembers(members);
                userConversationServiceImpl.save(conversation);
                userConversationList.add(conversation);
            } else {
                userConversationList.add(isExist);
            }
        }
        return userConversationList;
    }

    @GetMapping("/inviteUser")
    public UserInfo inviteUser(@RequestParam String userName,
                               @RequestParam String groupId,
                               @RequestParam String inviteUserId) {
        List<String> newMembers = new ArrayList<>();
        List<UserConversation> userConversationList = userConversationServiceImpl.getByConversationId(groupId);
        String groupName = userConversationList.get(0).getGroupName();
        for (UserConversation userConversation : userConversationList) {
            List<String> members = userConversation.getMembers();
            members.add(inviteUserId);
            userConversation.setMembers(members);
            userConversationServiceImpl.updateById(userConversation);
            newMembers.add(userConversation.getUserId());
        }
        UserConversation userConversation = new UserConversation();
        userConversation.setUserId(inviteUserId);
        userConversation.setConversationId(groupId);
        userConversation.setConversationType("GroupChat");
        userConversation.setMembers(newMembers);
        userConversation.setGroupName(groupName);
        userConversationServiceImpl.save(userConversation);

        // 通知被邀请者
        String tokens = hmsPushTokenServiceImpl.createTokens(inviteUserId);
        String json = "{\n" +
                "    \"validate_only\": false,\n" +
                "    \"message\": {\n" +
                "        \"data\": \"{'title':'旅兔通知'," +
                "'body':'"+userName+"已将你拉入群聊~'," +
                "'messageTag':'joinGroup'," +
                "'tagUserId':'" + inviteUserId + "'}\",\n" +
                "        \"token\": [" + tokens + "]\n" +
                "    }\n" +
                "}";
        try {
            huaweiPushService.sendPushMessage(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        User user = userServiceImpl.getUserById(inviteUserId);
        return new UserInfo(
                user.getUserId(),
                user.getUserName(),
                user.getStatus(),
                user.getGender(),
                user.getAge(),
                user.getBirth(),
                user.getAvatarUrl(),
                2
        );
    }
}
