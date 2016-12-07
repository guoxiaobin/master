package com.nd.share.demo.managebg.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.nd.gaea.client.http.WafSecurityHttpClient;
import com.nd.share.demo.managebg.service.CommonService;
import com.nd.share.demo.service.WordsInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 共通Service
 *
 * @author 郭晓斌(121017)
 * @version created on 20160823.
 */
@Service
public class CommonServiceImpl implements CommonService {
    private static final Logger logger = LoggerFactory.getLogger(WordsInfoService.class);

    @Value("${lc.uri}")
    private transient String lcHost;
    @Value("${lc.version}")
    private transient String lcVer;

    /**
     * 获取题目uri
     *
     * @param category 题目类型
     * @param start 开始索引
     * @param count 获取条数
     * @param prop
     * @return
     */
    public List<String> getIndexSrc(String category, int start, int count, String prop){
        List<String> indexSrcList = new ArrayList<>();
        String url = this.lcHost + "/" + this.lcVer + "/" + "questions/management/actions/query?include={include}&category={category}&coverage={coverage}&limit=({start},{count})" +
                "&words={words}&prop={prop}";

        WafSecurityHttpClient httpClient = new WafSecurityHttpClient();
        Map<String, Object> param = new HashMap<>();
        param.put("include", "TI,LC,EDU,CG,CR");
        param.put("coverage", "Org/rjs_dictionary/OWNER");
        param.put("words", "");
        param.put("start", start);
        param.put("count", count);
        param.put("category", category);
        param.put("prop", prop);

        try {
            Object obj = httpClient.getForObject(url, Object.class, param);
            String str = JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue);
            JSONObject jsonobject = (JSONObject) JSONObject.parse(str);
            JSONArray jsonarray = (JSONArray) JSONArray.toJSON(jsonobject.get("items"));

            for(int i = 0; i < jsonarray.size(); i++){
                JSONObject objs = (JSONObject) JSONObject.toJSON(jsonarray.get(i));
                JSONObject objs1 = (JSONObject) JSONObject.toJSON(objs.get("tech_info"));
                JSONObject objs2 = (JSONObject) JSONObject.toJSON(objs1.get("href"));
                indexSrcList.add((String) objs2.get("location"));
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }

        return indexSrcList;
    }
}
