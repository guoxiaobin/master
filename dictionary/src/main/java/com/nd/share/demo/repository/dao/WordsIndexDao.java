package com.nd.share.demo.repository.dao;

import com.nd.share.demo.domain.CollectWord;
import com.nd.share.demo.domain.WordsIndex;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 郭晓斌(121017)
 * @version created on 20160721.
 */
@Repository
public class WordsIndexDao extends BaseDao<WordsIndex> {

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 模糊检索字索引
     *
     * @param words
     * @return
     */
    public List<WordsIndex> searchWordsIndex(String words) {
        List<WordsIndex> wordsIndexList = new ArrayList<>();
        Query query = new Query();
        query.addCriteria(Criteria.where("title").regex(words));
        query.addCriteria(Criteria.where("type").in(1,2,3));
        wordsIndexList = mongoTemplate.find(query, WordsIndex.class, this.getCollectionName(Collection.WORDSINDEX.getName()));

        return wordsIndexList;
    }

    /**
     * 检索单个字索引
     *
     * @param collectWord 收藏词语实体
     * @return
     */
    public List<WordsIndex> searchWordsIndex(CollectWord collectWord) {
        List<WordsIndex> wordsIndexList = new ArrayList<>();
        Query query = new Query();
        query.addCriteria(Criteria.where("word").is(collectWord.getCharacter()));
        wordsIndexList = mongoTemplate.find(query, WordsIndex.class, this.getCollectionName(Collection.WORDSINDEX.getName()));

        return wordsIndexList;
    }
}
