package com.nd.share.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.nd.share.demo.service.AppendixService;
import com.nd.share.demo.service.AuthorwordsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 编者的话
 *
 * @author 郭晓斌(121017)
 * @version created on 20160602.
 */
@Service
public class AuthorwordsServiceImpl implements AuthorwordsService {

    @Resource
    private AppendixService appendixService;

    /**
     * 获取编者的话
     *
     * @return
     */
    @Override
    public JSONObject getAuthorworJson(){
        JSONObject jsonObject = new JSONObject();
        List<JSONObject> jsonObjects = appendixService.getOther(2);
        if(jsonObjects != null && jsonObjects.size() > 0){
            jsonObject = jsonObjects.get(0);
        }
        return jsonObject;
    }
}
