package com.nd.share.demo.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nd.share.demo.service.AppendixService;
import com.nd.share.demo.service.WordsInfoService;
import com.nd.share.demo.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 郭晓斌(121017)
 * @version created on 20160602.
 */
@Service
public class AppendixServiceImpl implements AppendixService {

    @Resource
    private WordsInfoService wordsInfoService;

    @Value(value = "${ref.path}")
    private String refPath;

    /**
     * 获取凡例/附录
     *
     * @return
     */
    @Override
    public JSONObject getAppendix() {
        JSONObject json = new JSONObject();
        // 获取凡例
        List<JSONObject> jsonObjectList = getOther(1);
        if(jsonObjectList != null && jsonObjectList.size() > 0){
            json.put("illustrate", jsonObjectList.get(0));
        }else{
            json.put("illustrate", new JSONObject());
        }

        // 获取附录
        List<String> appendixSrcLst = wordsInfoService.getIndexSrc("$RA0508", 0, 10);
        for (String s : appendixSrcLst) {
            // 获取目录src
            String src = s.replace("${ref_path}/", refPath);
            String content = StringUtil.getContent(src);

            // 附录一
            if (src.contains("fl1.json")) {
                json.put("fl1", JSONObject.parseObject(content));
            }
            // 附录二
            if (src.contains("fl2.json")) {
                json.put("fl2", JSONObject.parseObject(content));
            }
        }

        if(appendixSrcLst == null || appendixSrcLst.size() == 0){
            json.put("fl1", new JSONObject());
            json.put("fl2", new JSONObject());
        }

        return json;
    }

    /**
     * 获取凡例/编者的话/小知识/辨析/提示
     *
     * @param type 获取标识
     *             1:凡例
     *             2:编者的话
     *             3:小知识
     *             4:辨析
     *             5:提示
     * @return
     */
    @Override
    public List<JSONObject> getOther(int type) {
        List<JSONObject> jsonObjectList = new ArrayList<>();

        List<String> srcLst = wordsInfoService.getIndexSrc("$RA0510", 0, 10);
        if(srcLst != null && srcLst.size() > 0){
            String src = srcLst.get(0).replace("${ref_path}/", refPath);
            String content = StringUtil.getContent(src);
            JSONArray jsonArray = JSONArray.parseArray(content);

            for (int i = 0; i < jsonArray.size(); i++) {
                // 获取数据
                JSONObject tempJson = JSONObject.parseObject(jsonArray.get(i).toString());
                if (Integer.compare(type, tempJson.getIntValue("type")) == 0) {
                    jsonObjectList.add(tempJson);
                }
            }
        }

        return jsonObjectList;
    }
}
