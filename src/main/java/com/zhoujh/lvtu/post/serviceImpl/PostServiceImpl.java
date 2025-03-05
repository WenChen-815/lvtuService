package com.zhoujh.lvtu.post.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhoujh.lvtu.common.model.UserRelationship;
import com.zhoujh.lvtu.post.mapper.PostMapper;
import com.zhoujh.lvtu.post.model.Post;
import com.zhoujh.lvtu.post.model.TravelPlan;
import com.zhoujh.lvtu.post.service.PostService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    @Override
    public Post createPost(Post post) {
        post.setPostId(UUID.randomUUID().toString());
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setStarCount(0);
        post.setCreateTime(LocalDateTime.now());
        post.setUpdateTime(LocalDateTime.now());
        boolean success = save(post); // 使用 Mybatis-Plus 提供的 save() 方法保存数据
        return success ? post : null;
    }

    @Override
    public Post getPostById(String postId) {
        return getById(postId); // 使用 Mybatis-Plus 提供的 getById() 方法获取帖子
    }

    @Override
    public Page<Post> getAllPosts(int pageNum, int pageSize) {
        // 创建分页对象
        Page<Post> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Post::getCreateTime); // 按创建时间倒序排序
        // 执行分页查询
        return this.page(page, queryWrapper);
    }

    @Override
    public Page<Post> getFollowPosts(int pageNum, int pageSize, List<UserRelationship> userRelationships) {
        List<Post> allPosts = new ArrayList<>();
        for (UserRelationship userRelationship : userRelationships) {
            LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Post::getUserId, userRelationship.getRelatedUserId());
            queryWrapper.orderByDesc(Post::getCreateTime); // 按创建时间倒序排序

            IPage<Post> tempPage = new Page<>(1, Integer.MAX_VALUE); // 获取所有结果
            this.page(tempPage, queryWrapper);
            allPosts.addAll(tempPage.getRecords());
        }

        // 对所有结果进行排序
        allPosts.sort((tp1, tp2) -> tp2.getUpdateTime().compareTo(tp1.getUpdateTime()));

        // 计算分页
        int total = allPosts.size();
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, total);

        List<Post> currentPageRecords = allPosts.subList(start, end);

        // 构建返回的 Page 对象
        Page<Post> resultPage = new Page<>(pageNum, pageSize, total);
        resultPage.setRecords(currentPageRecords);
        return resultPage;
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
            return removeById(postId);
        }
        return false;
    }

    @Override
    public List<Post> getPostsByUserId(String userId) {
        return baseMapper.selectList(new LambdaQueryWrapper<Post>().eq(Post::getUserId, userId));
    }

    @Override
    public void commentIncrease(String postId) {
        Post post = getPostById(postId);
        if (post != null) {
            post.setCommentCount(post.getCommentCount() + 1);
            updateById(post);
        }
    }

    @Override
    public boolean likePost(String postId, String userId) {
        Post post = getPostById(postId);
        if (post != null) {
            post.setLikeCount(post.getLikeCount() + 1);
            updateById(post);
            return true;
        }
        return false;
    }

    @Override
    public boolean unlikePost(String postId, String userId) {
        Post post = getPostById(postId);
        if (post != null) {
            post.setLikeCount(post.getLikeCount() - 1);
            updateById(post);
            return true;
        }
        return false;
    }

    @Override
    public Page<Post> getMyPosts(int pageNum, int pageSize, String userId) {
        // 创建分页对象
        Page<Post> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Post::getUserId, userId)
                .orderByDesc(Post::getCreateTime); // 按创建时间倒序排序
        // 执行分页查询
        return this.page(page, queryWrapper);
    }

    @Override
    public Page<Post> searchPostsByTitle(String titleStr, int pageNum, int pageSize) {
        Page<Post> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Post::getPostTitle, titleStr)
                .orderByDesc(Post::getCreateTime);
        return this.page(page, queryWrapper);
    }
}
