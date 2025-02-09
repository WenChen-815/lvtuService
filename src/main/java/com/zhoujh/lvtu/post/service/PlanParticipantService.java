package com.zhoujh.lvtu.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhoujh.lvtu.post.model.PlanParticipant;

import java.util.List;

public interface PlanParticipantService extends IService<PlanParticipant> {
    List<PlanParticipant> findByPlanId(String planId);
    // 新增参与者记录
    boolean addParticipant(PlanParticipant participant);

    PlanParticipant findByPlanIdAndUserId(String planId, String userId);

    boolean deleteByPlanIdAndUserId(String planId, String userId);

    boolean deleteByPlanId(String planId);

    List<PlanParticipant> getParticipants(String travelPlanId);
}
