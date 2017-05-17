package com.nowcoder.async;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.service.JedisAdaptor;
import com.nowcoder.util.RedisKeyUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by ZGH on 2017/5/16.
 */
@Service
public class EventProducer {

    @Autowired
    JedisAdaptor jedisAdaptor;

    public Boolean fireEvent(EventModel eventModel){
        try{
            //这里可以用BlockingQueue
//            BlockingQueue<EventModel> q = ArrayBlockingQueue<EventModel>();
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdaptor.lpush(key, json);
            return true;


        }catch (Exception e){
            return false;
        }
    }
}
