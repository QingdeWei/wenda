package com.nowcoder.service;

import com.nowcoder.dao.QuestionDAO;
import com.nowcoder.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ZGH on 2017/4/10.
 */
@Service
public class    QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    public int addQuestion(Question question){
        //questionDAO.addQuestion(question);
        //敏感词过滤
        return questionDAO.addQuestion(question)>0 ? question.getId():0;

    }

    public List<Question> getLatestQuestions(int userId, int offset, int limit){
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }
}
