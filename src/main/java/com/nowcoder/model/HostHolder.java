package com.nowcoder.model;

import org.springframework.stereotype.Component;

import javax.jws.soap.SOAPBinding;

/**
 * Created by ZGH on 2017/4/17.
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser(){
        return users.get();
    }

    public void setUsers(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }
}
