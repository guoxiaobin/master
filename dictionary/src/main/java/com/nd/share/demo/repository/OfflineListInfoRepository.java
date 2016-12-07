package com.nd.share.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nd.share.demo.domain.OfflineListInfo;
/**
 * 
 * 
 *
 * @author 黄梦飞(920225)
 * @version created on 2016年6月14日下午8:44:59.
 */
@Repository
public interface OfflineListInfoRepository extends MongoRepository<OfflineListInfo,String> {

}
