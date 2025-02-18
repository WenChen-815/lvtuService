package com.zhoujh.lvtu.IM.modle;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhoujh.lvtu.common.model.UserInfo;
import com.zhoujh.lvtu.utils.StringListHandler;
import lombok.Data;

import java.util.List;

@Data
@TableName("user_conversation")
public class UserConversation {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String userId;
    private String conversationId;
    private String conversationType;
    @TableField(typeHandler = StringListHandler.class)
    private List<String> members;
    @TableField(exist = false)
    private List<UserInfo> userInfoList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getConversationType() {
        return conversationType;
    }

    public void setConversationType(String conversationType) {
        this.conversationType = conversationType;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public List<UserInfo> getUserInfoList() {
        return userInfoList;
    }

    public void setUserInfoList(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }
}