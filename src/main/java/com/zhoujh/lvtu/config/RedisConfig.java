package com.zhoujh.lvtu.config;

import com.zhoujh.lvtu.utils.LocationShareMsgSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 设置key的序列化器为StringRedisSerializer
        template.setKeySerializer(new StringRedisSerializer());
        // 设置value的序列化器为GenericJackson2JsonRedisSerializer
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // 设置hash的key的序列化器为StringRedisSerializer
        template.setHashKeySerializer(new StringRedisSerializer());
        // 设置hash的value的序列化器为GenericJackson2JsonRedisSerializer
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // ChannelTopic对应Redis的subscribe命令，指定一个channel
//        container.addMessageListener(messageListenerAdapter(new LocationShareMsgSubscriber()), new ChannelTopic("group:abc"));
        // PatternTopic对应Redis的psubscribe命令，channel的名称可以包含通配符（*、?、[]），如group:*   表示所有以group:开始的channel
//        container.addMessageListener(messageListenerAdapter(new LocationShareMsgSubscriber()), new PatternTopic("group:*"));
        return container;
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter (LocationShareMsgSubscriber locationShareMsgSubscriber){
        // 如果没有implements MessageListener，需要指定方法名
//        return new MessageListenerAdapter(locationShareMsgSubscriber, "onMessage");
        return new MessageListenerAdapter(locationShareMsgSubscriber);
    }
}