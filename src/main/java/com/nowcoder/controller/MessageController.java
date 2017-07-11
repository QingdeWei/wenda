package com.nowcoder.controller;

import com.nowcoder.model.HostHolder;

import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by VictorWei on 2017/5/2.
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;

    @RequestMapping(path ={"/msg/addMessage"},method = {RequestMethod.POST})
    @ResponseBody //因为是一个弹框，就用一个Jason的返回
    public String addMessage(@RequestParam("toName")String toName,
                             @RequestParam("content")String content){
    try {
        if (hostHolder.getUser() != null){
            User user = userService.selectByName(toName);
            if (user == null)
                return WendaUtil.getJSONString(1, "用户不存在");

            Message message = new Message();
            message.setContent(content);
            message.setCreatedDate(new Date());
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());

            messageService.addMessage(message);
            return WendaUtil.getJSONString(0);

        }else {
            return WendaUtil.getJSONString(999, "未登录");
        }

    }catch (Exception e){
        logger.error("发送私信失败"+ e.getMessage());
        return WendaUtil.getJSONString(1, "发信失败");
    }
    }

    @RequestMapping(path = {"/msg/list"},method = {RequestMethod.GET})
    public String getConversationList(Model model){
        if (hostHolder.getUser() == null){
            return "redirect:/reglogin";
        }

        int localUserId = hostHolder.getUser().getId();
        List<Message> conversationList = messageService.getConversationList(localUserId, 0, 10);
        List<ViewObject> conversations = new ArrayList<ViewObject>();
        for (Message message: conversationList){
            ViewObject vo = new ViewObject();
            vo.set("conversation",message);
            int targetId = message.getFromId() == localUserId ? message.getToId() : message.getFromId();
            vo.set("user",userService.getUser(targetId));
            vo.set("unread", messageService.getConversationUnreadCount(localUserId, message.getConversationId()));
            conversations.add(vo);
        }
        model.addAttribute("conversations",conversations);

        return "letter";
    }

    @RequestMapping(path = {"/msg/detail"},method = {RequestMethod.GET})
    public String getConversationDetail(Model model,
                                        @RequestParam("conversationId") String conversationId){
        try {

            List<Message> messageList = messageService.getConversationDetail(conversationId,0,10);
            List<ViewObject> messages = new ArrayList<ViewObject>();
            for (Message message: messageList){

                ViewObject vo = new ViewObject();
                vo.set("message",message);
                User user = userService.getUser(message.getFromId());
                if (user == null){
                    continue;
                }
                vo.set("headUrl",user.getHeadUrl());
                vo.set("userId",user.getId());
//                vo.set("user",userService.getUser(message.getFromId()));
                messages.add(vo);

        }
            model.addAttribute("messages",messages);

        }catch (Exception e){
            logger.error("获取详情失败"+ e.getMessage());
        }
        return "letterDetail";
    }

}
