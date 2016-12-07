package com.nd.share.demo.managebg.dao;

import com.nd.share.demo.managebg.domain.MBUserInfo;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户信息
 *
 * @author 郭晓斌(121017)
 * @version created on 20160721.
 */
@Repository
public class MBUserInfoDao extends CommonDao {

    @Resource
    private MongoTemplate mongoTemplate;


    public List<MBUserInfo> searchUserInfo(String username, String pwd){
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username).and("password").is(pwd));
        List<MBUserInfo> mbUserInfoList = mongoTemplate.find(query, MBUserInfo.class);
        return mbUserInfoList;
    }
}
