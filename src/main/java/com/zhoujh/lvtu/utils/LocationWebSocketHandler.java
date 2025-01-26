package com.zhoujh.lvtu.utils;

import com.google.gson.Gson;
import com.zhoujh.lvtu.common.model.LocationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashSet;
import java.util.Set;

@Component
public class LocationWebSocketHandler extends TextWebSocketHandler {
    private final Gson gson = new Gson();
    private final RedisTemplate<String, Set<WebSocketSession>> redisTemplate = new RedisTemplate<>();
    private final static Logger LOGGER = LoggerFactory.getLogger(LocationWebSocketHandler.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOGGER.info("Test Socket 连接成功，sessionId：{}", session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message instanceof TextMessage) {
            final String msg = ((TextMessage)message).getPayload();
            // 判断消息是否以#remove开头，如果是，则移除该Session 格式为 #remove,groupId
            if (msg.startsWith("#remove,")){
                String[] split = msg.split(",");
                if (split.length==2){
                    String groupId = split[1];
                    Set<WebSocketSession> webSocketSessions = redisTemplate.opsForValue().get(groupId);
                    if (webSocketSessions!=null){
                        webSocketSessions.remove(session);
                        redisTemplate.opsForValue().set(groupId, webSocketSessions, 6, java.util.concurrent.TimeUnit.HOURS);
                    }
                }
            } else{
                handlerTextMessage(session, (TextMessage) message);
            }
        } else {
            LOGGER.error("Test Socket 消息处理失败，只接受 文本消息，sessionId：{}", session.getId());
        }
    }


    public void handlerTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        final String msg = message.getPayload();
        LocationMessage locationMessage = gson.fromJson(msg, LocationMessage.class);
        // 查询Redis中所有key为groupId的值
        Set<WebSocketSession> webSocketSessions = redisTemplate.opsForValue().get(locationMessage.getGroupId());
        // 如果为空，则创建一个
        if (webSocketSessions==null) webSocketSessions = new HashSet<WebSocketSession>();
        // 遍历Set，发送消息
        for (WebSocketSession webSocketSession : webSocketSessions) {
            if (webSocketSession.isOpen()&& !webSocketSession.equals(session)) {
                webSocketSession.sendMessage(message);
            }
        }
        webSocketSessions.add(session);
        // 更新Redis中的Set 有效时间为6小时
        redisTemplate.opsForValue().set(locationMessage.getGroupId(), webSocketSessions, 6, java.util.concurrent.TimeUnit.HOURS);
        session.sendMessage(message);
        LOGGER.info("收到消息：{}", msg);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        LOGGER.error("Test Socket 处理异常，sessionId：{}, 异常原因：{}", session.getId(), exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        LOGGER.info("Test Socket 关闭，sessionId：{}", session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}