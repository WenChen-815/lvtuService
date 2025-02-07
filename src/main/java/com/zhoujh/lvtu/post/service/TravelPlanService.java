package com.zhoujh.lvtu.post.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhoujh.lvtu.post.model.TravelPlan;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelPlanService extends IService<TravelPlan> {
    TravelPlan createPlan(TravelPlan travelPlan);

    Page<TravelPlan> getAllPlans(int pageNum, int pageSize);
}