package com.nd.share.demo.service;

import com.nd.share.demo.domain.Questions;

import java.util.List;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
public interface QuestionsService {
    List<Questions> findQuestionList(String questionType,String category);
}
