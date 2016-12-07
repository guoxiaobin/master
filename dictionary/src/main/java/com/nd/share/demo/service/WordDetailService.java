package com.nd.share.demo.service;

import com.alibaba.fastjson.JSONObject;

/**
 * 词语详情
 *
 * @author 郭晓斌(121017)
 * @version created on 20160602.
 */
public interface WordDetailService {

    /**
     * 获取词语详情
     *
     * @param identifier
     * @return
     */
    JSONObject getWordDetailJson(String identifier);

    /**
     * 保存搜索词语
     *
     * @param identifier
     * @param character
     */
    void saveSearchWord(String identifier,String character);
}
