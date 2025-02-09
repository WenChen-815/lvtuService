package com.zhoujh.lvtu.post.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("plan_participants")
public class PlanParticipant {
    @TableId(type = IdType.ASSIGN_ID) // 雪花算法生成ID
    private Long id;
    private String planId;
    private String userId;
    private LocalDateTime joinTime;

    public PlanParticipant() {
        this.joinTime = LocalDateTime.now();
    }
}
