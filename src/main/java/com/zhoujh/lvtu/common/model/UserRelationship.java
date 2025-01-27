package com.zhoujh.lvtu.common.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_relationship")
public class UserRelationship {
    @TableId
    private String id; // 关系ID
    private String userId; // 用户ID
    private String relatedUserId; // 关联的用户ID
    private LocalDateTime createTime; // 创建时间
    private Integer relationshipType; // 关系类型 (1: 好友, 2: 关注, 3: 拉黑)
}
