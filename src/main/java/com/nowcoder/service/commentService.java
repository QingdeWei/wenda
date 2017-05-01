package com.nowcoder.service;

import com.nowcoder.dao.CommentDAO;
import com.nowcoder.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by ZGH on 2017/5/1.
 */
@Service
public class commentService {
    @Autowired
    CommentDAO commentDAO;
    @Autowired
    SensitiveService sensitiveService;

    List<Comment> getCommentsByEntity(int entityId, int entityType ){
        return commentDAO.selectCommentByEntity(entityId,entityType);
    }

    int addComment(Comment comment){
        //别忘了这里还要对评论的内容进行过滤
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));

        commentDAO.addComment(comment);

        return commentDAO.addComment(comment)>0 ? comment.getId() : 0;
    }



}
