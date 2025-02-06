package com.zhoujh.lvtu.common.controller;

import com.zhoujh.lvtu.utils.HuaweiPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class PushController {

    @Autowired
    private HuaweiPushService huaweiPushService;

    @GetMapping("/sendPush")
    public String sendPush(@RequestParam String deviceToken, @RequestParam String message) {
        try {
            String accessToken = huaweiPushService.getAccessToken();
            huaweiPushService.sendPushMessage(accessToken, deviceToken, message);
            return "Push message sent successfully!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to send push message: " + e.getMessage();
        }
    }
}
