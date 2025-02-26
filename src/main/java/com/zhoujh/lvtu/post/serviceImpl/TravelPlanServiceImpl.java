package com.zhoujh.lvtu.post.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhoujh.lvtu.common.model.UserRelationship;
import com.zhoujh.lvtu.post.model.TravelPlan;
import com.zhoujh.lvtu.post.mapper.TravelPlanMapper;
import com.zhoujh.lvtu.post.service.TravelPlanService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        queryWrapper.eq(TravelPlan::getStatus, 1);
        queryWrapper.orderByDesc(TravelPlan::getUpdateTime);
        return this.page(page, queryWrapper);
    }

    // 获取关注的人的Plans
    @Override
    public Page<TravelPlan> getFollowPlans(int pageNum, int pageSize, List<UserRelationship> followList) {
        List<TravelPlan> allTravelPlans = new ArrayList<>();
        for (UserRelationship userRelationship : followList) {
            LambdaQueryWrapper<TravelPlan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TravelPlan::getUserId, userRelationship.getRelatedUserId());
            queryWrapper.orderByDesc(TravelPlan::getUpdateTime);

            IPage<TravelPlan> tempPage = new Page<>(1, Integer.MAX_VALUE); // 获取所有结果
            this.page(tempPage, queryWrapper);
            allTravelPlans.addAll(tempPage.getRecords());
        }

        // 对所有结果进行排序
        allTravelPlans.sort((tp1, tp2) -> tp2.getUpdateTime().compareTo(tp1.getUpdateTime()));

        // 计算分页
        int total = allTravelPlans.size();
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, total);

        List<TravelPlan> currentPageRecords = allTravelPlans.subList(start, end);

        // 构建返回的 Page 对象
        Page<TravelPlan> resultPage = new Page<>(pageNum, pageSize, total);
        resultPage.setRecords(currentPageRecords);
        return resultPage;
    }


    @Override
    public List<TravelPlan> getPlansByUserId(String userId) {
        return this.list(new LambdaQueryWrapper<TravelPlan>().eq(TravelPlan::getUserId, userId));
    }

    @Override
    public TravelPlan createPlanGroup(String travelPlanId, String groupId) {
        TravelPlan travelPlan = this.getById(travelPlanId);
        if (travelPlan == null) {
            return null;
        }
        if (travelPlan.getConversationId() != null && !travelPlan.getConversationId().equals("")) {
            return null;
        }
        travelPlan.setConversationId(groupId);
        return this.updateById(travelPlan) ? travelPlan : null;
    }

    @Override
    public Page<TravelPlan> getMyPlans(int pageNum, int pageSize, String userId) {
        Page<TravelPlan> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<TravelPlan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TravelPlan::getUserId, userId).orderByDesc(TravelPlan::getUpdateTime);
        return this.page(page, queryWrapper);
    }

}
