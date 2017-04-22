package com.nowcoder.controller;

import com.nowcoder.model.User;
import com.nowcoder.service.WendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by ZGH on 2017/3/31.
 */
//@Controller
public class IndexController {
    @Autowired
    WendaService wendaService;
    @RequestMapping(path = {"/index"}, method = RequestMethod.GET)
    @ResponseBody
    public  String index(HttpSession httpSession){
        return wendaService.getMessage(2)+" Hello Nowcoder" + httpSession.getAttribute("msg");
    }
//
    @RequestMapping(path = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public  String profile(@PathVariable("userId")int userId,
                           @PathVariable("groupId")String groupId,
                           @RequestParam("type") int type,
                           @RequestParam(value = "value",defaultValue = "zzzz") String value){
        return String.format("Profile Page of %s/ %d , t= %d, k = %s", groupId,userId, type, value );
    }

    @RequestMapping(path = {"/vm"}, method = RequestMethod.GET)
    public  String template(Model model){
        model.addAttribute("value1","vvv1");
        List<String> colors = Arrays.asList(new String[] {"RED", "GREEN", "BLUE"});
        //model传递参数到模板文件
        model.addAttribute("colors",colors);

        Map<String, String> map = new HashMap<String, String>();
        for (int i=0;i<4; ++i){
            map.put(String.valueOf(i), String.valueOf(i*i));
        }
        model.addAttribute("map",map);
        model.addAttribute("user",new User("LEE"));

        return "home";
    }
//
    @RequestMapping(path = {"/request"}, method = RequestMethod.GET)
    @ResponseBody
    public String request(Model model, HttpServletResponse response,
                           HttpServletRequest request,
                           HttpSession session,
                          @CookieValue("JSESSIONID") String sessionId){
        StringBuilder sb = new StringBuilder();
        sb.append("COOKIEVALUE: " + sessionId);

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            sb.append(name + ":" + request.getHeader(name) + "<br>");
        }
        if (request.getCookies() != null){
            for (Cookie cookie: request.getCookies()){
                sb.append("Cookie: " + cookie.getName() + " value: " + cookie.getValue());
            }
        }
        sb.append(request.getMethod() + "<br>");
        sb.append(request.getQueryString() + "<br>");
        sb.append(request.getPathInfo() + "<br>");
        sb.append(request.getRequestURI() + "<br>");
        sb.append(request.getHeader("Connection") +"<br>");

        response.addHeader("nowcoderID", "hello");
        response.addCookie(new Cookie("username","nowcoder"));
//        response.getOutputStream().write();

        return sb.toString();
    }
//
    @RequestMapping(path = {"/redirect/{code}"}, method = RequestMethod.GET)
    public String redirect(@PathVariable("code") int code ,
                           HttpSession httpSession){
        httpSession.setAttribute("msg","jump from redirect");
        return "redirect:/";
    }

//    @RequestMapping(path = {"/redirect/{code}"}, method = RequestMethod.GET)
//    public RedirectView redirect(@PathVariable("code") int code ,
//                                 HttpSession httpSession){
//        httpSession.setAttribute("msg","jump from redirect");
//        RedirectView red = new RedirectView("/",true);
//        if(code == 301){
//            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
//        }
//        return red;
//    }
//
//    @RequestMapping(path = {"/admin"}, method = RequestMethod.GET)
//    @ResponseBody
//    public String admin(@RequestParam("key") String key){
//        if("admin".equals(key)){
//            return "Hello admin";
//        }
//        throw new IllegalArgumentException("参数不对");
//    }


//
    //异常的统一处理
    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e){

        return "error" + e.getMessage();
    }
}
