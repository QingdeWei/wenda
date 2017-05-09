package com.nowcoder.service;

import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ZGH on 2017/5/9.
 */
@Service
public class LikeService {
    @Autowired
    JedisAdaptor jedisAdaptor;

    public long getLikeCount(int entityType, int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        return jedisAdaptor.scard(likeKey);
    }

    public int getLikeStatus(int userId,int entityType, int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        if (jedisAdaptor.sismember(likeKey,String.valueOf(userId))){
            return 1;
        }
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType,entityId);
        return jedisAdaptor.sismember(dislikeKey,String.valueOf(userId)) ? -1 : 0 ;
    }

    public long like(int userId,int entityType, int entityId){

        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        jedisAdaptor.sadd(likeKey,String.valueOf(userId));
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType,entityId);
        jedisAdaptor.srem(dislikeKey,String.valueOf(userId));
        return jedisAdaptor.scard(likeKey);
    }

    public long dislike(int userId,int entityType, int entityId){
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType,entityId);
        jedisAdaptor.sadd(dislikeKey, String.valueOf(userId));
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        jedisAdaptor.srem(likeKey,String.valueOf(userId));
        return jedisAdaptor.scard(likeKey);
    }
}
