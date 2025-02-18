package com.zhoujh.lvtu.IM.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhoujh.lvtu.IM.modle.UserConversation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserConversationMapper extends BaseMapper<UserConversation> {
    @Select("SELECT * FROM user_conversation " +
            "WHERE conversation_id = #{conversationId} AND user_id = #{userId}")
    UserConversation getByConversationIdAndUserId(String conversationId, String userId);

    @Select("SELECT * FROM user_conversation WHERE user_id = #{userId}")
    List<UserConversation> getByUserId(String userId);
}
