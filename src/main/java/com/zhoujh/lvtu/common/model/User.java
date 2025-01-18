package com.zhoujh.lvtu.common.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@TableName("user")
@Builder
public class User {
    @TableId
    private String userId;       // 用户ID (UUID)
    @TableField("user_name")
    private String userName;     // 用户昵称 符合驼峰命名法则不需要用此注解
    private String phoneNum;     // 手机号
    private String email;        // 邮箱
    private String password;     // 密码
    private Integer status;      // 用户状态
    private Date createTime;     // 创建时间
    private Integer gender;      // 性别
    private Integer age;         // 年龄
    private String birth;        // 生日
    private String avatarUrl;    // 头像路径
    private Date updateTime;

    public User() {
    }

    public User(String userId, String userName, String phoneNum, String email, String password, Integer status, Date createTime, Integer gender, Integer age,String birth,String avatarUrl, Date updateTime) {
        this.userId = userId;
        this.userName = userName;
        this.phoneNum = phoneNum;
        this.email = email;
        this.password = password;
        this.status = status;
        this.createTime = createTime;
        this.gender = gender;
        this.age = age;
        this.birth = birth;
        this.avatarUrl = avatarUrl;
        this.updateTime = updateTime;
    }
}