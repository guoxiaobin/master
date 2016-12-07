package com.nd.share.demo.repository;

import com.nd.share.demo.domain.ThresholdPkg;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 增量包阀值配置表
 *
 * @author 郭晓斌(121017)
 * @version created on 20160613.
 */
@Repository
public interface ThresholdPkgRepository extends MongoRepository<ThresholdPkg,String> {

    List<ThresholdPkg> findByTypeId(String typeId);

}
