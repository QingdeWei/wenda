package com.nowcoder.controller;

import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.service.LikeService;
import com.nowcoder.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ZGH on 2017/5/9.
 */
@Controller
public class LikeController {

    @Autowired
    LikeService likeService;
    @Autowired
    HostHolder hostHolder;
    @RequestMapping(path = {"/like"},method ={RequestMethod.POST})
    @ResponseBody
    String like(@RequestParam("commentId")int commentId){

        if (hostHolder.getUser() == null){
            return WendaUtil.getJSONString(999);
        }
        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT,commentId);
        return WendaUtil.getJSONString(0,String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"},method ={RequestMethod.POST})
    @ResponseBody
    String dislike(@RequestParam("commentId")int commentId){

        if (hostHolder.getUser() == null){
            return WendaUtil.getJSONString(999);
        }
        long likeCount = likeService.dislike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT,commentId);
        return WendaUtil.getJSONString(0,String.valueOf(likeCount));
    }
}
