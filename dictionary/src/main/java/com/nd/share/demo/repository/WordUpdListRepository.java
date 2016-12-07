package com.nd.share.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nd.share.demo.domain.WordUpdList;

import java.util.List;

/**
 * 词条更新列表
 *
 * @author 郭晓斌(121017)
 * @version created on 20160613.
 */
@Repository
public interface WordUpdListRepository extends MongoRepository<WordUpdList, String> {

    // 获取更新词条数
    int countByFlag(int flag);
    // 获取更新词条信息
    List<WordUpdList> findByFlag(int flag);
}
