package com.zhoujh.lvtu.IM.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhoujh.lvtu.IM.modle.UserConversation;

import java.util.List;

public interface UserConversationService extends IService<UserConversation> {
    UserConversation getByConversationIdAndUserId(String conversationId, String userId);
    List<UserConversation> getByUserId(String userId);
}
