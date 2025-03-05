package com.zhoujh.lvtu.common.controller;

import com.zhoujh.lvtu.IM.modle.UserConversation;
import com.zhoujh.lvtu.IM.serviceImpl.UserConversationServiceImpl;
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
    @Autowired
    private final UserConversationServiceImpl userConversationServiceImpl;
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

    @PostMapping("/clientMsgPush")
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

    @PostMapping("/clientGroupMsgPush")
    public String clientGroupMsgPush(@RequestBody ClientPush clientPush) {
        UserConversation userConversation = userConversationServiceImpl.getByConversationIdAndUserId(clientPush.getTargetId(), clientPush.getPusherId());
        for(String s: userConversation.getMembers()){
            if (!s.equals(clientPush.getPusherId())){
                String tokens = tokenServiceImpl.createTokens(s);
                String json = "{\n" +
                        "    \"validate_only\": false,\n" +
                        "    \"message\": {\n" +
                        "        \"data\": \"{'title':'"+clientPush.getTitle()+"'," +
                        "'body':'"+clientPush.getBody()+"'," +
                        "'messageTag':'IM'," +
                        "'tagUserId':'"+ s +"'}\",\n" +
                        "        \"token\": ["+ tokens +"]\n" +
                        "    }\n" +
                        "}";
                System.out.println(json);
                try {
                    huaweiPushService.sendPushMessage(json);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return "推送成功";
    }
}
