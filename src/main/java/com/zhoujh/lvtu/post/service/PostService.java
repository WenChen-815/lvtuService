package com.zhoujh.lvtu.post.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhoujh.lvtu.post.mapper.PostMapper;
import com.zhoujh.lvtu.post.model.Post;
import com.zhoujh.lvtu.post.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PostService extends ServiceImpl<PostMapper, Post> implements PostRepository {

    @Override
    public Post createPost(Post post) {
        post.setPostId(UUID.randomUUID().toString());
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setStarCount(0);
        post.setCreateTime(LocalDateTime.now());
        post.setUpdateTime(LocalDateTime.now());
        boolean success = save(post); // 使用 Mybatis-Plus 提供的 save() 方法保存数据
        return success? post : null;
    }

    @Override
    public Post getPostById(String postId) {
        return getById(postId); // 使用 Mybatis-Plus 提供的 getById() 方法获取帖子
    }

    @Override
    public List<Post> getAllPosts() {
        return list(); // 使用 Mybatis-Plus 提供的 list() 方法获取所有帖子
    }

    @Override
    public boolean updatePost(Post post) {
        post.setUpdateTime(LocalDateTime.now()); // 更新时设置修改时间
        return updateById(post); // 使用 Mybatis-Plus 提供的 updateById() 方法更新帖子
    }

    @Override
    public boolean deletePost(String postId) {
        Post post = getPostById(postId);
        if (post != null) {
            post.setStatus(2); // 设置为已删除状态
            post.setUpdateTime(LocalDateTime.now()); // 设置修改时间
            return updateById(post); // 更新数据库
        }
        return false;
    }
}
