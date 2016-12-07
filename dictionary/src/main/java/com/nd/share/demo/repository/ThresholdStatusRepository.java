package com.nd.share.demo.repository;

import com.nd.share.demo.domain.ThresholdStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 增量阀值状态表
 *
 * @author 郭晓斌(121017)
 * @version created on 20160613.
 */
@Repository
public interface ThresholdStatusRepository extends MongoRepository<ThresholdStatus,String> {

    List<ThresholdStatus> findByTypeId(String typeId);

}
