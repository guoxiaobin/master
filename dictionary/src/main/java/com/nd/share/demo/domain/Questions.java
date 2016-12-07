package com.nd.share.demo.domain;

import com.nd.share.demo.service.Entity.ModalFeedback;
import com.nd.share.demo.service.Entity.SimpleChoice;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 选择题表
 *
 * @author 郭晓斌(121017)
 * @version created on 20160823.
 */
@Document
public class Questions {
    @Id
    private String id;
    private String questionUrl;
    private String questionType;
    private List<String> corrects;
    private String prompt;
    private List<SimpleChoice> simpleChoiceList;
    private List<ModalFeedback> modalFeedbacks;
    private String category;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionUrl() {
        return questionUrl;
    }

    public void setQuestionUrl(String questionUrl) {
        this.questionUrl = questionUrl;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public List<String> getCorrects() {
        return corrects;
    }

    public void setCorrects(List<String> corrects) {
        this.corrects = corrects;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public List<SimpleChoice> getSimpleChoiceList() {
        return simpleChoiceList;
    }

    public void setSimpleChoiceList(List<SimpleChoice> simpleChoiceList) {
        this.simpleChoiceList = simpleChoiceList;
    }

    public List<ModalFeedback> getModalFeedbacks() {
        return modalFeedbacks;
    }

    public void setModalFeedbacks(List<ModalFeedback> modalFeedbacks) {
        this.modalFeedbacks = modalFeedbacks;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
