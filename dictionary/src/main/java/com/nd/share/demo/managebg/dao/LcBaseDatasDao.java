package com.nd.share.demo.managebg.dao;

import com.nd.share.demo.domain.LcBaseDatas;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * lc基础数据关系
 *
 * @author 郭晓斌(121017)
 * @version created on 20160817.
 */
@Repository
public class LcBaseDatasDao extends CommonDao {

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 获取资源数据
     *
     * @param dicType
     * @param id
     * @return
     */
    public LcBaseDatas searchLcBaseDatas(int dicType, String id){
        List<LcBaseDatas> lcBaseDatasList = new ArrayList<>();
        Query query = Query.query(Criteria.where("id").is(id));
        lcBaseDatasList = mongoTemplate.find(query, LcBaseDatas.class, Collection.LCBASEDATAS.getName() + "_" + getDictionaryType(dicType));
        return lcBaseDatasList.get(0);
    }

    /**
     * 保存
     *
     * @param lcBaseDatas
     * @param dicType
     */
    public void save(LcBaseDatas lcBaseDatas, int dicType){
        mongoTemplate.save(lcBaseDatas, Collection.LCBASEDATAS.getName() + "_" + getDictionaryType(dicType));
    }

    /**
     * 检索资源
     *
     * @param dicType
     * @param type
     * @param key
     * @return
     */
    public List<LcBaseDatas> searchByType(int dicType,String type, String key) {
        List<LcBaseDatas> lcBaseDatasList = new ArrayList<>();
        Query query = Query.query(Criteria.where("type").is(type));
        query.addCriteria(Criteria.where("key").is(key));
        lcBaseDatasList = mongoTemplate.find(query, LcBaseDatas.class, Collection.LCBASEDATAS.getName() + "_" + getDictionaryType(dicType));
        return lcBaseDatasList;
    }
}
