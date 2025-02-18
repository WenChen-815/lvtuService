package com.zhoujh.lvtu.utils;

import com.google.gson.Gson;
import com.zhoujh.lvtu.common.model.LocationMessage;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@Service
public class LocationShareMsgSubscriber implements MessageListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocationShareMsgSubscriber.class);
    private final Gson gson = new Gson();

    @Override
    public void onMessage(@NotNull Message message, byte[] bytes) {
        String channel = new String(message.getChannel());
        String str0 = new String(message.getBody());
        String str1 = str0.replace("\\", "");
        // 去除两端的"
        String payload = str1.substring(1, str1.length() - 1);
        if (channel.startsWith("group:")) {
            LOGGER.info("111receive消息：{}", payload);
            LocationMessage locationMessage = gson.fromJson(payload, LocationMessage.class);
            String groupId = channel.substring(6);

            // 获取本地会话并广播
//            Set<WebSocketSession> sessions = LocationWebSocketHandler.groupSessions.getOrDefault(groupId, Collections.emptySet());
//            sessions.forEach(session -> {
//                try {
//                    // 排除消息发送者本身
//                    if (session.isOpen() && !session.getId().equals(locationMessage.getSenderId())) {
//                        session.sendMessage(new TextMessage(payload));
//                    }
//                } catch (IOException e) {
//                    LOGGER.error("消息发送失败: {}", e.getMessage());
//                }
//            });
        }
    }
}
