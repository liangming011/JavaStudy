package redis;

import redis.clients.jedis.Jedis;

import java.util.*;

public class Redis数据结构 {


    public static void main(String[] args) {
        //连接本地的 Redis 服务
        Jedis jedis = connectRedis();

        //Redis Java String(字符串) 实例
        redisString(jedis);

        //Redis Java List(列表) 实例
        redisList(jedis);

        //Redis Java Set(集合) 实例
        redisSet(jedis);

        //Redis Java ZSet(有序集合) 实例
        redisZSet(jedis);

        //Redis Java Hash(哈希) 实例
        redisHash(jedis);

        //Redis Java Keys 实例
        Set<String> keys = jedis.keys("*");
        Iterator<String> it=keys.iterator() ;
        while(it.hasNext()){
            String key = it.next();
            System.out.println(key);
        }
    }

    //连接到 redis 服务
    public static Jedis connectRedis() {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("localhost");
        System.out.println("连接成功");
        //查看服务是否运行
        System.out.println("服务正在运行: "+jedis.ping()+"\n");
        return jedis;
    }

    //Redis Java String(字符串) 实例
    public static void redisString(Jedis jedis) {
        //设置 redis 字符串数据
        jedis.set("mystring", "www.runoob.com");
        // 获取存储的数据并输出
        System.out.println("redis 存储的字符串为: "+ jedis.get("runoobkey")+"\n");
    }

    public static void redisList(Jedis jedis) {
        //存储数据到列表中
        jedis.lpush("mylist", "Runoob");
        jedis.lpush("mylist", "Google");
        jedis.lpush("mylist", "Taobao");
        // 获取存储的数据并输出
        List<String> list = jedis.lrange("site-list", 0 ,2);
        for(int i=0; i<list.size(); i++) {
            System.out.println("列表项为: "+list.get(i));
        }
        System.out.println("\n");
    }

    public static void redisSet(Jedis jedis) {
        //设置 redis Set数据
        jedis.sadd("myset","myset1");
        jedis.sadd("myset","myset2");
        jedis.sadd("myset","myset3");
        jedis.sadd("myset","myset2");
        // 获取存储的数据并输出
        Set<String> set = jedis.smembers("myset");
        for (String str : set) {
            System.out.println("Set项为: "+str);
        }
//        Iterator<String> it = set.iterator();
//        while (it.hasNext()) {
//            String str = it.next();
//            System.out.println("Set项为: "+str);
//        }
        System.out.println("\n");

    }

    public static void redisZSet(Jedis jedis) {
        //设置 redis Zset数据,不允许值重复
        jedis.zadd("myzset",3,"myzset3");
        jedis.zadd("myzset",2,"myzset2");
        jedis.zadd("myzset",1,"myzset1");
        jedis.zadd("myzset",2,"myzset1");
        jedis.zadd("myzset",2,"myzset1");
        // 获取存储的数据并输出
        Set<String> set = jedis.zrange("myzset",0,5);
        for (String str : set) {
            System.out.println("ZSet项为: "+str);
        }
        System.out.println("\n");
    }

    public static void redisHash(Jedis jedis) {
        //创建 map
        Map map = new HashMap<>();
        map.put("field","master");

        //设置 redis Zset数据,不允许值重复
        jedis.hmset("myhash",map);
        jedis.hmset("myhash",map);
        // 获取存储的数据并输出
        //List<String> list = Collections.singletonList(jedis.hget("myhash", "filed"));//获取一个
        List<String> list = jedis.hmget("myhash", new String[]{"field","field1","field2"});//获取一个
        for(int i=0; i<list.size(); i++) {
            System.out.println("Hash项为: "+list.get(i));
        }
        System.out.println("\n");
    }
}
