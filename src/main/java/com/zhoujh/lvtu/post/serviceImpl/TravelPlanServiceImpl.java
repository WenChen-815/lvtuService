package com.zhoujh.lvtu.post.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhoujh.lvtu.post.model.TravelPlan;
import com.zhoujh.lvtu.post.mapper.TravelPlanMapper;
import com.zhoujh.lvtu.post.service.TravelPlanService;
import org.springframework.stereotype.Service;

@Service
public class TravelPlanServiceImpl extends ServiceImpl<TravelPlanMapper, TravelPlan> implements TravelPlanService {
    @Override
    public TravelPlan createPlan(TravelPlan travelPlan) {
        return this.save(travelPlan)? travelPlan : null;
    }

}
