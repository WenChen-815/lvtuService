package com.zhoujh.lvtu.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhoujh.lvtu.post.model.PostLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PostLikeMapper extends BaseMapper<PostLike> {
    @Select("select * from post_like where post_id = #{postId} and user_id = #{userId}")
    PostLike getByPostIdAndUserId(String postId, String userId);
}
