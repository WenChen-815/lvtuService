package com.zhoujh.lvtu.common.controller;

import com.zhoujh.lvtu.common.serviceImpl.HmsPushTokenServiceImpl;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/push")
@RequiredArgsConstructor
public class HmsPushTokenController {

    private final HmsPushTokenServiceImpl tokenServiceImpl;

    // 注册/更新Token
    @PostMapping("/register")
    public String registerToken(@RequestParam String userId,
                              @RequestParam String hmsToken) {
        boolean result = tokenServiceImpl.saveOrUpdateToken(userId, hmsToken);
        return result ? "注册成功" : "注册失败";
    }

    // 获取用户所有Token
    @GetMapping("/{userId}")
    public List<String> getTokens(@PathVariable String userId) {
        return tokenServiceImpl.getTokensByUserId(userId);
    }

    // 删除指定Token
    @DeleteMapping
    public String removeToken(@RequestParam String userId,
                            @RequestParam String hmsToken) {
        boolean result = tokenServiceImpl.removeToken(userId, hmsToken);
        return result ? "删除成功" : "删除失败";
    }
}
