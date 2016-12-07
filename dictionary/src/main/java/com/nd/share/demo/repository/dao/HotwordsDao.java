package com.nd.share.demo.repository.dao;

import com.nd.share.demo.domain.Hotwords;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 郭晓斌(121017)
 * @version created on 20160601.
 */
@Repository
public class HotwordsDao extends BaseDao<Hotwords> {

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 获取热词列表
     *
     * @param fieldName 排序字段
     * @param sortType  排序类型（1：升序；2：降序）
     * @param limit     检索数量
     * @return
     */
    public List<Hotwords> getHotwords(String fieldName, int sortType, int limit) {
        Query query = new Query();
        super.getSortAndLimitQuery(query,fieldName, sortType, limit);
        return mongoTemplate.find(query, Hotwords.class, getCollectionName(BaseDao.Collection.HOTWORDS.getName()));
    }
    
    public void saveHotword(Hotwords hotword){
        mongoTemplate.save(hotword, getCollectionName(BaseDao.Collection.HOTWORDS.getName()));
    }
}
