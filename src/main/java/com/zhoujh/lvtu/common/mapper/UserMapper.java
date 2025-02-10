package com.zhoujh.lvtu.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhoujh.lvtu.common.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * FROM user WHERE phone_num = #{phoneNum}")
    User findByPhoneNum(String phoneNum); // 根据手机号查找用户
    @Select("SELECT * FROM user WHERE email = #{email}")
    User findByEmail(String email); // 根据邮箱查找用户
    @Select("SELECT * FROM user WHERE user_id = #{userId}")
    User getUserById(String relatedUserId);
}
