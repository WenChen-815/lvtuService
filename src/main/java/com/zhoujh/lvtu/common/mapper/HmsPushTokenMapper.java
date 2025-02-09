package com.zhoujh.lvtu.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhoujh.lvtu.common.model.HmsPushToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HmsPushTokenMapper extends BaseMapper<HmsPushToken> {
    @Select("select * from hms_push_token where user_id = #{userId}")
    // 根据用户ID查询所有Token
    List<HmsPushToken> selectByUserId(@Param("userId") String userId);

    @Select("select * from hms_push_token where user_id = #{userId} and hms_token = #{hmsToken}")
    // 根据用户ID和Token查询记录
    HmsPushToken selectByUserAndToken(@Param("userId") String userId,
                                      @Param("hmsToken") String hmsToken);
}
