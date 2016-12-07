package com.nd.share.demo.repository.dao;

import com.nd.share.demo.domain.DictionaryTypeList;
import com.nd.share.demo.domain.Hotwords;
import com.nd.share.demo.domain.WordSearchLog;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 郭晓斌(121017)
 * @version created on 20160602.
 */
@Repository
public class WordSearchLogDao extends BaseDao<WordSearchLog> {
    private static final Logger logger = LoggerFactory.getLogger(WordSearchLogDao.class);

    @Resource
    private MongoTemplate mongoTemplate;

    @Value("${hotword.limit}")
    private int hotwordCount;

    /**
     * 提取热词并保存
     */
    public void saveHotwords() {

        // 获取辞典类型
        List<DictionaryTypeList> dictionaryTypeLists = super.getDictionaryTypes();

        for (DictionaryTypeList d : dictionaryTypeLists) {
            List<Hotwords> hotwordses = new ArrayList<>();
            String wordSearchLog = Collection.WORDSEARCHLOG.getName() + "_" + d.getType();
            String hotWords = Collection.HOTWORDS.getName() + "_" + d.getType();

            // 获取搜索日志词条数据
            Aggregation groupCount = Aggregation.newAggregation(
                    Aggregation.group("identifier", "character").count().as("count")
            );
            AggregationResults<Map> aggregateResult = mongoTemplate.aggregate(groupCount, wordSearchLog, Map.class);
            Object resultMap = aggregateResult.getMappedResults();
            List<Map<String, String>> result = (List<Map<String, String>>) resultMap;

            for (Map<String, String> stringStringMap : result) {
                Hotwords hotwords = new Hotwords();
                hotwords.setIdentifier(MapUtils.getString(stringStringMap, "identifier"));
                hotwords.setCharacter(MapUtils.getString(stringStringMap, "character"));
                hotwords.setCount(MapUtils.getInteger(stringStringMap, "count"));
                hotwordses.add(hotwords);
            }

            // 排序(降序)
            Collections.sort(hotwordses, new Comparator<Hotwords>() {
                @Override
                public int compare(Hotwords h1, Hotwords h2) {
                    if(h1.getCount() > h2.getCount()){
                        return -1;
                    } else if(h1.getCount() < h2.getCount()){
                        return 1;
                    }else{
                        return 0;
                    }
                }
            });

            // 删除热词集合数据
            if(mongoTemplate.collectionExists(hotWords)){
                mongoTemplate.dropCollection(hotWords);
            }

            int count = 1;
            // 更新热词列表
            for (Hotwords h : hotwordses) {
                if(count > hotwordCount){
                    break;
                }
                // 字词内容
                String title = h.getCharacter();
                if(h.getCharacter() == null || "".equals(h.getCharacter())){
                    continue;
                }
                if(title.contains("<")){
                    title = title.substring(0,title.indexOf("<"));
                }
                // 设定文字
                h.setCharacter(title);
                h.setCreateTime(new Date());
                mongoTemplate.save(h, hotWords);
                count++;
            }
            logger.info("WordSearchLogDao.saveHotwords:[Dictionary {}] hotwords update successfully.", d.getType());
        }
    }

    /**
     * 保存搜索词语
     *
     * @param identifier 词语id
     * @param character  词语
     */
	public void saveSearchWord(String identifier, String character) {
		WordSearchLog wordSearchLog = new WordSearchLog();
		wordSearchLog.setIdentifier(identifier);
		wordSearchLog.setCharacter(character);
		wordSearchLog.setCreateTime(new Date());

		// 搜索词语入库
		mongoTemplate.save(wordSearchLog, getCollectionName(BaseDao.Collection.WORDSEARCHLOG.getName()));
	}

}
