package com.nd.share.demo.repository;

import com.nd.share.demo.domain.WordSearchLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * 词语搜索日志
 *
 * @author 郭晓斌(121017)
 * @version created on 20160602.
 */
@Repository
public interface WordSearchLogRepository extends MongoRepository<WordSearchLog,String> {
}
