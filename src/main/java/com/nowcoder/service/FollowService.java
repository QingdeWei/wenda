package com.nowcoder.service;

import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import javax.jnlp.IntegrationService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by ZGH on 2017/5/23.
 */
@Service
public class FollowService {
    @Autowired
    JedisAdaptor jedisAdaptor;

    //关注
    public boolean follow(int userId, int entityType, int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        Date date = new Date();
        Jedis jedis = jedisAdaptor.getJedis();
        Transaction tx = jedisAdaptor.multi(jedis);
        tx.zadd(followerKey,date.getTime(),String.valueOf(userId));
        tx.zadd(followeeKey, date.getTime(), String.valueOf(entityId));
        List<Object> ret = jedisAdaptor.exec(tx, jedis);
        return ret.size()==2 && (Long)ret.get(0)>0 && (Long) ret.get(1)>0;
    }

    public boolean unfollow(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        Date date = new Date();
        Jedis jedis = jedisAdaptor.getJedis();
        Transaction tx = jedisAdaptor.multi(jedis);
        // 实体的粉丝增加当前用户
        tx.zrem(followerKey, String.valueOf(userId));
        // 当前用户对这类实体关注-1
        tx.zrem(followeeKey, String.valueOf(entityId));
        List<Object> ret = jedisAdaptor.exec(tx, jedis);
        return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
    }


    public List<Integer> getIdsFromSet(Set<String> idset){
        List<Integer> ids = new ArrayList<>();
        for (String str: idset){
            ids.add(Integer.parseInt(str));
        }
        return ids;
    }
    public List<Integer> getFollowers(int entityType, int entityId,int count){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);

        return getIdsFromSet(jedisAdaptor.zrevrange(followerKey,0,count));
    }
    public List<Integer> getFollowers(int entityType, int entityId, int offset, int count){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(jedisAdaptor.zrevrange(followerKey, offset, offset + count));
    }
    public List<Integer> getFollowees(int userId, int entityType, int count){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
        return getIdsFromSet(jedisAdaptor.zrevrange(followeeKey, 0, count));
    }
    public List<Integer> getFollowees(int userId, int entityType, int offset, int count){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return getIdsFromSet(jedisAdaptor.zrevrange(followeeKey, offset, offset + count));
    }

    public long getFollowerCount(int entityType, int entityId){
        String followKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return jedisAdaptor.zcard(followKey);
    }
    public long getFolloweeCount(int userId, int entityType){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
        return jedisAdaptor.zcard(followeeKey);
    }

    //判断用户是否关注了某个实体
    public boolean isFollower(int userId, int entityType, int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        return jedisAdaptor.zscore(followerKey, String.valueOf(userId)) != null;
    }

}
