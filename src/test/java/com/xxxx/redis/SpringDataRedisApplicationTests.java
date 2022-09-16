package com.xxxx.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest(classes = SpringDataRedisApplication.class)
public class SpringDataRedisApplicationTests {
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void initConn() {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set("username","lisi");
        ValueOperations<Object, Object> value = redisTemplate.opsForValue();
        value.set("name","wangwu");
        System.out.println(ops.get("name"));//null
        System.out.println(value.get("name"));//wangwu
        System.out.println(value.get("username"));//null
        System.out.println(ops.get("username"));//lisi
    }
    @Test
    public void test01(){
        redisTemplate.opsForValue().set("cc","cc");
        System.out.println(redisTemplate.opsForValue().get("cc"));
    }
}
