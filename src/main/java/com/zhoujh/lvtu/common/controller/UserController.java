package com.zhoujh.lvtu.common.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhoujh.lvtu.common.model.User;
import com.zhoujh.lvtu.common.model.UserInfo;
import com.zhoujh.lvtu.common.model.UserRelationship;
import com.zhoujh.lvtu.common.serviceImpl.UserRelationshipServiceImpl;
import com.zhoujh.lvtu.common.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private UserRelationshipServiceImpl userRelationshipServiceImpl;

    @GetMapping("/user")
    public User getUser(@RequestParam String userId) {
        return userServiceImpl.getById(userId);
    }

    @GetMapping("getUserAvatarById")
    public String getUserAvatarById(@RequestParam String userId) {
        User user = userServiceImpl.getById(userId);
        return user.getAvatarUrl();
    }

    // 用户登录注册接口
    @PostMapping("/registerAndLogin")
    public User register(@RequestBody User user) { //用RequestBody注解将JSON自动转为User对象
        User user1 = userServiceImpl.registerAndLogin(user);
        return user1;
    }

    /**
     * 根据用户昵称模糊搜索用户
     * @param userNameStr 用户昵称关键字
     * @param pageNum 当前页码
     * @param pageSize 每页大小
     * @return 分页查询结果
     */
    @GetMapping("/search")
    public Page<UserInfo> searchUsersByUserName(@RequestParam String userId,
                                                @RequestParam String userNameStr,
                                                @RequestParam(defaultValue = "1") int pageNum,
                                                @RequestParam(defaultValue = "10") int pageSize) {
        return userServiceImpl.searchUsersByUserName(userId, userNameStr, pageNum, pageSize);
    }

    @PostMapping("/update")
    public User updateUserInfo(@RequestParam(value = "file", required = false) MultipartFile file,
                                 @RequestParam String userName,
                                 @RequestParam String userId,
                                 @RequestParam Integer gender,
                                 @RequestParam String email,
                                 @RequestParam String birth) {
        User updateUser = userServiceImpl.updateUserInfo(file, userName, userId, gender, email, birth);
        return updateUser;
    }

    @GetMapping("/getMutualFollowUser")
    public List<UserInfo> getMutualFollowUser(@RequestParam String userId) {
        List<UserRelationship> userRelationships = userRelationshipServiceImpl.getMutualFollowUser(userId);
        return userRelationships.stream().map(userRelationship -> {
            User user = userServiceImpl.getById(userRelationship.getRelatedUserId());
            return new UserInfo(
                    user.getUserId(),
                    user.getUserName(),
                    user.getStatus(),
                    user.getGender(),
                    user.getAge(),
                    user.getBirth(),
                    user.getAvatarUrl(),
                    userRelationship.getRelationshipType());
        }).collect(Collectors.toList());
    }
}
