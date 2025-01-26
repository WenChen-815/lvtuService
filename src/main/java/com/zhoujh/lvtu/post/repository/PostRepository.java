package com.zhoujh.lvtu.post.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhoujh.lvtu.post.model.Post;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends IService<Post> {
    Post createPost(Post post);
    Post getPostById(String postId);
    List<Post> getAllPosts();
    boolean updatePost(Post post);
    boolean deletePost(String postId);
}
