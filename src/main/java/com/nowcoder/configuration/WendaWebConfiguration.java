package com.nowcoder.configuration;

import com.nowcoder.interceptor.LoginRequiredInterceptor;
import com.nowcoder.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by ZGH on 2017/4/17.
 */
@Component
public class WendaWebConfiguration extends WebMvcConfigurerAdapter{

    //创建我们自己的拦截器
    @Autowired
    PassportInterceptor passportInterceptor;
    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);//这才将拦截器注册到真正的链路上
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/user/*");//跟上一个拦截器是有先后执行顺序的
        super.addInterceptors(registry);

    }
}
