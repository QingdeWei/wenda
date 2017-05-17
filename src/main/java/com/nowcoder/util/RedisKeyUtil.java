package com.nowcoder.util;

/**
 * Created by ZGH on 2017/5/9.
 */
public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENTQUEUE = "EVENT_QUEUE";

    public static String getLikeKey(int entityType, int entityId){
         return BIZ_LIKE + SPLIT + String.valueOf(entityType)+ SPLIT + String.valueOf(entityId);
    }
    public static String getDislikeKey(int entityType, int entityId){
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType)+ SPLIT + String.valueOf(entityId);
    }
    public static String getEventQueueKey(){
        return BIZ_EVENTQUEUE;
    }
}
