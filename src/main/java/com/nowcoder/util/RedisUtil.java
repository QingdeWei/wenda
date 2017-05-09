package com.nowcoder.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nowcoder.model.User;
import javafx.animation.Interpolatable;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import java.util.FormatFlagsConversionMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ZGH on 2017/5/8.
 */
public class RedisUtil {

    public static void print(int num, String s){
        System.out.println(String.format("%d , %s", num,s));
    }
    public static void print(int num, List<String> s){
        System.out.println(String.format("%d , %s", num,s));
    }
    public static void print(int num, long s){
        System.out.println(String.format("%d , %s", num,s));
    }
    public static void print(int num, Map<String,String> s){
        System.out.println(String.format("%d , %s", num,s));
    }
    public static void print(int num, boolean s){
        System.out.println(String.format("%d , %s", num,s));
    }
    public static void print(int num, Set<String> s){
        System.out.println(String.format("%d , %s", num,s));
    }
    public static void print(int num, double s){
        System.out.println(String.format("%d , %s", num,s));
    }

    public static void main(String[] args){

        Jedis jedis = new Jedis("redis://localhost:6379/9");

        System.out.println("ping 后： " + jedis.ping());
        jedis.set("hello", "world");
        jedis.rename("hello","newhello");
        //过期时间可以用在验证码
        jedis.setex("hello1",15,"worlds");

        //阅读数,把读写放到内存提高性能
        jedis.set("pv", "100");
        jedis.incr("pv");
        jedis.incrBy("pv",5);
        print(1,jedis.get("pv"));
        jedis.decr("pv");
        jedis.decrBy("pv",3);
        print(2,jedis.get("pv"));
        print(3,jedis.get("*"));
        //
        String listName = "list";
        jedis.del(listName);
        for (int i=0; i<10; ++i){
            jedis.lpush(listName,"a"+ String.valueOf(i));
        }
        print(4,jedis.lrange(listName,0,12));
        print(4, jedis.lrange(listName, 0, 3));
        print(5,jedis.llen(listName));
        print(6,jedis.lpop(listName));
        print(7,jedis.llen(listName));
        print(8,jedis.lset(listName,5,"这里我修改啦"));
        print(8,jedis.lrange(listName,0,10));
        print(9,jedis.lindex(listName,2));
        //定位插入，可以在某个元素之前或之后插入
        jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER,"a5","xx");
        jedis.linsert(listName,BinaryClient.LIST_POSITION.BEFORE,"a5","xx");
        print(10,jedis.lrange(listName,0,12));

        //hash
        String userKey = "userxx";
        jedis.hset(userKey,"name","Jack");
        jedis.hset(userKey, "age", "14");
        jedis.hset(userKey,"phone","18818881777");
        print(11,jedis.hget(userKey,"name"));
        print(12,jedis.hgetAll(userKey));
        jedis.hdel(userKey,"phone");
        print(13,jedis.hgetAll(userKey));
        print(14,jedis.hexists(userKey,"age"));
        print(15,jedis.hexists(userKey,"email"));
        print(16,jedis.hkeys(userKey));
        print(17,jedis.hvals(userKey));
        jedis.hsetnx(userKey,"name","wei");
        jedis.hsetnx(userKey, "age", "34");
        jedis.hsetnx(userKey,"school","shu");
        print(18,jedis.hgetAll(userKey));


        //
        String likeKey1 = " commentLike1";
        String likeKey2 = " commentLike2";
        for (int i=0; i<10;++i){
            jedis.sadd(likeKey1,String.valueOf(i));
            jedis.sadd(likeKey2,String.valueOf(i*i));
        }
        print(19,jedis.smembers(likeKey1));
        print(20,jedis.smembers(likeKey2));

        print(21,jedis.sdiff(likeKey1,likeKey2)); //
        print(22,jedis.sunion(likeKey1,likeKey2));//集合的并集
        print(23,jedis.sinter(likeKey1,likeKey2));//集合的交集
        print(24,jedis.sismember(likeKey1,"14"));
        print(25,jedis.sismember(likeKey1,"3"));
        jedis.srem(likeKey1,"4");
        print(26,jedis.smembers(likeKey1));
        jedis.smove(likeKey2,likeKey1,"4");
        print(27,jedis.smembers(likeKey1));
        print(28,jedis.scard(likeKey1));

        //SortedSet 优先队列，可以用来做排行榜
        String rankKey  =" rankKey";
        jedis.zadd(rankKey,15,"jem");
        jedis.zadd(rankKey,89,"wei");
        jedis.zadd(rankKey,78,"Mao");
        jedis.zadd(rankKey,45,"tao");
        jedis.zadd(rankKey,66,"Liang");
        print(29,jedis.zcard(rankKey));
        print(30,jedis.zcount(rankKey,60,80));
        print(31,jedis.zscore(rankKey,"wei"));
        jedis.zincrby(rankKey,34,"wei");

        print(32,jedis.zscore(rankKey,"wei"));
        jedis.zincrby(rankKey,2,"luc");
        print(33,jedis.zrange(rankKey,0,100));
        //求出前几位
        print(34,jedis.zrange(rankKey,0,10));
        print(35,jedis.zrange(rankKey,0,3));//升序
        print(36,jedis.zrevrange(rankKey,0,3));//降序
        for (Tuple tuple:jedis.zrangeByScoreWithScores(rankKey,0,100)){
            print(37,tuple.getElement() + ": " + String.valueOf(tuple.getScore()));
        }
        print(38,jedis.zrank(rankKey,"wei"));
        print(39,jedis.zrevrank(rankKey,"wei"));

        String setKey = "zset";
        jedis.zadd(setKey,1,"a");
        jedis.zadd(setKey,1,"b");
        jedis.zadd(setKey,1,"c");
        jedis.zadd(setKey,1,"d");
        jedis.zadd(setKey,1,"e");
        //分值一样，按照key的字典序排序
        print(40,jedis.zlexcount(setKey,"-","+"));
        print(40,jedis.zlexcount(setKey,"[a","[d"));
        print(40,jedis.zlexcount(setKey,"(a","[d"));
        jedis.zrem(setKey,"a");
        print(41,jedis.zrange(setKey,0,8));
        //按照字典序把c以上的删除
        jedis.zremrangeByLex(setKey,"(c","+");
        print(42,jedis.zrange(setKey,0,8));
        //按照排名删除
        jedis.zremrangeByRank(setKey,0,0);
        print(43,jedis.zrange(setKey,0,8));
        jedis.zadd(setKey,90,"e90");
        jedis.zadd(setKey,50,"e50");
        jedis.zadd(setKey,40,"e40");

        //按照分数删除
        jedis.zremrangeByScore(setKey,80,100);
        print(44,jedis.zrange(setKey,0,9));
//        jedis.zremrangeByScore(setKey,"[80","+");

        jedis.set("pv","190");
        //连接池,默认8条线程，如果close方法不调用，只能打印8个
//        JedisPool pool = new JedisPool();
//        for (int i=0 ; i<100; ++i){
//            Jedis j = pool.getResource();
//            print(45,j.get("pv"));
//            j.close();
//        }
        User user = new User();
        user.setName("xxx");
        user.setPassword("123");
        user.setHeadUrl("a.png");
        user.setSalt("salt");
        user.setId(1);
        //以Json格式序列化
        jedis.set("user1", JSONObject.toJSONString(user));
        print(46,jedis.get("user1"));
        String value = jedis.get("user1");
        User user2 = JSON.parseObject(value,User.class);//可以直接用来缓存
        System.out.println();



















    }

}
