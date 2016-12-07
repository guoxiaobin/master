package com.nd.share.demo.managebg.service;

import java.util.List;

/**
 * 共通Service
 *
 * @author 郭晓斌(121017)
 * @version created on 20160823.
 */
public interface CommonService {
    /**
     * 获取题目uri
     *
     * @param category 题目类型
     * @param start 开始索引
     * @param count 获取条数
     * @param prop
     * @return
     */
    public List<String> getIndexSrc(String category, int start, int count, String prop);
}
