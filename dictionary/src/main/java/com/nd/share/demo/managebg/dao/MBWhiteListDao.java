package com.nd.share.demo.managebg.dao;

import com.nd.share.demo.managebg.domain.MBUserInfo;
import com.nd.share.demo.managebg.domain.MBWhiteList;
import org.springframework.data.mongodb.config.MongoNamespaceHandler;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * 白名单
 *
 * @author 郭晓斌(121017)
 * @version created on 20160805.
 */
@Repository
public class MBWhiteListDao extends CommonDao {

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 白名单存储
     *
     * @param ip 白名单IP
     */
    public void saveWhiteList(String ip){
        MBWhiteList mbWhiteList = new MBWhiteList();
        mbWhiteList.setIp(ip);
        mongoTemplate.save(mbWhiteList);
    }

    /**
     * 是否存在白名单
     *
     * @param ip 白名单IP
     */
    public long countWhiteList(String ip){
        Query query = Query.query(Criteria.where("ip").is(ip));
        long count = mongoTemplate.count(query,MBWhiteList.class);
        return count;
    }

    /**
     * 删除白名单
     *
     * @param ip 白名单IP
     */
    public void deleteWhiteList(String ip){
        Query query = Query.query(Criteria.where("ip").is(ip));
        mongoTemplate.remove(query,MBWhiteList.class);
    }
}
