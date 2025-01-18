package com.zhoujh.lvtu.common.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhoujh.lvtu.common.mapper.UserMapper;
import com.zhoujh.lvtu.common.model.User;
import com.zhoujh.lvtu.common.model.UserInfo;
import com.zhoujh.lvtu.common.model.UserRelationship;
import com.zhoujh.lvtu.common.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService extends ServiceImpl<UserMapper, User>
        implements UserRepository {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRelationshipService userRelationshipService;
    // 从配置文件中读取文件存储路径
    @Value("${spring.resources.static-locations}")
    private String staticPath;

    @Override
    public User registerAndLogin(User user) {
        // 检查手机号是否已存在
        if (isPhoneNumExist(user.getPhoneNum())) {
            User user1 = userMapper.findByPhoneNum(user.getPhoneNum());
            // 对输入的密码进行MD5加密，并与数据库中的密码进行比较
            if (user.getPassword().equals(user1.getPassword())) {
                return user1;
            }
            return null;
        }
        // 随机创建一个用户名
        user.setUserName("用户_" + System.currentTimeMillis());
        user.setPassword(user.getPassword());
        user.setUserId(java.util.UUID.randomUUID().toString());
        user.setCreateTime(new Date());
        user.setStatus(1); // 默认状态为正常
        userMapper.insert(user);// 插入数据库
        return user;
    }
    @Override
    public boolean isPhoneNumExist(String phoneNum) {
        return userMapper.findByPhoneNum(phoneNum) != null;
    }

    @Override
    public boolean isEmailExist(String email) {
        return userMapper.findByEmail(email) != null;
    }

    @Override
    public Page<UserInfo> searchUsersByUserName(String userId, String userNameStr, int pageNum, int pageSize) {
        // 创建分页对象
        Page<User> page = new Page<>(pageNum, pageSize);

        // 使用 LambdaQueryWrapper 构造模糊查询条件
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(User::getUserName, userNameStr); // 模糊匹配用户昵称
        queryWrapper.orderByDesc(User::getCreateTime); // 按创建时间倒序排序
        // 执行分页查询
        Page<User> userPage = this.page(page, queryWrapper);

        // 将 User 转换为 UserInfo
        List<UserInfo> userInfoList = userPage.getRecords().stream().map(user -> {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(user.getUserId());
            userInfo.setUserName(user.getUserName());
            userInfo.setStatus(user.getStatus());
            userInfo.setAvatarUrl(user.getAvatarUrl());
            userInfo.setGender(user.getGender());
            userInfo.setAge(user.getAge());
            userInfo.setBirth(user.getBirth());

            // 查询当前用户与发起搜索用户的关系
            UserRelationship userRelationship = userRelationshipService.findRelationship(userId, user.getUserId());
            if (userRelationship != null){
                userInfo.setRelationship(userRelationship.getRelationshipType());
            } else {
                userInfo.setRelationship(0);
            }

            return userInfo;
        }).collect(Collectors.toList());

        // 返回分页结果
        Page<UserInfo> resultPage = new Page<>();
        resultPage.setRecords(userInfoList);
        resultPage.setCurrent(userPage.getCurrent());
        resultPage.setSize(userPage.getSize());
        resultPage.setTotal(userPage.getTotal());
        return resultPage;
    }

    @Override
    public boolean updateUserInfo(MultipartFile file, String userName, String userId, Integer gender, String email, String birth) {
        // 校验用户是否存在
        User user = this.getById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        // 保存头像文件
        String avatarUrl = user.getAvatarUrl(); // 默认保留旧头像
        if (file != null && !file.isEmpty()) {
            try {
                // 构造存储路径
                String basePath = staticPath.replace("file:", ""); // 去掉前缀
                File directory = new File(basePath, "userAvatar");
                if (!directory.exists()) {
                    directory.mkdirs(); // 确保目录存在
                }
                // 构造文件名
                String fileName = user.getUserId() + "_" + file.getOriginalFilename();
                File destination = new File(directory, fileName);
                file.transferTo(destination); // 保存文件
                avatarUrl = "/lvtu/userAvatar/" + fileName; // 更新头像路径
            } catch (IOException e) {
                throw new RuntimeException("头像上传失败", e);
            }
        }
        // 更新用户信息
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getUserId, userId)
                .set(User::getUserName, userName)
                .set(User::getGender, gender)
                .set(User::getEmail, email)
                .set(User::getBirth, birth)
                .set(User::getAvatarUrl, avatarUrl);
        return this.update(updateWrapper);
    }
}
