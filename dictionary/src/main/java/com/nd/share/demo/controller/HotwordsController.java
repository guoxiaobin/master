package com.nd.share.demo.controller;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.nd.share.demo.constants.Constants;
import com.nd.share.demo.domain.Hotwords;
import com.nd.share.demo.service.HotWordsService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 郭晓斌(121017)
 * @version created on 20160531.
 */
@RestController
@RequestMapping(value = Constants.VERSION_CURRENT)
public class HotwordsController {

    @Resource
    private HotWordsService hotWordsService;

    /**
     * 获取热词列表
     *
     * @return
     */
    @RequestMapping(value = "/search/hotwords", method = RequestMethod.GET)
    private JSONObject getHotwords() {
        JSONObject jsonObject = hotWordsService.getHotwords();
        return jsonObject;
    }
    
    
    @RequestMapping(value = "/search/hotwordssave", method = RequestMethod.GET)
    private Object saveHotword(String id,int count) {
        hotWordsService.saveHotword(id, count, new Date(), new Date());
        return id;
    }
    
    
}
