package com.zhoujh.lvtu.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhoujh.lvtu.post.model.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    @Select("SELECT * FROM comments WHERE post_id = #{postId} ORDER BY create_time")
    List<Comment> getCommentsByPostId(String postId);

    @Select("INSERT INTO comments (id, post_id, parent_id, user_id, reply_to_user_id, content, create_time) VALUES (#{id}, #{postId}, #{parentId}, #{userId}, #{replyToUserId}, #{content}, #{createTime})")
    Comment addComment(Comment comment);
}

