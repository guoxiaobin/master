package com.nd.share.demo.repository;

import com.nd.share.demo.domain.Feedback;
import com.nd.share.demo.domain.Hotwords;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * 热词列表
 * @author 郭晓斌(121017)
 * @version created on 20160531.
 */
@Repository
public interface HotWordsRepository extends MongoRepository<Hotwords, String> {

}
