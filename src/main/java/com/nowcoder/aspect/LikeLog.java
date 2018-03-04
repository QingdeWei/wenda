package com.nowcoder.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ZGH on 2017/8/15.
 */
public class LikeLog {
    private static final Logger logger = LoggerFactory.getLogger(LikeLog.class);

    @Before("com.nowcoder.controller.likeController.(/like)")
    public void beforeMethod(JoinPoint joinPoint){
        StringBuilder sb = new StringBuilder();
        System.out.println("点赞服务启动。。。");
    }
}
