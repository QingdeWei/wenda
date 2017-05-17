package com.nowcoder.async;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.service.JedisAdaptor;
import com.nowcoder.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZGH on 2017/5/16.
 */
@Service
public class EventConsumer implements InitializingBean,ApplicationContextAware{

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    private Map<EventType, List<EventHandler>> config = new HashMap<EventType,List<EventHandler>>();
    private ApplicationContext applicationContext;

    @Autowired
    JedisAdaptor jedisAdaptor;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null) {
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();

                for (EventType type : eventTypes) {
                    if (!config.containsKey(type)) {
                        config.put(type, new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    String key = RedisKeyUtil.getEventQueueKey();
                    List<String> events = jedisAdaptor.brpop(0,key);
                    for (String message : events){
                        //因为brpop方法的返回值第一个是key，所以要先过滤掉
                        if (message.equals(key)){
                            continue;
                        }
                        //之前是将事件转成JSON串推进redis里，这里取出事件将其反序列化
                        EventModel eventModel = JSONObject.parseObject(message, EventModel.class);

                        if (!config.containsKey(eventModel.getType())){
                            logger.error("不能识别的事件" );
                            continue;
                        }
                        for (EventHandler handler: config.get(eventModel.getType())){
                            handler.doHandler(eventModel);

                        }
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
    }
}
