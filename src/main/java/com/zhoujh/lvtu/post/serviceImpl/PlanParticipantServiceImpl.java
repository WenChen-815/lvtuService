package com.zhoujh.lvtu.post.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhoujh.lvtu.post.mapper.PlanParticipantMapper;
import com.zhoujh.lvtu.post.model.PlanParticipant;
import com.zhoujh.lvtu.post.service.PlanParticipantService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlanParticipantServiceImpl
        extends ServiceImpl<PlanParticipantMapper, PlanParticipant>
        implements PlanParticipantService {
    @Override
    public List<PlanParticipant> findByPlanId(String planId) {
        return baseMapper.findByPlanId(planId);
    }

    @Override
    public boolean addParticipant(PlanParticipant participant) {
        if (participant.getJoinTime() == null) {
            participant.setJoinTime(LocalDateTime.now());
        }
        return this.save(participant);
    }

    @Override
    public PlanParticipant findByPlanIdAndUserId(String planId, String userId) {
        return baseMapper.findByPlanIdAndUserId(planId, userId);
    }

    @Override
    public boolean deleteByPlanIdAndUserId(String planId, String userId) {
        return baseMapper.deleteByPlanIdAndUserId(planId, userId)>=0;
    }

    @Override
    public boolean deleteByPlanId(String planId) {
        return baseMapper.deleteByPlanId(planId) >= 0;
    }

    @Override
    public List<PlanParticipant> getParticipants(String travelPlanId) {
        return baseMapper.findByPlanId(travelPlanId);
    }
}
