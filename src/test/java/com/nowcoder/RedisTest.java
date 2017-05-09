package com.nowcoder;

import redis.clients.jedis.Jedis;

/**
 * Created by ZGH on 2017/5/4.
 */
public class RedisTest {
   public static void main(String[] args){
       Jedis jedis = new Jedis("localhost");
       jedis.set("wei","hellowoerld");
       String value = jedis.get("wei");
       System.out.println(value);
       System.out.println(jedis.ping());
   }




}
