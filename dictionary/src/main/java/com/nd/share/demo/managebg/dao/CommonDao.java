package com.nd.share.demo.managebg.dao;

import com.nd.share.demo.common.response.ErrorCode;
import com.nd.share.demo.common.response.RestfulException;
import com.nd.share.demo.domain.DictionaryTypeList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息
 *
 * @author 郭晓斌(121017)
 * @version created on 20160721.
 */
@Repository
public class CommonDao {
    private static final Logger logger = LoggerFactory.getLogger(CommonDao.class);

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 集合的基础表名的枚举定义，用于统一切换新的集合使用
     * 例如: 枚举一个集合 APPUPDINFO 中的两个属性  name 对应集合的名称，infoname 对应集合的中文信息
     */
    public enum Collection {
        LCBASEDATAS("lcBaseDatas", "LC基础数据关系表");

        private String name;
        private String infoName;

        private Collection(String name, String infoName) {
            this.name = name;
            this.infoName = infoName;
        }

        public String getName() {
            return this.name;
        }

        public String getInfoName() {
            return this.infoName;
        }
    }

    /**
     * 获取辞典类型
     *
     * @param typeId dictionaryTypeList
     * @return
     */
    public String getDictionaryType(int typeId){
        // 参数值不正
        if(typeId <= 0){
            logger.error("CommonDao.getDictionaryType:[DictionarySource={}] value is wrong.","ID="+typeId);
            throw new RestfulException(ErrorCode.INVALID_ARGUMENT,"[DictionarySource: ID=" + typeId + "] value is wrong.");
        }

        List<DictionaryTypeList> dictionaryTypeList = new ArrayList<>();
        Query query = Query.query(Criteria.where("typeId").is(typeId));
        dictionaryTypeList = mongoTemplate.find(query,DictionaryTypeList.class);

        String dicType = "";
        if(dictionaryTypeList != null && dictionaryTypeList.size() > 0){
            dicType = dictionaryTypeList.get(0).getType();
        }

        if(dictionaryTypeList == null || dictionaryTypeList.size() == 0){
            logger.warn("CommonDao.getDictionaryType:[DictionarySource={}] dictionry is not exist.","ID="+typeId);
            throw new RestfulException(ErrorCode.INVALID_ARGUMENT,"[DictionarySource: ID=" + typeId + "] dictionry is not exist.");
        }

        return dicType;
    }
}
