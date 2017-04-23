package com.nowcoder.controller;

import com.nowcoder.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.sun.xml.internal.ws.api.ha.StickyFeature;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZGH on 2017/4/17.
 */
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserService userService;

    @RequestMapping(path = {"/reg/"}, method = {RequestMethod.POST})
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "next",required = false) String next,
                      HttpServletResponse response){
        try{
            Map<String, String> map = userService.register(username,password);

            if ( map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);//浏览器下发一个ticket
                if (StringUtils.isNotBlank(next)){
                    return "redirect:" + next;
                }
                return "redirect:/";
            }else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }

//            if(map.containsKey("msg")) {
//                model.addAttribute("msg", map.get("msg"));
//                return "login";
//            }
        }catch (Exception e){
            logger.error("密码错误",e.getMessage());
            model.addAttribute("msg", "服务器错误");
            return "login";
        }
//        return "redirect:/";
    }

//    @RequestMapping(path = {"/reglogin"}, method = {RequestMethod.GET})
//    public String reglogin(Model model,
//                               @RequestParam(value = "next",required = false) String next) {
//        model.addAttribute("next", next);
//        return "login";
//    }
@RequestMapping(path = {"/reglogin"}, method = {RequestMethod.GET})
public String regloginPage(Model model, @RequestParam(value = "next", defaultValue = "",required = false) String next) {
    model.addAttribute("next", next);
    return "login";
}

    @RequestMapping(path = {"/login/"}, method = {RequestMethod.POST})
    public String login(Model model, @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "next",required = false) String next,
                        HttpServletResponse response){
        try{
            Map<String, String> map = userService.login(username,password);

            if ( map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);//浏览器下发一个ticket
                if (StringUtils.isNotBlank(next)){
                    return "redirect:" + next;
                }
                return "redirect:/";
            }else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }

        }catch (Exception e){
            logger.error("密码错误",e.getMessage());
            return "login";
        }

    }


    /*登出*/
    @RequestMapping("/logout")
    String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }

}
