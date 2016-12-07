package com.nd.share.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.nd.share.demo.constants.Constants;
import com.nd.share.demo.service.AppendixService;
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
public class AppendixController {

    @Resource
    private AppendixService appendixService;

    /**
     * 获取凡例/附录接口
     *
     * @return
     */
    @RequestMapping(value = "/basicdata/appendix", method = RequestMethod.GET)
    private JSONObject getAppendix() {
        JSONObject jsonObject = appendixService.getAppendix();
        return jsonObject;
    }
}
