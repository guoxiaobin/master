package com.nd.share.demo.repository.dao;

import com.nd.share.demo.domain.ThresholdPkg;
import com.nd.share.demo.domain.ThresholdStatus;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 郭晓斌(121017)
 * @version created on 20160623.
 */
@Repository
public class ThresholdStatusDao extends BaseDao<ThresholdStatus> {

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 获取增量阀值状态
     *
     * @param dicType 辞典类型
     * @param pkgType 离线包类型
     * @return
     */
    public List<ThresholdStatus> getThresholdStatus(String dicType, String pkgType){
        String thresholdStatus = Collection.THRESHOLDSTATUS.getName() + "_" + dicType;
        Query query = new Query();
        query.addCriteria(Criteria.where("typeId").is(pkgType));
        return mongoTemplate.find(query, ThresholdStatus.class, thresholdStatus);
    }

    /**
     * 更新词条
     *
     * @param dicType 辞典类型
     * @param thresholdStatus 更新对象
     * @return
     */
    public void save(String dicType, ThresholdStatus thresholdStatus){
        String colThresholdStatus = Collection.THRESHOLDSTATUS.getName() + "_" + dicType;
        mongoTemplate.save(thresholdStatus, colThresholdStatus);
    }
}
