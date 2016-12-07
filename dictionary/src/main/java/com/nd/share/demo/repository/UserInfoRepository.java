package com.nd.share.demo.repository;

import com.nd.share.demo.domain.UserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * 用户信息
 *
 * @author 郭晓斌(121017)
 * @version created on 20160530.
 */
@Repository
public interface UserInfoRepository extends MongoRepository<UserInfo, String> {
}
