package com.nd.share.demo.repository;

import com.nd.share.demo.domain.DictionaryTypeList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 收藏字词
 *
 * @author 郭晓斌(121017)
 * @version created on 20160531.
 */
@Repository
public interface DictionaryTypeListRepository extends MongoRepository<DictionaryTypeList,String> {

    // 获取辞典类型
    List<DictionaryTypeList> findByTypeId(int typeId);

}
