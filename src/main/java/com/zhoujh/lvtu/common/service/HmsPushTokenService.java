package com.zhoujh.lvtu.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhoujh.lvtu.common.model.HmsPushToken;

import java.util.List;

public interface HmsPushTokenService extends IService<HmsPushToken> {

    // 保存或更新Token
    boolean saveOrUpdateToken(String userId, String hmsToken);

    // 获取用户所有Token
    List<String> getTokensByUserId(String userId);

    // 删除指定Token
    boolean removeToken(String userId, String hmsToken);
}
