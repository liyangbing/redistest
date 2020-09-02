package com.example;

import com.jedis.Constant;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.sync.RedisStringCommands;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.cluster.api.reactive.RedisClusterReactiveCommands;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LettuceTest {

    static RedisClusterClient client;

    @BeforeClass
    public static void beforeClass() {
        List<RedisURI> list = new ArrayList<>();
        list.add(RedisURI.create(Constant.REDIS_ADDRESS, Constant.REDIS_PORT));
        list.add(RedisURI.create(Constant.REDIS_ADDRESS, Constant.REDIS_PORT+1));
        list.add(RedisURI.create(Constant.REDIS_ADDRESS, Constant.REDIS_PORT+2));
        client = RedisClusterClient.create(list);

    }

    @Test
    public void testSync() {
        StatefulRedisClusterConnection<String, String> connection = client.connect();

        RedisStringCommands sync = connection.sync();

        String key = "key";
        String value = "value";
        sync.set(key, value);

        Assert.assertEquals(value, sync.get(key));
    }

    @Test
    public void testASync() throws Exception {
        StatefulRedisClusterConnection<String, String> connection = client.connect();

        RedisAdvancedClusterAsyncCommands<String, String> async = connection.async();

        String key = "key";
        String value = "value";
        RedisFuture<String> set = async.set(key, value);
        RedisFuture<String> get = async.get(key);

        set.await(1, TimeUnit.MINUTES);
        get.await(1, TimeUnit.MINUTES);

        Assert.assertEquals("OK", set.get());

        Assert.assertEquals(value, get.get());
    }

    @Test
    public void testReactive() {
        StatefulRedisClusterConnection<String, String> connection = client.connect();
        RedisClusterReactiveCommands<String, String> reactive = connection.reactive();

        String key = "key";
        String value = "value";

        Mono<String> set = reactive.set(key, value);
        Mono<String> get = reactive.get(key);

        set.subscribe();

        Assert.assertEquals(value, get.block());
    }

}
