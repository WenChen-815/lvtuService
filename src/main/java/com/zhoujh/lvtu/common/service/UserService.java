package com.zhoujh.lvtu.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhoujh.lvtu.common.model.User;
import com.zhoujh.lvtu.common.model.UserInfo;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
public interface UserService extends IService<User> {
    User registerAndLogin(User user);  // 用户登录注册

    boolean isPhoneNumExist(String phoneNum);  // 检查手机号是否已注册

    boolean isEmailExist(String email);  // 检查邮箱是否已注册

    /**
     * 根据用户昵称模糊搜索用户
     * @param userNameStr 用户昵称关键字
     * @param pageNum 当前页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    Page<UserInfo> searchUsersByUserName(String userId, String userNameStr, int pageNum, int pageSize);

    /**
     * 更新用户信息，包括头像上传
     * @param file 用户新头像
     * @param userName 用户昵称
     * @param userId 用户ID
     * @param gender 性别
     * @param email 邮箱
     * @param birth 生日
     * @return 是否更新成功
     */
    User updateUserInfo(MultipartFile file, String userName, String userId, Integer gender, String email, String birth);
}
