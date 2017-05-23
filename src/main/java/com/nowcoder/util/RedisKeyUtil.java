package com.nowcoder.util;

/**
 * Created by ZGH on 2017/5/9.
 */
public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENTQUEUE = "EVENT_QUEUE";
    //粉丝
    private static String BIZ_FOLLOWER = "FOLLOWER";
    //关注对象
    private static String BIZ_FLOOWEE = "FOLLOWEE";

    private static String BIZ_TIMELINE = "TIMELINE";

    public static String getLikeKey(int entityType, int entityId){
         return BIZ_LIKE + SPLIT + String.valueOf(entityType)+ SPLIT + String.valueOf(entityId);
    }
    public static String getDislikeKey(int entityType, int entityId){
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType)+ SPLIT + String.valueOf(entityId);
    }
    public static String getFollowerKey(int entityType,int entityId){
        return BIZ_FOLLOWER + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
    public static String getFolloweeKey(int userId, int entityType){
        return BIZ_FLOOWEE + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
    }

    public static String getEventQueueKey(){
        return BIZ_EVENTQUEUE;
    }
}
