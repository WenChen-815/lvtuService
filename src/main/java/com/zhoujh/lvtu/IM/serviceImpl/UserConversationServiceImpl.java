package com.zhoujh.lvtu.IM.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhoujh.lvtu.IM.mapper.UserConversationMapper;
import com.zhoujh.lvtu.IM.modle.UserConversation;
import com.zhoujh.lvtu.IM.service.UserConversationService;
import com.zhoujh.lvtu.common.model.User;
import com.zhoujh.lvtu.common.model.UserInfo;
import com.zhoujh.lvtu.common.model.UserRelationship;
import com.zhoujh.lvtu.common.serviceImpl.UserRelationshipServiceImpl;
import com.zhoujh.lvtu.common.serviceImpl.UserServiceImpl;
import com.zhoujh.lvtu.utils.HuanXinUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserConversationServiceImpl extends ServiceImpl<UserConversationMapper, UserConversation> implements UserConversationService {
    @Autowired
    private UserConversationMapper userConversationMapper;
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private UserRelationshipServiceImpl userRelationshipServiceImpl;
    @Override
    public UserConversation getByConversationIdAndUserId(String conversationId, String userId) {
        UserConversation conversation = userConversationMapper.getByConversationIdAndUserId(conversationId,userId);
        if (conversation != null){
            conversation.setUserInfoList(setUserInfoList(conversation,userId));
        }

        return conversation;
    }
    private List<UserInfo> setUserInfoList(UserConversation conversation, String userId){
        List<UserInfo> userInfoList = new ArrayList<>();
        if(conversation.getConversationType().equals("GroupChat")){
            for(String s : conversation.getMembers()){
                User user = userServiceImpl.getUserById(s);
                UserRelationship userRelationship = userRelationshipServiceImpl.findRelationship(userId, s);
                UserInfo userInfo = new UserInfo(
                        user.getUserId(),
                        user.getUserName(),
                        user.getStatus(),
                        user.getGender(),
                        user.getAge(),
                        user.getBirth(),
                        user.getAvatarUrl(),
                        userRelationship==null?0:userRelationship.getRelationshipType()
                );
                userInfoList.add(userInfo);
            }
        } else if(conversation.getConversationType().equals("Chat")){
            String memberId = HuanXinUtils.restoreUUID(conversation.getConversationId());
            User user = userServiceImpl.getUserById(memberId);
            System.out.println(user);
            UserRelationship userRelationship = userRelationshipServiceImpl.findRelationship(userId, memberId);
            UserInfo userInfo = new UserInfo(
                    user.getUserId(),
                    user.getUserName(),
                    user.getStatus(),
                    user.getGender(),
                    user.getAge(),
                    user.getBirth(),
                    user.getAvatarUrl(),
                    userRelationship==null?0:userRelationship.getRelationshipType()
            );
            userInfoList.add(userInfo);
        }
        return userInfoList;
    }

    @Override
    public List<UserConversation> getByUserId(String userId) {
        List<UserConversation> userConversationList = userConversationMapper.getByUserId(userId);
        if (userConversationList != null){
            for(UserConversation userConversation : userConversationList){
                userConversation.setUserInfoList(setUserInfoList(userConversation,userId));
            }
        }
        return userConversationList;
    }

    @Override
    public UserConversation addOne(UserConversation userConversation) {
        userConversationMapper.insert(userConversation);
        List<UserConversation> userConversationList = userConversationMapper.getByConversationId(userConversation.getConversationId());
        for (UserConversation userConversation1 : userConversationList){
            userConversation1.getMembers().add(userConversation.getUserId());
            userConversationMapper.updateById(userConversation1);
        }
        return userConversationMapper.getByConversationIdAndUserId(userConversation.getConversationId(),userConversation.getUserId());
    }

    @Override
    public void removeOne(UserConversation userConversation) {
        userConversationMapper.deleteById(userConversation);
        List<UserConversation> userConversationList = userConversationMapper.getByConversationId(userConversation.getConversationId());
        for (UserConversation userConversation1 : userConversationList){
            userConversation1.getMembers().remove(userConversation.getUserId());
            userConversationMapper.updateById(userConversation1);
        }
    }

    @Override
    public int deleteByConversationId(String conversationId) {
        return userConversationMapper.deleteByConversationId(conversationId);
    }

    @Override
    public List<UserConversation> getByConversationId(String groupId) {
        return this.lambdaQuery()
                .eq(UserConversation::getConversationId, groupId)
                .list();
    }
}
