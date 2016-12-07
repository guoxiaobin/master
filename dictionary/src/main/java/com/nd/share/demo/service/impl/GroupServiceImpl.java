package com.nd.share.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.nd.share.demo.constants.Constants;
import com.nd.share.demo.service.AppendixService;
import com.nd.share.demo.service.GroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 小知识/辨析/提示
 *
 * @author 郭晓斌(121017)
 * @version created on 20160602.
 */
@Service
public class GroupServiceImpl implements GroupService {

    @Resource
    private AppendixService appendixService;

    /**
     * 获取小知识/辨析/提示
     *
     * @param data 数据标识
     * @return
     */
    @Override
    public List<JSONObject> getGroupJson(String data) {
        int type = 0;

        // 小知识
        if (Constants.GroupType.K.getValue().equals(data)) {
            type = 3;
        }
        // 辨析
        if (Constants.GroupType.D.getValue().equals(data)) {
            type = 4;
        }
        // 提示
        if (Constants.GroupType.H.getValue().equals(data)) {
            type = 5;
        }

        // 获取数据
        List<JSONObject> jsonObjects = appendixService.getOther(type);
//        List<JSONObject> rdJsonObjects = new ArrayList();
//        if(jsonObjects != null && jsonObjects.size() > 0){
//            rdJsonObjects = new ArrayList<>();
//            Random rand = new Random();
//            // 随机返回条数
//            int randNum = rand.nextInt(jsonObjects.size()) + 1;
//            // 混淆顺序
//            Collections.shuffle(jsonObjects);
//            for (int i = 0; i < randNum; i++) {
//                rdJsonObjects.add(jsonObjects.get(i));
//            }
//        }

        return jsonObjects;
    }
}
