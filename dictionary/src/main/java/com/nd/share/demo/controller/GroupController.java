package com.nd.share.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.nd.share.demo.constants.Constants;
import com.nd.share.demo.service.GroupService;
import org.springframework.web.bind.annotation.PathVariable;
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
public class GroupController {

    @Resource
    private GroupService groupService;

    /**
     * 获取小知识/辨析/提示
     *
     * @param data 类型
     * @return
     */
    @RequestMapping(value = "/basicdata/group/{data}", method = RequestMethod.GET)
    private JSONObject getGroup(@PathVariable String data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("items",groupService.getGroupJson(data));
        return jsonObject;
    }
}
