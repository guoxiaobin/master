package com.nd.share.demo.repository.dao;

import com.mongodb.WriteResult;
import com.nd.share.demo.common.response.ErrorCode;
import com.nd.share.demo.common.response.RestfulException;
import com.nd.share.demo.constants.Messages;
import com.nd.share.demo.domain.CollectWord;
import com.nd.share.demo.repository.CollectWordRepository;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

/**
 * @author 郭晓斌(121017)
 * @version created on 20160601.
 */
@Repository
public class CollectWordDao extends BaseDao<CollectWord> {

    @Resource
    private MongoTemplate mongoTemplate;
    
    @Resource
    private CollectWordRepository collectWordRepository;

    /**
     * 获取用户收藏字词
     *
     * @param timeStamp
     * @return
     */
    public List<CollectWord> getCollectWorks(Date timeStamp,String userId,String entrance){
        Query query = new Query();
        query.addCriteria(Criteria.where("createTime").gt(timeStamp));
        query.addCriteria(Criteria.where("userId").is(userId));
//        query.addCriteria(Criteria.where("entrance").is(entrance));
        getSortAndLimitQuery(query, "spellindex", 1, 0);
        return mongoTemplate.find(query,CollectWord.class,getCollectionName(BaseDao.Collection.COLLECTWORD.getName()));
    }
    /**
     * 收藏词语
     * @param collectWord
     */
    public void updateCollectWorks(CollectWord collectWord){
        try {
            mongoTemplate.save(collectWord, getCollectionName(BaseDao.Collection.COLLECTWORD.getName()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestfulException(ErrorCode.SERVICE_ERROR_SYSTEM, Messages.INTERNAL_SERVER_ERROR, String.format("CollectWordServiceImpl, msg:%s", e.getMessage()));
        }
    }
    
    public long findCollectWordCount(CollectWord collectWord){
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(collectWord.getUserId()));
        query.addCriteria(Criteria.where("identifier").is(collectWord.getIdentifier()));
        long res = mongoTemplate.count(query, getCollectionName(BaseDao.Collection.COLLECTWORD.getName()));
        return res;
    }

    /**
     * 取消收藏
     *
     * @param collectWord
     * @return
     */
    public int cancelCollection(CollectWord collectWord){
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(collectWord.getUserId()));
        query.addCriteria(Criteria.where("identifier").is(collectWord.getIdentifier()));

        // 查询收藏字词
        long count = mongoTemplate.count(query,CollectWord.class,getCollectionName(BaseDao.Collection.COLLECTWORD.getName()));
        if (count == 0){
            return 2;
        }

        // 删除收藏字词
        WriteResult writeResult = mongoTemplate.remove(query, getCollectionName(BaseDao.Collection.COLLECTWORD.getName()));

        return (writeResult.getError() == null) ? 0 : 1;
    }

}
