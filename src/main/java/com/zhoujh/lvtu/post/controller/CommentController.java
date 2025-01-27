package com.zhoujh.lvtu.post.controller;

import com.zhoujh.lvtu.post.model.Comment;
import com.zhoujh.lvtu.post.service.CommentService;
import com.zhoujh.lvtu.post.serviceImpl.CommentServiceImpl;
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

    @GetMapping("/getByPostId")
    public ResponseEntity<List<Comment>> getComments(@RequestParam String postId) {
        return ResponseEntity.ok(commentServiceImpl.getCommentsByPostId(postId));
    }

    @PostMapping("addComment")
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentServiceImpl.addComment(comment));
    }
}

