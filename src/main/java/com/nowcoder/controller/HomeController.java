package com.nowcoder.controller;

import com.nowcoder.aspect.LikeLog;
import com.nowcoder.model.*;
import com.nowcoder.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.crypto.Cipher;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by VictorWei on 2017/4/7.
 */
@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    FollowService followService;
    @Autowired
    CommentService commentService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    LikeService likeService;

    private List<ViewObject> getQuestions(int userId, int offset, int limit) {
        List<Question> questionList = questionService.getLatestQuestions(userId, offset, limit);
        List<ViewObject> vos = new ArrayList<>();
        for (Question question : questionList) {
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("user", userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }


    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model,
                        @RequestParam(value = "pop", defaultValue = "0") int pop) {
        model.addAttribute("vos", getQuestions(0, 0, 10));
        return "index";
    }

    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId) {

        model.addAttribute("vos", getQuestions(userId, 0, 10));
        User user = userService.getUser(userId);
        //service方法还未实现
        int commentCount = commentService.getCommentCount(userId, EntityType.ENTITY_USER);

       ViewObject profileUser = getProfileUserInfo(userId);
        model.addAttribute("profileUser",profileUser);
        //model.addAttribute("profileUser",commentCount);

        return "profile";
    }


    private ViewObject getProfileUserInfo(int userId){
        ViewObject profileUser = new ViewObject();
        int commentCount = commentService.getUserCommentCount(userId);
        long followerCount = followService.getFollowerCount(EntityType.ENTITY_USER,userId);
        long followeeCount = followService.getFolloweeCount(userId,EntityType.ENTITY_USER);
        boolean followed = followService.isFollower(hostHolder.getUser().getId(),EntityType.ENTITY_USER,userId);
        long likeCount = likeService.getLikeCount(EntityType.ENTITY_USER,userId);
        User user = userService.getUser(userId);
        profileUser.set("commentCount",commentCount);
        profileUser.set("followerCount",followerCount);
        profileUser.set("followeeCount",followeeCount);
        profileUser.set("user",user);
        profileUser.set("followed",followed);
        profileUser.set("likeCount",likeCount);

        return profileUser;

    }

}
