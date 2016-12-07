package com.nd.share.demo.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author 郭晓斌(121017)
 * @version created on 20160602.
 */
public interface AppendixService {

    /**
     * 获取凡例/附录
     *
     * @return
     */
    JSONObject getAppendix();

    /**
     * 获取凡例/编者的话/小知识/辨析/提示
     *
     * @param type 获取标识
     *             1:凡例
     *             2:编者的话
     *             3:小知识
     *             4:辨析
     *             5:提示
     * @return
     */
    List<JSONObject> getOther(int type);

}
