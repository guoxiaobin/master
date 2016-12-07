/**
 * 
 */
package com.nd.share.demo.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nd.share.demo.constants.Constants;
import com.nd.share.demo.service.IndexService;
import com.nd.share.demo.service.impl.IndexServiceImpl.IndexType;

/**
 * 索引控制层
 * @author 黄梦飞(920225)
 * @version created on 2016年6月28日下午4:22:50.
 */
@RestController
@RequestMapping(value = Constants.VERSION_CURRENT)
public class IndexController {
    @Resource
    private IndexService indexService;
    
    @RequestMapping(value = "/reference/spell", method = RequestMethod.GET)
    public Object getIndexPinyin(){
        JSONArray ja = indexService.getIndex(IndexType.PINYIN);
        JSONObject jo = new JSONObject();
        jo.put("items", ja);
        return jo;
    }
    
    @RequestMapping(value = "/reference/stroke", method = RequestMethod.GET)
    public Object getIndexBihua(){
        JSONArray ja = indexService.getIndex(IndexType.BIHUA);
        JSONObject jo = new JSONObject();
        jo.put("items", ja);
        return jo;
    }
    
    @RequestMapping(value = "/reference/emptywords", method = RequestMethod.GET)
    public Object getIndexXuci(){
        JSONArray ja = indexService.getIndex(IndexType.XUCI);
        JSONObject jo = new JSONObject();
        jo.put("items", ja);
        return jo;
    }
    @RequestMapping(value = "/index/test", method = RequestMethod.GET)
    public String test(){
        return "test";
    }
    
}
