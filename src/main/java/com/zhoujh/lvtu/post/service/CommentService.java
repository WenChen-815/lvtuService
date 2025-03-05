package com.zhoujh.lvtu.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhoujh.lvtu.post.model.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentService extends IService<Comment> {
    List<Comment> getCommentsByPostId(String postId);

    Comment addComment(Comment comment);

    int deleteByPostId(String postId);
}

