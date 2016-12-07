package com.nd.share.demo.repository.dao;

import com.nd.share.demo.domain.ThresholdPkg;
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
public class ThresholdPkgDao extends BaseDao<ThresholdPkg> {

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 获取增量包阀值配置
     *
     * @param dicType 辞典类型
     * @param pkgType 离线包类型
     * @return
     */
    public List<ThresholdPkg> getThresholdPkg(String dicType, String pkgType){
        String thresholdPkg = Collection.THRESHOLDPKG.getName() + "_" + dicType;
        Query query = new Query();
        query.addCriteria(Criteria.where("typeId").is(pkgType));
        return mongoTemplate.find(query, ThresholdPkg.class, thresholdPkg);
    }

}
