package com.nd.share.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.nd.share.demo.constants.Constants;
import com.nd.share.demo.service.AuthorwordsService;
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
public class AuthorwordsController {

    @Resource
    private AuthorwordsService authorwordsService;

    /**
     * 获取编者的话
     *
     * @return
     */
    @RequestMapping(value = "/basicdata/authorwords", method = RequestMethod.GET)
    private JSONObject getAuthorwords() {
        JSONObject jsonObject = authorwordsService.getAuthorworJson();
        return jsonObject;
    }
}
