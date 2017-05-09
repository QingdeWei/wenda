package com.nowcoder.dao;

import com.nowcoder.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by ZGH on 2017/5/1.
 */
@Mapper
public interface CommentDAO {

    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " content, user_id, entity_id, entity_type, created_date, status ";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;

    @Insert({ " insert into " + TABLE_NAME + "(" + INSERT_FIELDS +
            " ) values (#{content},#{userId}, #{entityId}, #{entityType}, #{createdDate}, #{status} )"})
    int addComment(Comment comment);

    @Select({" select " + SELECT_FIELDS +" from ", TABLE_NAME,
            " where entity_id = #{entityId} and entity_type = #{entityType} order by created_date desc "})
    List<Comment> selectCommentByEntity(@Param("entityId") int entityId,
                                        @Param("entityType") int entityType);

    @Select({"select count(id) from ", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType} "})
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Update({"update ", TABLE_NAME, " set status=#{status} where entity_id=#{entityId} and entity_type=#{entityType}"})
    void updateStatus(@Param("entityId") int entityId, @Param("entityType") int entityType, @Param("status") int status);

}
