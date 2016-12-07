package com.nd.share.demo.repository.dao;

import com.nd.share.demo.domain.WordUpdList;
import com.nd.share.demo.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author jinmj
 * @date : 2016年6月13日 下午3:43:40
 */
@Repository
public class WordUpdListDao extends BaseDao<WordUpdList>{
	private static final Logger logger = LoggerFactory.getLogger(WordSearchLogDao.class);
	
	// 热词列表累计更新次数
    private long taskCount = 1;
    
	@Resource
    private MongoTemplate mongoTemplate;

	/**
     * 更新词条通知列表
     */
    public void saveWordUpdList(List<String> updlist){    	
    	
        for(String uuid : updlist){
            Query query = Query.query(Criteria.where("wordId").is(uuid).and("flag").is(0));
            query.limit(1);           
            List<WordUpdList> chkList = mongoTemplate.find(query,WordUpdList.class, getCollectionName(BaseDao.Collection.WORDUPDLIST.getName()));
            WordUpdList wordUpdList = this.setWordUpdList(chkList,uuid);
            mongoTemplate.save(wordUpdList, getCollectionName(BaseDao.Collection.WORDUPDLIST.getName()));
        }
        
        logger.info("{} 更新词条通知列表第 " + (taskCount++) + " 次批量更新", StringUtil.getDateStr(new Date()));
    }

    /**
     * 获取更新词条数量
     *
     * @param dicType 辞典类型
     * @param searchTime 检索范围
     * @return
     */
    public List<WordUpdList> getWordUpdLists(String dicType, Date searchTime){
        String wordUpdList = BaseDao.Collection.WORDUPDLIST.getName() + "_" + dicType;
        Query query = new Query();
        query.addCriteria(Criteria.where("flag").is(0).and("createTime").lte(searchTime));
        return mongoTemplate.find(query, WordUpdList.class, wordUpdList);
    }

    /**
     * 更新词条打包状态
     *
     * @param dicType 辞典类型
     * @param wordUpdList 更新对象
     * @return
     */
    public void updateWordStatus(String dicType, WordUpdList wordUpdList){
        String colWordUpdList = BaseDao.Collection.WORDUPDLIST.getName() + "_" + dicType;
        mongoTemplate.save(wordUpdList,colWordUpdList);
    }

    /**
     * 更新词语更新列表
     *
     * @param dicType
     * @param uuid
     */
    public void saveWord(String dicType, String uuid){
        String colWordUpdList = BaseDao.Collection.WORDUPDLIST.getName() + "_" + dicType;
        Query query = Query.query(Criteria.where("wordId").is(uuid).and("flag").is(0));
        List<WordUpdList> chkList = mongoTemplate.find(query,WordUpdList.class, colWordUpdList);
        WordUpdList wordUpdList = this.setWordUpdList(chkList,uuid);

        mongoTemplate.save(wordUpdList, colWordUpdList);
    }

    /**
     * 设置词语更新列表
     *
     * @param chkList
     * @param uuid
     * @return
     */
    private WordUpdList setWordUpdList(List<WordUpdList> chkList,String uuid){
        WordUpdList wordUpdList = new WordUpdList();
        if(chkList == null || chkList.size() == 0){
            wordUpdList.setWordId(uuid);
            wordUpdList.setFlag(0);
            wordUpdList.setCreateTime(new Date());
        } else {
            wordUpdList.setId(chkList.get(0).getId());
            wordUpdList.setWordId(chkList.get(0).getWordId());
            wordUpdList.setFlag(chkList.get(0).getFlag());
            wordUpdList.setCreateTime(chkList.get(0).getCreateTime());
            wordUpdList.setUpdateTime(new Date());
        }

        return wordUpdList;
    }
}
