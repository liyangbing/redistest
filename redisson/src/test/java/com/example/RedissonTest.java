package com.example;

import com.jedis.Constant;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;

public class RedissonTest {

    static Config config = new Config();


    @BeforeClass
    public static void beforeClass() {
        config.useClusterServers()
                .addNodeAddress("redis://" + Constant.REDIS_ADDRESS + ":" + Constant.REDIS_PORT)
                .addNodeAddress("redis://" + Constant.REDIS_ADDRESS + ":" + (Constant.REDIS_PORT + 1))
                .addNodeAddress("redis://" + Constant.REDIS_ADDRESS + ":" + (Constant.REDIS_PORT + 2));
    }

    @Test
    public void testSync() {
        RedissonClient redissonClient = Redisson.create(config);

        String key = "key";
        String value = "value";
        RBucket<Object> result = redissonClient.getBucket(key);

        result.set(value);

        Assert.assertEquals(value, result.get().toString());

        String mapKey = "mapKey";
        RMap<String, String> rmap = redissonClient.getMap(mapKey);
        rmap.put(key, value);

        Assert.assertEquals(value, rmap.get(key));


        // lock
        redissonClient.getBucket(key).delete();
        RLock lock = redissonClient.getLock(key);
        lock.lock();
        //
        lock.unlock();
    }

    @Test
    public void testReactive() {
        RedissonReactiveClient redissonClient = Redisson.createReactive(config);

        String key = "key";
        String value = "value";
        RBucketReactive<Object> result = redissonClient.getBucket(key);

        result.set(value);

        Assert.assertEquals(value, result.get().toString());

        String mapKey = "mapKey";
        RMapReactive<String, String> rmap = redissonClient.getMap(mapKey);
        rmap.put(key, value);

        Assert.assertEquals(value, rmap.get(key));

        // lock
        redissonClient.getBucket(key).delete();
        RLockReactive lock = redissonClient.getLock(key);
        lock.lock();
        //
        lock.unlock();

    }

    @Test
    public void testRxJava2() {
        RedissonRxClient redissonClient = Redisson.createRx(config);

        String key = "key";
        String value = "value";
        RBucketRx<Object> result = redissonClient.getBucket(key);

        result.set(value);

        Assert.assertEquals(value, result.get().toString());

        String mapKey = "mapKey";
        RMapRx<String, String> rmap = redissonClient.getMap(mapKey);
        rmap.put(key, value);

        Assert.assertEquals(value, rmap.get(key));

        // lock
        redissonClient.getBucket(key).delete();
        RLockRx lock = redissonClient.getLock(key);
        lock.lock();
        //
        lock.unlock();

    }



}
