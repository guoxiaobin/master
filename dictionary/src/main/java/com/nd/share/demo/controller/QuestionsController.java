package com.nd.share.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.nd.share.demo.domain.Questions;
import com.nd.share.demo.service.QuestionsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nd.share.demo.constants.Constants;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author 郭晓斌(121017)
 * @version created on 20160823.
 */
@RestController
@RequestMapping(value = Constants.VERSION_CURRENT + "/question")
public class QuestionsController {

    @Resource
    private QuestionsService questionsService;

    /**
     * 获取练习题
     *
     * @param type 题型
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    private JSONObject getSingleQuestions(@RequestParam(value = "type", required = false) String type,@RequestParam(value = "category", required = false) String category) {
        JSONObject jsonObject = new JSONObject();
        List<Questions> questionsList = questionsService.findQuestionList(type,category);
        List<Questions> list = new ArrayList<>();
        if(questionsList != null && questionsList.size() > 0){
            Collections.shuffle(questionsList);
            for(int i = 0; i < 10; i++){
                list.add(questionsList.get(i));
            }
        }
        jsonObject.put("items",list);
        return jsonObject;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    private JSONObject getAllQuestions(@RequestParam(value = "type", required = false) String type,@RequestParam(value = "category", required = false) String category) {
        JSONObject jsonObject = new JSONObject();
        List<Questions> questionsList = questionsService.findQuestionList(type,category);
        jsonObject.put("items",questionsList);
        return jsonObject;
    }

}
