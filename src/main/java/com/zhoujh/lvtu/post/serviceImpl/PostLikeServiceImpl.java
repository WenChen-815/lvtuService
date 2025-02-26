package com.zhoujh.lvtu.post.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhoujh.lvtu.post.mapper.PostLikeMapper;
import com.zhoujh.lvtu.post.model.PostLike;
import com.zhoujh.lvtu.post.service.PostLikeService;
import org.springframework.stereotype.Service;

@Service
public class PostLikeServiceImpl extends ServiceImpl<PostLikeMapper, PostLike> implements PostLikeService {
    @Override
    public PostLike likePost(String postId, String userId) {
        PostLike postLike = this.getBaseMapper().getByPostIdAndUserId(postId, userId);
        if (postLike != null) {
            return postLike;
        }
        // 添加点赞记录
        postLike = new PostLike(postId, userId);
        return this.save(postLike)? postLike : null;
    }

    @Override
    public boolean unlikePost(String postId, String userId) {
        PostLike postLike = this.getBaseMapper().getByPostIdAndUserId(postId, userId);
        if (postLike != null) {
            return this.removeById(postLike.getId());
        }
        return false;
    }

    @Override
    public PostLike isLikePost(String postId, String userId) {
        return this.getBaseMapper().getByPostIdAndUserId(postId, userId);
    }
}
