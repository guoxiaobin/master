package com.nd.share.demo.repository.dao;

import com.nd.share.demo.domain.OfflinePkg;
import com.nd.share.demo.repository.OfflinePkgRepository;
import org.apache.commons.collections4.map.LinkedMap;import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 郭晓斌(121017)
 * @version created on 20160601.
 */
@Repository
public class OfflinePkgDao extends BaseDao<OfflinePkg> {

    @Resource
    private MongoTemplate mongoTemplate;
    
    @Resource
    private OfflinePkgRepository offlinePkgRepository;

    /**
     * 获取离线包最新版本
     *
     * @param fieldName 排序字段
     * @param sortType  排序类型（1：升序；2：降序）
     * @param limit     检索数量
     * @return
     */
    public List<OfflinePkg> getOfflimePkg(String fieldName, int sortType,int limit) {
        Query query = new Query();
        Map<String,Object> where = new HashMap<>();
        where.put("updateFlag","1");

        super.getWhereIsQuery(query,where);
        super.getSortAndLimitQuery(query,fieldName, sortType, limit);
        return mongoTemplate.find(query, OfflinePkg.class, getCollectionName(BaseDao.Collection.OFFLINEPKG.getName()));
    }
    /**
     * 临时添加,后期删除%HMF-Delete
     * @param pkgList
     * @return
     */
    public void insertOfflinePkg(OfflinePkg pkg){
    	mongoTemplate.save(pkg, getCollectionName(Collection.OFFLINEPKG.getName()));
    }
    /**
     * 获取需要下载的更新包
     * @param typeId 类型
     * @param version 版本号
     * @param flag 传入0或者null则为全部获取,传入其他值则根据flag获取
     * @return
     */
    public List<OfflinePkg> getOfflinePkgsAdding(String typeId,int version ,Integer flag){
    	Query query = Query.query(Criteria.where("typeId").is(typeId).and("version").gt(version));
    	if(flag == null || flag == 0){
    		query = getSortAndLimitQuery(query, "version", 1, 0);
    	}else{
    		if(flag > 2){
    			return null;
    		}
    		query.addCriteria(Criteria.where("flag").is(flag.toString()));
    		query = getSortAndLimitQuery(query, "version", 2, 0);
    	}
    	List<OfflinePkg> pkgs = mongoTemplate.find(query, OfflinePkg.class , getCollectionName(BaseDao.Collection.OFFLINEPKG.getName()));
    	
    	return pkgs;
    }

    /**
     * 获取最新增量包信息
     *
     * @param dicType 辞典类型
     * @param fieldName 排序字段
     * @param typeId 包类型
     * @return 增量包信息
     */
    public List<OfflinePkg> getLastVerPkgInfo(String dicType, String fieldName,String typeId) {
        Query query = new Query();
        Map<String,Object> where = new HashMap<>();
        where.put("typeId",typeId);

        super.getWhereIsQuery(query,where);
        super.getSortAndLimitQuery(query, fieldName, 2, 1);
        return mongoTemplate.find(query, OfflinePkg.class ,BaseDao.Collection.OFFLINEPKG.getName() + "_" + dicType);
    }

    /**
     * 保存离线包信息
     *
     * @param dicType 辞典类型
     * @param offlinePkg 保存对象
     */
    public void save(String dicType, OfflinePkg offlinePkg){
        String colOfflineOkg = Collection.OFFLINEPKG.getName() + "_" + dicType;
        mongoTemplate.save(offlinePkg, colOfflineOkg);
    }

}
