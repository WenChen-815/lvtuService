package com.zhoujh.lvtu.common.controller;

import cn.hutool.core.lang.copier.SrcToDestCopier;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhoujh.lvtu.common.model.User;
import com.zhoujh.lvtu.common.model.UserInfo;
import com.zhoujh.lvtu.common.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    // 用户登录注册接口
    @PostMapping("/registerAndLogin")
    public User register(@RequestBody User user) { //用RequestBody注解将JSON自动转为User对象
        User user1 = userService.registerAndLogin(user);
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
        return userService.searchUsersByUserName(userId, userNameStr, pageNum, pageSize);
    }

    @PostMapping("/update")
    public String updateUserInfo(@RequestParam(value = "file", required = false) MultipartFile file,
                                 @RequestParam String userName,
                                 @RequestParam String userId,
                                 @RequestParam Integer gender,
                                 @RequestParam String email,
                                 @RequestParam String birth) {
        boolean result = userService.updateUserInfo(file, userName, userId, gender, email, birth);
        return result ? "用户信息更新成功" : "用户信息更新失败";
    }
}
