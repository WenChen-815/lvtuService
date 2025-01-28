package com.zhoujh.lvtu.post.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhoujh.lvtu.post.mapper.CommentMapper;
import com.zhoujh.lvtu.post.model.Comment;
import com.zhoujh.lvtu.post.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> getCommentsByPostId(String postId) {
        return commentMapper.getCommentsByPostId(postId);
    }

    @Override
    public Comment addComment(Comment comment) {
        comment.setId(UUID.randomUUID().toString());
        return commentMapper.insert(comment) > 0 ? comment: null;
    }
}

