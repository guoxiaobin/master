package com.nd.share.demo.repository.dao;

import com.nd.share.demo.domain.Questions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 郭晓斌(121017)
 * @version created on 20160823.
 */
@Repository
public class QuestionsDao extends BaseDao<Questions> {
    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 保存单选题数据
     *
     * @param questions
     * @param dicType
     */
    public void saveQuestions(Questions questions, int dicType){
        String dicTypeName = getDictionaryType(dicType).getType();
        mongoTemplate.save(questions, Collection.QUESTIONS.getName() + "_" + dicTypeName);
    }

    /**
     * 删除集合
     *
     * @param dicType
     */
    public void deleteQuestions(int dicType){
        String tblQuestions = Collection.QUESTIONS.getName() + "_" + getDictionaryType(dicType).getType();
        if(mongoTemplate.collectionExists(tblQuestions)){
            mongoTemplate.dropCollection(tblQuestions);
        }
    }

    /**
     * 获取单选题
     *
     * @param questionType 题型
     * @return
     */
    public List<Questions> searchQuestions(String questionType,String category){
        List<Questions> questionsList = new ArrayList<>();
        Query query = new Query();
        query.addCriteria(Criteria.where("category").is(category));
        if(questionType != null && !"".equals(questionType)){
            query.addCriteria(Criteria.where("questionType").is(questionType));
        }
        questionsList = mongoTemplate.find(query,Questions.class,getCollectionName(Collection.QUESTIONS.getName()));
        return questionsList;
    }
}
