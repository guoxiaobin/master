package com.nd.share.demo.repository.dao;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.nd.share.demo.domain.AppUpdInfo;
import com.nd.share.demo.repository.AppUpdInfoRepository;
import com.nd.share.demo.util.StringUtil;

/**
 * 热修复检测
 * 
 * @author jinmj
 * @date : 2016年6月13日 下午3:43:40
 */
@Repository
public class AppUpdInfoDao extends BaseDao<AppUpdInfo>{
	private static final Logger logger = LoggerFactory.getLogger(AppUpdInfoDao.class);
	
	// 热修复检测更新次数
    private long taskCount = 1;
    
	@Resource
    private MongoTemplate mongoTemplate;
	
	@Resource
	private AppUpdInfoRepository appUpdInfoRepository;
	
	/**
     * 热修复检测
     */
    public AppUpdInfo getAppVersionInfo(Integer version, Integer type){    	
//       logger.info("{} 热修复检测第 " + (taskCount++) + " 次更新", StringUtil.getDateStr(new Date()));	
        Query query = null;
        if(type == 1){
            query = Query.query(Criteria.where("type").is(type));
        }else{
            query = Query.query(Criteria.where("type").is(type).and("appVersion").is(version));
        }
       
       query.with(new Sort(new Order(Direction.DESC, "pkgVersoin")));
       query.limit(1);       
       try {
    	   return mongoTemplate.find(query,AppUpdInfo.class,getCollectionName(BaseDao.Collection.APPUPDINFO.getName())).get(0);
       } catch (Exception e) {
    	   return new AppUpdInfo();
       }
       
    }
    
    public AppUpdInfo updateAppverInfo(AppUpdInfo auireq , String dicType){
        String collection = getCollectionName(BaseDao.Collection.APPUPDINFO.getName());
        mongoTemplate.upsert(new Query(Criteria.where("type").is(auireq.getType())), new Update().set("size", auireq.getSize())
                        .set("description", auireq.getDescription()).set("url", auireq.getUrl())
                        .set("appVersion", auireq.getAppVersion()).set("pkgVersion", auireq.getPkgVersion())
                        .set("createTime", DateFormatUtils.format(new Date(), "yyyy-MM-dd'T'HH:mm:ss")),
                AppUpdInfo.class,getCollectionName(BaseDao.Collection.APPUPDINFO.getName()));
        AppUpdInfo aui = mongoTemplate.find(new Query(Criteria.where("type").is(auireq.getType())), AppUpdInfo.class, collection).get(0);
        return aui;
    }

}
