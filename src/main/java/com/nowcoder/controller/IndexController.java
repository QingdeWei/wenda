package com.nowcoder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ZGH on 2017/3/31.
 */
@Controller
public class IndexController {
    @RequestMapping(path = {"/","/index"})
    @ResponseBody
    public  String index(){
        return "Hello Nowcoder";
    }

    @RequestMapping(path = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public  String profile(@PathVariable("userId")int userId,
                           @PathVariable("groupId")String groupId,
                           @RequestParam("type") int type,
                           @RequestParam(value = "value",defaultValue = "zzzz") String value){
        return String.format("Profile Page of %s/ %d , t= %d, k = %s", groupId,userId, type, value );
    }
}
