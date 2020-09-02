package com.example;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 使用Lettuce 操作redis单机版和redis集群
 */
public class LettuceClient {
    public static void main(String[] args) {
        operCluster();
    }

    public static void operSingle(){
        RedisClient client = RedisClient.create(RedisURI.create("redis://127.0.0.1:7001"));
        StatefulRedisConnection<String,String> connect = client.connect();

        /**
         * 同步调用
         */
        RedisCommands<String,String> commands = connect.sync();
        commands.set("hello","hello world");
        String str = commands.get("hello");
        System.out.println(str);

        /**
         * 异步调用
         */
        RedisAsyncCommands<String,String> asyncCommands = connect.async();
        RedisFuture<String> future = asyncCommands.get("hello");
        try {
            String str1 = future.get();
            System.out.println(str1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        connect.close();
        client.shutdown();
    }

    public static void operCluster(){
        List<RedisURI> list = new ArrayList<>();
        list.add(RedisURI.create("redis://127.0.0.1:7001"));
        list.add(RedisURI.create("redis://127.0.0.1:7002"));
        list.add(RedisURI.create("redis://127.0.0.1:7003"));
        list.add(RedisURI.create("redis://127.0.0.1:7004"));
        list.add(RedisURI.create("redis://127.0.0.1:7005"));
        list.add(RedisURI.create("redis://127.0.0.1:7006"));
        RedisClusterClient client = RedisClusterClient.create(list);
        StatefulRedisClusterConnection<String, String> connect = client.connect();

        /**
         * 同步执行命令
         */
        RedisAdvancedClusterCommands<String, String> commands = connect.sync();
        commands.set("hello","hello world");
        String str = commands.get("hello");
        System.out.println(str);

        /**
         * 异步执行命令
         */
        RedisAdvancedClusterAsyncCommands<String,String> asyncCommands = connect.async();
        RedisFuture<String> future = asyncCommands.get("hello");

        try {
            String str1 = future.get();
            System.out.println(str1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        connect.close();
        client.shutdown();
    }
}
