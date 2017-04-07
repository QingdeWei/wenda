package com.nowcoder.dao;

import com.nowcoder.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * DAO层是用来访问数据库的
 */
@Mapper  //跟MyBatis关联的DAO
public interface UserDAO {
    String TABLE_NAME = "user";
    String INSERT_FIELDS = "name, password, salt, head_url";
    String SELECT_FIELDS = "id, name, password, salt, head_url";

    @Insert({"insert into", TABLE_NAME, "(", INSERT_FIELDS, ") Values(#{name}. #{password},#{salt}, #{headUrl})"})
     int addUser(User user);

     int selectById(int id);

     void updatePassword(User user);

     void deleteById(int id);


}
