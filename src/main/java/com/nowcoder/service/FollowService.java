package com.nowcoder.service;

import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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

    }

}
