package com.jedis;

import com.common.Constant;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.Transaction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JedisTest {

    static JedisCluster jedis;

    @BeforeClass
    public static void beforeClass() {
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort(Constant.REDIS_ADDRESS, Constant.REDIS_PORT));
        jedisClusterNodes.add(new HostAndPort(Constant.REDIS_ADDRESS, Constant.REDIS_PORT+1));
        jedisClusterNodes.add(new HostAndPort(Constant.REDIS_ADDRESS, Constant.REDIS_PORT+2));

        jedis = new JedisCluster(jedisClusterNodes);
    }

    @Test
    public void testSimple() {
        //创建Jedis对象
        Jedis jedis=new Jedis(Constant.REDIS_ADDRESS, Constant.REDIS_PORT);

        jedis.set("key1", "jedis test");
        String string=jedis.get("key1");
        System.out.println(string);
        jedis.close();
    }

    @Test
    public void testCommandString() {
        String key = "{prod}_base_info_25560";
        String value = "bar";
        jedis.set(key, "bar");
        Assert.assertEquals(value, jedis.get(key));
    }

    @Test
    public void testCommandList() {
        jedis.lpush("queue#tasks", "firstTask");
        jedis.lpush("queue#tasks", "secondTask");

        String task = jedis.rpop("queue#tasks");

        System.out.println(task);
    }

    @Test
    public void testCommandSet() {
        jedis.sadd("nicknames", "nickname#1");
        jedis.sadd("nicknames", "nickname#2");
        jedis.sadd("nicknames", "nickname#1");

        Set<String> nicknames = jedis.smembers("nicknames");
        boolean exists = jedis.sismember("nicknames", "nickname#1");

        System.out.println(exists);
    }

    @Test
    public void testHash() {
        jedis.hset("user#1", "name", "Peter");
        jedis.hset("user#1", "job", "politician");

        String name = jedis.hget("user#1", "name");

        Map<String, String> fields = jedis.hgetAll("user#1");
        String job = fields.get("job");

        System.out.println(job);
    }

    @Test
    public void testSortedSet() {
        Map<String, Double> scores = new HashMap<>();

        scores.put("PlayerOne", 3000.0);
        scores.put("PlayerTwo", 1500.0);
        scores.put("PlayerThree", 8200.0);

        scores.entrySet().forEach(playerScore -> {
            jedis.zadd(playerScore.getKey(), playerScore.getValue(), playerScore.getKey());
        });
    }

    @Test
    // 事务方式(Transactions) redis的事务很简单，他主要目的是保障，一个client发起的事务中的命令可以连续的执行，而中间不会插入其他client的命令
    public void testTransactions() {
        String friendsPrefix = "teacher#";
        String userOneId = "4352523";
        String userTwoId = "5552321";
        Jedis jedis=new Jedis(Constant.REDIS_ADDRESS, Constant.REDIS_PORT);

        Transaction t = jedis.multi();
        t.sadd(friendsPrefix + userOneId, userTwoId);
        t.sadd(friendsPrefix + userOneId, userOneId);
        t.exec();
    }

    @Test
    // 事务方式(Transactions) redis的事务很简单，他主要目的是保障，一个client发起的事务中的命令可以连续的执行，而中间不会插入其他client的命令
    public void testPipeline() {
        String friendsPrefix = "teacher#";
        String userOneId = "4352523";
        String userTwoId = "5552321";

        jedis
    }



}
