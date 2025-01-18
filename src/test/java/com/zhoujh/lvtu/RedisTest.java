package com.zhoujh.lvtu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    // 测试新增（设置键值对）
    public void testSet() {
        redisTemplate.opsForValue().set("testKey", "testValue");
        System.out.println("Set key-value pair in Redis");
    }
    @Test
    // 测试查询（获取值）
    public void testGet() {
        Object value = redisTemplate.opsForValue().get("testKey");
        System.out.println("Got value from Redis: " + value);
    }
    @Test
    // 测试修改（更新值）
    public void testUpdate() {
        redisTemplate.opsForValue().set("testKey", "newTestValue");
        System.out.println("Updated value in Redis");
    }
    @Test
    // 测试删除
    public void testDelete() {
        redisTemplate.delete("testKey");
        System.out.println("Deleted key from Redis");
    }
}