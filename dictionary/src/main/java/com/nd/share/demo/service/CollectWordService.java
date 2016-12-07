package com.nd.share.demo.service;

import com.nd.share.demo.domain.CollectWord;
import com.nd.share.demo.service.Entity.collectWords.CollectWordOutPut;

import java.util.List;

/**
 * 收藏词语
 *
 * @author 郭晓斌(121017)
 * @version created on 20160601.
 */
public interface CollectWordService {

    /**
     * 收藏词语
     *
     * @param collectWord
     */
    int save(CollectWord collectWord);

    /**
     * 获取用户收藏字词
     *
     * @param timeStamp
     * @param userId
     * @param entrance
     * @return
     */
    List<CollectWordOutPut> getCollectWords(String timeStamp,String userId,String entrance);
    /**
     * 获取词语是否已被收藏
     * @param identifier
     * @param userId
     * @return
     */
    public int isCollectionWordExist(String identifier,String userId);

    /**
     * 取消收藏
     *
     * @param identifier 词语uuid
     * @param userId 用户id
     * @return
     */
    int cancelCollection(String identifier, String userId);
}
