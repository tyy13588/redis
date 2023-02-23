package com.it.jedis;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

public class JedisDemo1 {
    public static void main(String[] args) {
        //创建jedis 对象
        Jedis jedis = new Jedis("192.168.95.134", 6379);
        String ping = jedis.ping();
        System.out.println(ping);
    }


    //操作key
    @Test
    public void demo1() {
        //创建jedis 对象
        Jedis jedis = new Jedis("192.168.95.134", 6379);

        //添加
        jedis.set("name", "tyy");
        //获取
        String name = jedis.get("name");

        //设置多个 key— value
        jedis.mset("k1", "v1", "k2", "v2");
        List<String> mget = jedis.mget("k1", "k2");
        System.out.println(mget);
        System.out.println(name);
        for (String key : jedis.keys("*")) {
            System.out.println(key);
        }

    }


    //操作key
    @Test
    public void demo3() {
        //创建jedis 对象
        Jedis jedis = new Jedis("192.168.95.134", 6379);
//        jedis.hset("user", "age", "20");
//        String hget = jedis.hget("user", "age");
//        System.out.println(hget);
        jedis.zadd("zset01", 100d, "z3");
        jedis.zadd("zset01", 90d, "l4");
        jedis.zadd("zset01", 80d, "w5");
        jedis.zadd("zset01", 70d, "z6");

        Set<String> zrange = jedis.zrange("zset01", 0, -1);
        for (String e : zrange) {
            System.out.println(e);
        }
    }

}
