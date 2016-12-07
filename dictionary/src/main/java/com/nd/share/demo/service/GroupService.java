package com.nd.share.demo.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 小知识/辨析/提示
 *
 * @author 郭晓斌(121017)
 * @version created on 20160602.
 */
public interface GroupService {

    /**
     * 获取小知识/辨析/提示
     *
     * @param data 数据标识
     * @return
     */
    List<JSONObject> getGroupJson(String data);
}
