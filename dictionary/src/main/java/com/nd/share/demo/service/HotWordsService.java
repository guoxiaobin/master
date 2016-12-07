package com.nd.share.demo.service;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;

/**
 * 热词
 * @author 郭晓斌(121017)
 * @version created on 20160601.
 */
public interface HotWordsService {

    /**
     * 获取热词列表
     */
    JSONObject getHotwords();
    
    public void saveHotword(String identifier,int count,Date createTime,Date UpdateTime);
}
