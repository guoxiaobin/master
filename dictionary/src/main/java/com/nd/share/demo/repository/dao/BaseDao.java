package com.nd.share.demo.repository.dao;

import com.nd.share.demo.common.response.ErrorCode;
import com.nd.share.demo.common.response.RestfulException;
import com.nd.share.demo.domain.DictionaryTypeList;
import com.nd.share.demo.repository.DictionaryTypeListRepository;
import com.nd.share.demo.service.context.IDictionarySource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 底层dao
 *
 * @author 郭晓斌(121017)
 * @version created on 20160601.
 */
@Repository
public class BaseDao<T> {
    private static final Logger logger = LoggerFactory.getLogger(BaseDao.class);

    /**
     * 集合的基础表名的枚举定义，用于统一切换新的集合使用
     * 例如: 枚举一个集合 APPUPDINFO 中的两个属性  name 对应集合的名称，infoname 对应集合的中文信息
     */
    public enum Collection {
    	APPUPDINFO("appUpdInfo", "热修复更新"),
    	COLLECTWORD("collectWord", "收藏词语"),
    	DICTIONARYTYPELIST("dictionaryTypeList", "辞典类型列表"),
    	FEEDBACK("feedback", "意见回馈"),
    	HOTWORDS("hotwords", "热词列表"),
    	OFFLINELISTINFO("offlineListInfo", "离线包列表信息"),
    	OFFLINEPKG("offlinePkg", "离线包明细信息"),
    	THRESHOLDPKG("thresholdPkg", "增量包阀值配置表"),
    	THRESHOLDSTATUS("thresholdStatus", "增量阀值状态表"),
    	WORDSEARCHLOG("wordSearchLog", "词语搜索日志"),
    	WORDUPDLIST("wordUpdList", "词条更新列表"),
        LCBASEDATAS("lcBaseDatas", "LC基础数据关系表"),
        WORDSINDEX("wordsIndex", "字索引表"),
        WORDDETAIL("wordDetail", "词语详情表"),
        QUESTIONS("questions", "题目缓存表");

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

    @Resource
    private DictionaryTypeListRepository dictionaryTypeListRepository;

    @Resource
	private IDictionarySource dictionarySourceContext;

    /**
     * 获取集合名词
     *
     * @param
     * @return
     */
    public String getCollectionName(String BaseCollectionName){
    	DictionaryTypeList dictionaryTypeList = getDictionaryType(dictionarySourceContext.getId());
		return BaseCollectionName + "_" + dictionaryTypeList.getType();
    }

    /**
     * 获取辞典类型
     *
     * @param typeId dictionaryTypeList
     * @return
     */
    public DictionaryTypeList getDictionaryType(int typeId){
        // 参数值不正
        if(typeId <= 0){
            logger.error("BaseDao.getDictionaryType:[DictionarySource={}] value is wrong.","ID="+typeId);
            throw new RestfulException(ErrorCode.INVALID_ARGUMENT,"[DictionarySource: ID=" + typeId + "] value is wrong.");
        }

        List<DictionaryTypeList> dictionaryTypeList = new ArrayList<>();
        dictionaryTypeList = dictionaryTypeListRepository.findByTypeId(typeId);

        if(dictionaryTypeList.isEmpty() || dictionaryTypeList.size() == 0){
            logger.warn("BaseDao.getDictionaryType:[DictionarySource={}] dictionry is not exist.","ID="+typeId);
            throw new RestfulException(ErrorCode.INVALID_ARGUMENT,"[DictionarySource: ID=" + typeId + "] dictionry is not exist.");
        }

        return dictionaryTypeList.get(0);
    }

    /**
     * 获取所有辞典列表
     *
     * @return
     */
    public List<DictionaryTypeList> getDictionaryTypes(){
        List<DictionaryTypeList> dictionaryTypeLists = new ArrayList<>();
        dictionaryTypeLists = dictionaryTypeListRepository.findAll();

        if(dictionaryTypeLists.isEmpty() || dictionaryTypeLists.size() == 0){
            logger.warn("BaseDao.getDictionaryType:Collection DictionaryTypeList is empty.");
            throw new RestfulException(ErrorCode.INVALID_ARGUMENT,"BaseDao.getDictionaryType:Collection DictionaryTypeList is empty.");
        }

        return dictionaryTypeLists;
    }

    /**
     * 取得固定数量的排序数据
     *
     * @param query 其他检索条件
     * @param fieldName 排序字段
     * @param sortType  排序类型(1：升序；2：降序)
     * @param limit     查询数量(0:全部返回，不为0则返回指定数量)
     * @return 返回查询条件
     */
    protected Query getSortAndLimitQuery(Query query,String fieldName, int sortType, int limit) {

        // 排序
        switch (sortType) {
            case 1:
                query.with(new Sort(new Sort.Order(Sort.Direction.ASC, fieldName)));
                break;
            case 2:
                query.with(new Sort(new Sort.Order(Sort.Direction.DESC, fieldName)));
                break;
            default:
                query.with(new Sort(new Sort.Order(Sort.Direction.ASC, fieldName)));
        }

        // 数量
        if (Integer.compare(0, limit) < 0) {
            query.limit(limit);
        }

        return query;
    }

    /**
     * 拼接Where的is(=)条件
     *
     * @param query 其他检索条件
     * @param where 条件集合
     * @return
     */
    protected  Query getWhereIsQuery(Query query, Map<String,Object> where){
        for(Map.Entry<String,Object> e : where.entrySet()){
            query.addCriteria(Criteria.where(e.getKey()).is(e.getValue()));
        }

        return query;
    }

    /**
     * 拼接Where的lte(<=)条件
     *
     * @param query 其他检索条件
     * @param where 条件集合
     * @return
     */
    protected  Query getWhereLteQuery(Query query, Map<String,Object> where){
        for(Map.Entry<String,Object> e : where.entrySet()){
            query.addCriteria(Criteria.where(e.getKey()).lte(e.getValue()));
        }

        return query;
    }

}
