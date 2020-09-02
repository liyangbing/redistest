package com.example;

import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

public class SpringTest {

    public static ApplicationContext ctx;

    public static JedisConnectionFactory jedisConnectionFactory;

    public RedisConnection redisConnection;

    private RedisTemplate redisTemplate;

    @BeforeClass
    public static void setBeforeClass() {
        ctx = new ClassPathXmlApplicationContext("spring.xml");
        jedisConnectionFactory = (JedisConnectionFactory) ctx
                .getBean("jedisConnectionFactory");
    }

    @Before
    public void setBefore() {
        redisConnection = jedisConnectionFactory.getConnection();
        redisTemplate = (RedisTemplate) ctx.getBean("redisTemplate");
    }

    @After
    public void setAfter() {
        redisConnection.close();
    }

    @Test
    public void testConnection() {
        String key = "key";
        String value = "value";
        redisConnection.set(key.getBytes(), value.getBytes());

        Assert.assertEquals(value, new String(redisConnection.get(key.getBytes())));
    }

    @Test
    public void testTemplate() {
        String key = "key";
        String value = "value";
        redisTemplate.boundValueOps(key).set(value);

        Assert.assertEquals(value, redisTemplate.boundValueOps(key).get());

        redisTemplate.boundListOps("namelist1").rightPush("刘备");
        redisTemplate.boundListOps("namelist1").rightPush("关羽");
        redisTemplate.boundListOps("namelist1").rightPush("张飞");

        List list = redisTemplate.boundListOps("namelist1").range(0, 10);
        System.out.println(list);

        redisTemplate.boundHashOps("namehash").put("a", "唐僧");
        redisTemplate.boundHashOps("namehash").put("b", "悟空");
        redisTemplate.boundHashOps("namehash").put("c", "八戒");
        redisTemplate.boundHashOps("namehash").put("d", "沙僧");

    }

}
