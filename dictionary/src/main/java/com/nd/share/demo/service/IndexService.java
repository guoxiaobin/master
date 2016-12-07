package com.nd.share.demo.service;

import com.alibaba.fastjson.JSONArray;
import com.nd.share.demo.service.impl.IndexServiceImpl.IndexType;

/**
 * 
 * 
 * @author 黄梦飞(920225)
 * @version created on 2016年6月28日下午3:38:50.
 */
public interface IndexService {
    /**
     * 给定设定好的索引类型获取对应的索引数据
     * @param type PINYIN拼音索引,BIHUA笔画索引,XUCI虚词索引
     * @return
     */
    public JSONArray getIndex(IndexType type);
}
