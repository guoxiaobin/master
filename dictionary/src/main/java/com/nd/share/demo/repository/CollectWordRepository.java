package com.nd.share.demo.repository;

import com.nd.share.demo.domain.CollectWord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * 收藏字词
 *
 * @author 郭晓斌(121017)
 * @version created on 20160531.
 */
@Repository
public interface CollectWordRepository extends MongoRepository<CollectWord, String> {

    int countByIdentifier(String identifier);
}
