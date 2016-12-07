package com.nd.share.demo.service.impl;

import com.nd.share.demo.domain.Questions;
import com.nd.share.demo.repository.dao.QuestionsDao;
import com.nd.share.demo.service.QuestionsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
@Service
public class QuestionsServiceImpl implements QuestionsService {

    @Resource
    private QuestionsDao questionsDao;

    /**
     * 获取单选题
     *
     * @return
     */
    public List<Questions> findQuestionList(String questionType,String category){
        List<Questions> questionsList = new ArrayList<>();
        questionsList = questionsDao.searchQuestions(questionType,category);
        return questionsList;
    }
}
