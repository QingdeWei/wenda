package com.nowcoder.controller;

import com.nowcoder.model.Question;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.crypto.Cipher;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ZGH on 2017/4/7.
 */
@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);


    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;


    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET})
    public String index(Model model){
        List<Question>questionsList =  questionService.getLatestQuestions(0, 0, 10);
//        model.addAttribute("questions", questionsList);

        List<ViewObject> vos = new ArrayList<ViewObject>();
        for (Question question: questionsList){
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("user",userService.getUser(question.getUserId()));
            vos.add(vo);

        }
         model.addAttribute("vos", vos);
         return "index";
     }
}
