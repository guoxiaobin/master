package com.nd.share.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nd.share.demo.domain.AppUpdInfo;

/**
 * 热修复检测
 * 
 * @author jinmj
 * @date : 2016年6月14日 下午4:15:21
 */
@Repository
public interface AppUpdInfoRepository extends MongoRepository<AppUpdInfo, String> {
}
