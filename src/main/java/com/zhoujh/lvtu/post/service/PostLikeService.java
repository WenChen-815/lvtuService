package com.zhoujh.lvtu.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhoujh.lvtu.post.model.PostLike;

public interface PostLikeService extends IService<PostLike> {
    PostLike likePost(String postId, String userId);
    boolean unlikePost(String postId, String userId);
    PostLike isLikePost(String postId, String userId);
}
