package com.zhoujh.lvtu.post.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhoujh.lvtu.post.model.Post;
import org.springframework.stereotype.Repository;

@Repository
public interface PostService extends IService<Post> {
    Post createPost(Post post);
    Post getPostById(String postId);
    Page<Post> getAllPosts(int pageNum, int pageSize);
    boolean updatePost(Post post);
    boolean deletePost(String postId);
}
