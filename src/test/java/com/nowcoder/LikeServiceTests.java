package com.nowcoder;

import com.nowcoder.service.LikeService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by VictorWei on 2017/7/11.
 * 测试四部曲：
 * 1.初始化数据
 * 2.执行要测试的业务
 * 3.验证测试的数据
 * 4.清理数据
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
public class LikeServiceTests {
    @Autowired
    LikeService likeService;

    @Before
    public void setUp(){
        System.out.println("setUp");
    }

    @After
    public void tearDown(){
        System.out.println("tearDown");
    }

    @Test
    public void testLike(){
        System.out.println("testLike");
        likeService.like(1,1,1);
        Assert.assertEquals(1,likeService.getLikeStatus(1,1,1));

        likeService.dislike(1,1,1);
        Assert.assertEquals(-1,likeService.getLikeStatus(1,1,1));

    }

    @Test
    public void testXXX(){
        System.out.println("testXXX");
    }
}
