package com.xxxx.redis;

import com.xxxx.redis.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

@SpringBootTest(classes = SpringDataRedisApplication.class)
public class SpringDataRedisApplicationTests {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 测试项目搭建
     */
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

    /**
     * 测试项目搭建
     */
    @Test
    public void test01(){
        redisTemplate.opsForValue().set("cc","cc");
        System.out.println(redisTemplate.opsForValue().get("cc"));
        System.out.println(redisTemplate.opsForValue().get("username"));
    }

    /**
     * 测试序列化
     */
    @Test
    public void test02(){
        User user = new User();
        user.setId(20);
        user.setUserName("admin");
        user.setPassword("root");
        redisTemplate.opsForValue().set("user::id::200",user);
        System.out.println(redisTemplate.opsForValue().get("user::id::200"));
    }
}
