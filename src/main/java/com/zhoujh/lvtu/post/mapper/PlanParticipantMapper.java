package com.zhoujh.lvtu.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhoujh.lvtu.post.model.PlanParticipant;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PlanParticipantMapper extends BaseMapper<PlanParticipant> {
    @Select("SELECT * FROM plan_participants WHERE plan_id = #{planId}")
    List<PlanParticipant> findByPlanId(@Param("planId") String planId);

    @Select("SELECT * FROM plan_participants WHERE plan_id = #{planId} AND user_id = #{userId}")
    PlanParticipant findByPlanIdAndUserId(@Param("planId") String planId, @Param("userId") String userId);

    @Delete("DELETE FROM plan_participants WHERE plan_id = #{planId} AND user_id = #{userId}")
    int deleteByPlanIdAndUserId(@Param("planId") String planId, @Param("userId") String userId);

    @Delete("DELETE FROM plan_participants WHERE plan_id = #{planId}")
    int deleteByPlanId(@Param("planId") String planId);
}
