package com.zhoujh.lvtu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{
    /**
     * 通信文本消息和二进制缓存区大小
     * 避免对接 第三方 报文过大时，Websocket 1009 错误
     *
     * @return
     */

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        // 在此处设置bufferSize
        container.setMaxTextMessageBufferSize(10240000);
        container.setMaxBinaryMessageBufferSize(10240000);
        container.setMaxSessionIdleTimeout(15 * 60000L);
        return container;
    }

    // 这个 bean 用于在 Spring Boot 中支持 WebSocket，它会自动注册使用了 @ServerEndpoint 注解的端点
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(locationWebSocketHandler(), "/ws/location")
                .setAllowedOrigins("*");
    }
    public LocationWebSocketHandler locationWebSocketHandler() {
        return new LocationWebSocketHandler();
    }
}
