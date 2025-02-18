package com.zhoujh.lvtu.utils;

import com.google.gson.Gson;
import com.zhoujh.lvtu.common.model.LocationMessage;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LocationWebSocketHandler extends TextWebSocketHandler implements MessageListener {
    private final Gson gson = new Gson();
    private final static Logger LOGGER = LoggerFactory.getLogger(LocationWebSocketHandler.class);
    private static final ConcurrentHashMap<String, Set<WebSocketSession>> groupSessions = new ConcurrentHashMap<>();
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOGGER.info("Test Socket 连接成功，sessionId：{}", session.getId());
    }

    @Override
    public void handleMessage(@NotNull WebSocketSession session, @NotNull WebSocketMessage<?> message) throws Exception {
        if (message instanceof TextMessage) {
            final String msg = ((TextMessage) message).getPayload();
            handlerTextMessage(session, (TextMessage) message);
        } else {
            LOGGER.error("Socket 消息处理失败，只接受 文本消息，sessionId：{}", session.getId());
        }
    }


    public void handlerTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            final String msg = message.getPayload();
            LocationMessage locationMessage = gson.fromJson(msg, LocationMessage.class);
            String groupId = locationMessage.getGroupId();

            // 将会话加入本地group
            String resultGroupId = addSessionToGroup(groupId, session, locationMessage.getType());

            // 设置消息发送者标识
            locationMessage.setSenderId(session.getId());

            // 通过Redis广播消息
            redisTemplate.convertAndSend("group:" + resultGroupId, gson.toJson(locationMessage));
        } catch (Exception e) {
            LOGGER.error("消息处理失败: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    // Redis消息监听接口实现
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String str0 = new String(message.getBody());
        String str1 = str0.replace("\\", "");
        // 去除两端的"
        String payload = str1.substring(1, str1.length() - 1);
        if (channel.startsWith("group:")) {
            LocationMessage locationMessage = gson.fromJson(payload, LocationMessage.class);
            String groupId = channel.substring(6);

            // 获取本地会话并广播
            Set<WebSocketSession> sessions = groupSessions.getOrDefault(groupId, Collections.emptySet());
            boolean isExist = groupSessions.containsKey(groupId);
            sessions.forEach(session -> {
                try {
//                     排除消息发送者本身
                    LOGGER.info("发送消息");
                    if (session.isOpen() && !session.getId().equals(locationMessage.getSenderId())) {
                        session.sendMessage(new TextMessage(payload));
                    }
                } catch (IOException e) {
                    LOGGER.error("消息发送失败: {}", e.getMessage());
                }
            });
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        LOGGER.error("Socket 处理异常，sessionId：{}, 异常原因：{}", session.getId(), exception.getMessage());
        exception.printStackTrace();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        // 从所有group中移除会话
        groupSessions.forEach((group, sessions) -> sessions.remove(session));
        LOGGER.info("Socket 关闭，sessionId：{}", session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private String addSessionToGroup(String groupId, WebSocketSession session, int type) {
        // 动态订阅Redis频道（确保每个group只订阅一次）
        String channel;
        if (type == LocationMessage.SINGLE_TYPE) {
            String[] groupIdSp = groupId.split("#");
            boolean isExist0 = groupSessions.containsKey(groupId);
            boolean isExist1 = groupSessions.containsKey(groupIdSp[1] + "#" + groupIdSp[0]);
            if (isExist0) {
                // do nothing
            } else if (isExist1) {
                groupId = groupIdSp[1] + "#" + groupIdSp[0];
            } else {
                channel = "group:" + groupId;
                redisMessageListenerContainer.addMessageListener(this, new ChannelTopic(channel));
                groupSessions.putIfAbsent(groupId, ConcurrentHashMap.newKeySet());
            }
        } else if (type == LocationMessage.GROUP_TYPE) {
            channel = "group:" + groupId;
            if (!groupSessions.containsKey(groupId)) {
                redisMessageListenerContainer.addMessageListener(this, new ChannelTopic(channel));
                groupSessions.putIfAbsent(groupId, ConcurrentHashMap.newKeySet());
            }
        }
        // 将会话加入group
        groupSessions.get(groupId).add(session);
        return groupId;
    }
}