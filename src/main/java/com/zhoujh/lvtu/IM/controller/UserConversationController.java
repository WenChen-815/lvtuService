package com.zhoujh.lvtu.IM.controller;

import com.zhoujh.lvtu.IM.modle.UserConversation;
import com.zhoujh.lvtu.IM.serviceImpl.UserConversationServiceImpl;
import com.zhoujh.lvtu.utils.HuanXinUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/conversation")
public class UserConversationController {
    @Autowired
    private UserConversationServiceImpl userConversationServiceImpl;

    @GetMapping("/getByUserId")
    public List<UserConversation> getByUserId(@RequestParam String userId) {
        return userConversationServiceImpl.getByUserId(userId);
    }

    @PostMapping("/createChat")
    public List<UserConversation> createChat(@RequestBody UserConversation userConversation) {
       List<UserConversation> userConversationList = new ArrayList<>();
       UserConversation isExist = userConversationServiceImpl.getByConversationIdAndUserId(userConversation.getConversationId(),userConversation.getUserId());
       if (isExist == null){
           userConversationServiceImpl.save(userConversation);
           userConversationList.add(userConversation);
       } else {
           userConversationList.add(isExist);
       }
       for (String member : userConversation.getMembers()){
           isExist = userConversationServiceImpl.getByConversationIdAndUserId(HuanXinUtils.createHXId(userConversation.getUserId()),member);
           if (isExist == null){
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
        for (int i = 0; i < userConversation.getMembers().size(); i++){
            String member = userConversation.getMembers().get(i);
            UserConversation isExist = userConversationServiceImpl.getByConversationIdAndUserId(userConversation.getConversationId(),member);
            if (isExist == null){
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
}
