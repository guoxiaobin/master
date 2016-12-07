package com.nd.share.demo.repository;

import com.nd.share.demo.domain.Feedback;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * 意见反馈
 *
 * @author 郭晓斌(121017)
 * @version created on 20160531.
 */
@Repository
public interface FeedbackRepository extends MongoRepository<Feedback, String> {
}
