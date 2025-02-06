package com.zhoujh.lvtu.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhoujh.lvtu.post.model.TravelPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TravelPlanMapper extends BaseMapper<TravelPlan> {
}
