package com.xxxx.redis;

import com.xxxx.redis.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

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
        ops.set("username", "lisi");
        ValueOperations<Object, Object> value = redisTemplate.opsForValue();
        value.set("name", "wangwu");
        System.out.println(ops.get("name"));//null
        System.out.println(value.get("name"));//wangwu
        System.out.println(value.get("username"));//null
        System.out.println(ops.get("username"));//lisi
    }

    /**
     * 测试项目搭建
     */
    @Test
    public void test01() {
        redisTemplate.opsForValue().set("cc", "cc");
        System.out.println(redisTemplate.opsForValue().get("cc"));
        System.out.println(redisTemplate.opsForValue().get("username"));
    }

    /**
     * 测试序列化
     */
    @Test
    public void test02() {
        User user = new User();
        user.setId(20);
        user.setUserName("admin");
        user.setPassword("root");
        redisTemplate.opsForValue().set("user::id::200", user);
        System.out.println(redisTemplate.opsForValue().get("user::id::200"));
    }

    /**
     * 操作String
     * 添加、获取、层级存储、删除、自增、过期
     */
    @Test
    public void testString() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        //过期
        valueOperations.set("abc", "abc", 300L, TimeUnit.SECONDS);
        //添加数据
        valueOperations.set("username", "zhangsan");
        valueOperations.set("age", 18);
        //以层级关系、目录形式存储数据
        valueOperations.set("user:01", "lisi");
        valueOperations.set("user:02", "wangwu");
        // 添加多条数据
        Map<String, String> userMap = new HashMap<>();
        userMap.put("address", "bj");
        userMap.put("sex", "1");
        valueOperations.multiSet(userMap);
        // 获取一条数据
        System.out.println(valueOperations.get("username"));
        // 获取多条数据
        //方式一
        valueOperations.multiGet(Arrays.asList("username", "age", "address", "sex")).forEach(System.out::println);
        System.out.println("----------------------------");
        //方式二
        List<String> keys = new ArrayList<>();
        keys.add("username");
        keys.add("age");
        keys.add("address");
        keys.add("sex");
        valueOperations.multiGet(keys).forEach(System.out::println);
        // 删除
        redisTemplate.delete("username");
        //自增
        System.out.println(valueOperations.increment("account::id", 100));
        //查看过期时间
        System.out.println(redisTemplate.getExpire("abc", TimeUnit.SECONDS));
        //判断是否存在
        System.out.println(redisTemplate.hasKey("abc"));
        //
        System.out.println(redisTemplate.keys("abc"));
        //判断类型
        System.out.println(redisTemplate.type("abc"));
        //过期处理
        redisTemplate.expire("abc", 10L, TimeUnit.SECONDS);
        //查看过期时间
        System.out.println(redisTemplate.getExpire("abc", TimeUnit.SECONDS));
        //过期处理
//        redisTemplate.expireAt("abc",)
    }

    /**
     * 操作Hash
     */
    @Test
    public void testHash() {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        //添加
        hashOperations.put("userInfo", "name", "lisi");
        Map<String, String> map = new HashMap<>();
        map.put("age", "20");
        map.put("sex", "1");
        hashOperations.putAll("userInfo", map);
        //获取
        System.out.println(hashOperations.get("userInfo", "name"));
        //批量获取方式一
        List<String> keys = new ArrayList<>();
        keys.add("age");
        keys.add("sex");
        hashOperations.multiGet("userInfo", keys).forEach(System.out::println);
        System.out.println("-------------------------");
        //批量获取方式二
        hashOperations.multiGet("userInfo", Arrays.asList("name", "age", "sex")).forEach(System.out::println);
        //获取hash类型所以数据
        Map<String, String> userMap = hashOperations.entries("userInfo");
        userMap.forEach((k, v) -> System.out.println(k + "--" + v));
        for (Map.Entry<String, String> userInfo : userMap.entrySet()) {
            System.out.println(userInfo.getKey() + "--" + userInfo.getValue());
        }
        //删除
        hashOperations.delete("userInfo", "name");
    }

    /**
     * 操作List
     */
    @Test
    public void testList() {
        //添加(入队)
        ListOperations<String,Object> listOperations = redisTemplate.opsForList();
        listOperations.leftPush("students","wang wu");
        listOperations.leftPush("students","li si","zhang san");
        listOperations.rightPush("students","zhao liu","sun qi");
        listOperations.rightPush("students","zhou ba");
        //根据索引修改元素
        listOperations.set("students",1,"SpringCloud");
        //获取（-1代表到最后）
        listOperations.range("students",0,2).forEach(System.out::println);
        System.out.println(listOperations.index("students", 1));
        //获取总条数
        System.out.println("总条数"+listOperations.size("students"));
        //出队
        System.out.println(listOperations.rightPop("students"));
        //删除
        listOperations.remove("students",1,"li si");
        redisTemplate.delete("students");
    }
}