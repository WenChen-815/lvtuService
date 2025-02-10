package com.zhoujh.lvtu.common.controller;

import com.zhoujh.lvtu.common.model.ClientPush;
import com.zhoujh.lvtu.common.serviceImpl.HmsPushTokenServiceImpl;
import com.zhoujh.lvtu.utils.HuaweiPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/push")
@RequiredArgsConstructor
public class HmsPushController {
    @Autowired
    private final HmsPushTokenServiceImpl tokenServiceImpl;
    @Autowired
    private final HuaweiPushService huaweiPushService;
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

    @PostMapping("/clientPush")
    public String pushRequest(@RequestBody ClientPush clientPush) {
        String tokens = tokenServiceImpl.createTokens(clientPush.getTargetId());
        String json = "{\n" +
                "    \"validate_only\": false,\n" +
                "    \"message\": {\n" +
                "        \"data\": \"{'title':'"+clientPush.getTitle()+"'," +
                "'body':'"+clientPush.getBody()+"'," +
                "'messageTag':'IM'," +
                "'tagUserId':'"+ clientPush.getTargetId() +"'}\",\n" +
                "        \"token\": ["+ tokens +"]\n" +
                "    }\n" +
                "}";
        try {
            huaweiPushService.sendPushMessage(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "推送成功";
    }
}
