package com.nd.share.demo.repository.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.nd.share.demo.domain.OfflineListInfo;
import com.nd.share.demo.domain.OfflinePkg;
import com.nd.share.demo.repository.OfflineListInfoRepository;

/**
 * 离线包列表Dao
 *
 * @author 黄梦飞(920225)
 * @version created on 2016年6月14日下午8:48:50.
 */
@Repository
public class OfflineListInfoDao extends BaseDao<OfflineListInfo> {

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private OfflineListInfoRepository offlineListInfoRepository;

    public List<OfflineListInfo> getOfflineListInfo(String typeId) {
        if (typeId == null) {
            return mongoTemplate.find(new Query(), OfflineListInfo.class, getCollectionName(BaseDao.Collection.OFFLINELISTINFO.getName()));
        }
        Query query = Query.query(Criteria.where("typeId").is(typeId));

        List<OfflineListInfo> pkglistinfos = mongoTemplate.find(query, OfflineListInfo.class, getCollectionName(BaseDao.Collection.OFFLINELISTINFO.getName()));

        return pkglistinfos;
    }

    /**
     * 获取离线包列表信息
     *
     * @param dicType 辞典类型
     * @return
     */
    public List<OfflineListInfo> getOfflineListInfos(String dicType) {
        String offlineListInfo = BaseDao.Collection.OFFLINELISTINFO.getName() + "_" + dicType;
        List<OfflineListInfo> pkglistinfos = mongoTemplate.find(new Query(), OfflineListInfo.class, offlineListInfo);
        return pkglistinfos;
    }
}
