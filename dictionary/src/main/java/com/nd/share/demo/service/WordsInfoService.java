package com.nd.share.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.nd.share.demo.service.Entity.Word.Src_resources;
import com.nd.share.demo.service.Entity.Word.Word;

import java.util.List;
import java.util.Map;

/**
 * @author jinmj
 * @date : 2016年6月13日 下午2:55:22
 */
public interface WordsInfoService {	
	
	/**
     * 保存词条更新通知
     *
     * @param List<uuid>
     */
    void saveSearchWord(List<String> wordUpdList);    
    
    /**
     * 获取LC接口的收索接口，映射List<Word>
     *
     * @param String word, int limitStart, int limitEnd
     */
    List<Word> getListLCByWord(String word, int limitStart, int limitEnd);
    
    /**
     * 获取LC接口的收索接口，映射Word
     *
     * @param String Id
     */
    List<Word> getListLCById(List<String> ids);
    
    /**
     * 文言文字典拼音索引颗粒模板    $RA0505
	 * 文言文虚词索引颗粒模板            $RA0506
	 * 文言文笔画索引颗粒模板            $RA0507
     * 文言文附录颗粒模板                     $RA0508
     * 文言文字索引模板颗粒                $RA0509
     * 文言文凡例模板颗粒                    $RA0510
     */
    List<String> getIndexSrc(String category, int limitStart, int limitEnd);
    
    /**
     * 传递jsonstring 获取 List<Src_resources> 集合
     */
    public List<Src_resources> getSrcResources(String jsonstr);

    /**
     * 批量获取词语详情
     *
     * @param ids uuid集合
     * @return
     */
    Map<String, Word> getListLCByIds(List<String> ids);

    /**
     * 生成词语详情对象
     *
     * @param object
     * @return
     */
    Word getJson(JSONObject object);
}
