package com.nd.share.demo.repository.dao;

import com.mongodb.WriteResult;
import com.nd.share.demo.domain.OfflinePkg;
import com.nd.share.demo.domain.WordDetail;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 郭晓斌(121017)
 * @version created on 20160804.
 */
@Repository
public class WordDetailDao  extends BaseDao<OfflinePkg> {

    @Resource
    private BaseDao baseDao;
    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 获取词语详情
     *
     * @param identifier 词语uuid
     * @return
     */
    public List<WordDetail> getWordDetailList(String identifier){
        List<WordDetail> wordDetailList = new ArrayList<>();
        Query query = Query.query(Criteria.where("identifier").is(identifier));
        wordDetailList = mongoTemplate.find(query,WordDetail.class,baseDao.getCollectionName(Collection.WORDDETAIL.getName()));

        return wordDetailList;
    }

    /**
     * 获取所有详情
     *
     * @param dicType
     * @return
     */
    public List<WordDetail> getAllWordDetails(int dicType){
        List<WordDetail> wordDetailList = new ArrayList<>();
        String dicTypeName =  baseDao.getDictionaryType(dicType).getType();
        wordDetailList = mongoTemplate.findAll(WordDetail.class, Collection.WORDDETAIL.getName() + "_" + dicTypeName);
        // 更新词语详情
        return wordDetailList;
    }
}
