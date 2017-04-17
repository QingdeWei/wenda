package com.nowcoder.dao;

import com.nowcoder.model.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * Created by ZGH on 2017/4/17.
 */
@Mapper
public interface LoginTicketDAO {

    String TABLE_NAME = " login_ticket ";
    String INSERT_FIELDS = " user_id, ticket, expired, status ";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;

    @Insert({" insert into" ,TABLE_NAME , "(", INSERT_FIELDS,
            ") values ( ", "#{userId}, #{ticket}, #{expired}, #{status})",})
    int addTicket(LoginTicket loginTicket);

    //登录的时候，看看用户在不在
    @Select({" select ", SELECT_FIELDS, " from ",TABLE_NAME, " where ticket = #{ticket}"})
    LoginTicket selectByTicket( String ticket);

    //如果登出，需要将ticket过期掉
    @Update({" update ", TABLE_NAME, " set status = #{status} where ticket = #{ticket}"})
    void updateStatus(@Param("ticket") String ticket,
                      @Param("status") int status);

}
