package com.zhoujh.lvtu.post.controller;

import com.zhoujh.lvtu.post.model.Comment;
import com.zhoujh.lvtu.post.model.Post;
import com.zhoujh.lvtu.post.service.CommentService;
import com.zhoujh.lvtu.post.serviceImpl.CommentServiceImpl;
import com.zhoujh.lvtu.post.serviceImpl.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentServiceImpl commentServiceImpl;
    @Autowired
    private PostServiceImpl postServiceImpl;

    @GetMapping("/getByPostId")
    public ResponseEntity<List<Comment>> getComments(@RequestParam String postId) {
        return ResponseEntity.ok(commentServiceImpl.getCommentsByPostId(postId));
    }

    @PostMapping("/addComment")
    public Comment addComment(@RequestBody Comment comment) {
        postServiceImpl.commentIncrease(comment.getPostId());
        return commentServiceImpl.addComment(comment);
    }
}

