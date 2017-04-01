package com.nowcoder.service;

import org.springframework.stereotype.Service;

/**
 * Created by ZGH on 2017/4/1.
 */
@Service
public class WendaService {
    public String getMessage(int userId){
        return "Hello Message: " + String.valueOf(userId);
    }
}
