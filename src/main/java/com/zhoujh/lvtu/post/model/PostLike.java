package com.zhoujh.lvtu.post.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("post_like")
public class PostLike {
    @TableId(type = IdType.ASSIGN_ID) // 雪花算法生成ID
    private Long id;
    private String postId;
    private String userId;

    public PostLike() {
    }
    public PostLike(String postId, String userId) {
        this.postId = postId;
        this.userId = userId;
    }
}
