package com.nd.share.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.nd.share.demo.constants.Constants;
import com.nd.share.demo.domain.UserInfo;
import com.nd.share.demo.service.UserInfoService;
import com.nd.share.demo.util.StringUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * @author 郭晓斌(121017)
 * @version created on 20160531.
 */
@RestController
@RequestMapping(value = Constants.VERSION_CURRENT)
public class UserLoginController {

    @Resource
    private UserInfoService userInfoService;

    /**
     * 用户登录
     *
     * @param userInfo 登录信息
     * @return
     */
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    private JSONObject geLogin(@RequestBody UserInfo userInfo) {
        JSONObject jsonObject = new JSONObject();
        userInfo.setId(StringUtil.getUUID());
        userInfo.setCreateTime(new Date());
        userInfoService.save(userInfo);
        return jsonObject;
    }
}
