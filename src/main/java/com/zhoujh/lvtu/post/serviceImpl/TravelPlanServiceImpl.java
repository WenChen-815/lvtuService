package com.zhoujh.lvtu.post.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    @Override
    public Page<TravelPlan> getAllPlans(int pageNum, int pageSize) {
        Page<TravelPlan> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<TravelPlan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(TravelPlan::getUpdateTime);
        return this.page(page, queryWrapper);
    }

}
