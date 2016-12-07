package com.nd.share.demo.service.impl;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.nd.share.demo.util.StringUtil;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.nd.gaea.client.http.WafSecurityHttpClient;
import com.nd.share.demo.repository.dao.WordUpdListDao;
import com.nd.share.demo.service.WordsInfoService;
import com.nd.share.demo.service.Entity.Word.Src_resources;
import com.nd.share.demo.service.Entity.Word.Tags;
import com.nd.share.demo.service.Entity.Word.Word;

@Service
@PropertySource(value = {"classpath:lc.properties"})
public class WordsInfoServiceImpl<T> implements WordsInfoService {

    private static final Logger logger = LoggerFactory.getLogger(WordsInfoService.class);

    @Value("${lc.uri}")
    private String lcUrl;

    @Value("${lc.version}")
    private String lcVersion;

    @Resource
    private WordUpdListDao wordUpdListDao;

    @Override
    public void saveSearchWord(List<String> wordUpdList) {
        wordUpdListDao.saveWordUpdList(wordUpdList);
    }

    @Override
    public List<Word> getListLCByWord(String word, int limitStart, int limitEnd) {
        List<Word> words = new ArrayList<Word>();

        String url = lcUrl + "/" + lcVersion + "/coursewareobjects/management/actions/query?include={include}&coverage={coverage}&words={words}&limit=({limitStart},{limitEnd})" +
                "&category={category}";
        if (word != "") {
            url += "&prop={prop}";
        }

        try {
            String[] prop = {"title like ", "tags like "};
            boolean flag = false;
            for (int j = 0; j < 2; j++) {
                WafSecurityHttpClient httpClient = new WafSecurityHttpClient();
                Map<String, Object> param = new HashMap<>();
                param.put("include", "TI,CG");
                param.put("coverage", "Org/nd/");
                param.put("words", "");
                param.put("limitStart", limitStart);
                param.put("limitEnd", limitEnd);
                param.put("category", "$RA0504");
                if (word != "") {
                    param.put("prop", prop[j] + word);
                }

                Object obj = httpClient.getForObject(url, Object.class, param);
                String str = JSONObject.toJSONString(obj,
                        SerializerFeature.WriteMapNullValue);
                JSONObject jsonobject = (JSONObject) JSONObject.parse(str);
                JSONArray jsonarray = (JSONArray) JSONArray.toJSON(jsonobject
                        .get("items"));

                for (int i = 0; i < jsonarray.size(); i++) {
                    flag = true;
                    JSONObject object = (JSONObject) JSONObject
                            .toJSON(jsonarray.get(i));
                    words.add(getJson(object));
                }
                if (flag) {
                    break;
                }
            }

            return words;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return words;
        }
    }

    @Override
    public List<Word> getListLCById(List<String> ids) {
        List<Word> words = new ArrayList<Word>();
        for (String id : ids) {
            String url = lcUrl + "/" + lcVersion + "/coursewareobjects/{id}?include={include}&coverage={coverage}";
            Map<String, Object> param = new HashMap<>();
            param.put("id", id);
            param.put("include", "TI,CG");
            param.put("coverage", "Org/nd/");

            try {
                WafSecurityHttpClient httpClient = new WafSecurityHttpClient();
                Object obj = httpClient.getForObject(url, Object.class, param);
                String str = JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue);
                JSONObject jsonobject = (JSONObject) JSONObject.parse(str);
                words.add(getJson(jsonobject));
            } catch (Exception e) {
                logger.error(e.getMessage());
                return words;
            }
        }
        return words;
    }

    /**
     * 文言文字典拼音索引颗粒模板    $RA0505
     * 文言文虚词索引颗粒模板            $RA0506
     * 文言文笔画索引颗粒模板            $RA0507
     * 文言文附录颗粒模板                     $RA0508
     * 文言文字索引模板颗粒                $RA0509
     * 文言文凡例模板颗粒                    $RA0510
     */
    @Override
    public List<String> getIndexSrc(String category, int limitStart, int limitEnd) {
        List<String> list = new ArrayList<String>();
        String url = lcUrl + "/" + lcVersion + "/assets/management/actions/query?include={include}&coverage={coverage}&words={words}&limit=({limitStart},{limitEnd})" +
				"&category={category}";

        try {
            WafSecurityHttpClient httpClient = new WafSecurityHttpClient();
            Map<String, Object> param = new HashMap<>();
            param.put("include", "TI,CG");
            param.put("coverage", "Org/nd/");
            param.put("words", "");
            param.put("limitStart", limitStart);
            param.put("limitEnd", limitEnd);
            param.put("category", category);

            int j = 1;
            if ("$RA0508".equals(category)) {
                j = 2;
            }

            Object obj = httpClient.getForObject(url, Object.class, param);
            String str = JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue);
            JSONObject jsonobject = (JSONObject) JSONObject.parse(str);
            JSONArray jsonarray = (JSONArray) JSONArray.toJSON(jsonobject.get("items"));

            for (int i = 0; i < j; i++) {
                JSONObject objs = (JSONObject) JSONObject.toJSON(jsonarray.get(i));
                JSONObject objs1 = (JSONObject) JSONObject.toJSON(objs.get("tech_info"));
                JSONObject objs2 = (JSONObject) JSONObject.toJSON(objs1.get("href"));
                list.add((String) objs2.get("location"));
            }
            return list;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return list;
    }

    @Override
    public Word getJson(JSONObject object) {
        Word word = new Word();
        try {
            JSONObject retobj = (JSONObject) JSONObject.toJSON(object.get("custom_properties"));
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
            objectMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            word = objectMapper.readValue(retobj.get("original").toString(), Word.class);
            word.setIdentifier(object.get("identifier").toString());
            List<Tags> tags = new ArrayList<Tags>();
            JSONArray tagsarray = (JSONArray) JSONArray.toJSON(object.get("tags"));
            for (int j = 0; j < tagsarray.size(); j++) {
                Tags tag = objectMapper.readValue(tagsarray.get(j).toString(), Tags.class);
                tags.add(tag);
            }
            word.setTags(tags);
            List<Src_resources> srcRes = getSrcResources(retobj.get("original").toString());
            word.setSrc_resources(srcRes);
            return word;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return word;
        }
    }

    /**
     * 传递jsonstring 获取 List<Src_resources> 集合
     */
    @Override
    public List<Src_resources> getSrcResources(String jsonstr) {
        List<Src_resources> srcRes = new ArrayList<Src_resources>();
        String[] searchBeginStrs = {"src=\\\"", "\"mp3\":", "src='"};
        String[] searchEndStrs = {"\\\"", "\",", "'/"};
        int[] addPos = {5, 6, 4};

        for (int t = 0; t < searchBeginStrs.length; t++) {
            int beginpos = jsonstr.indexOf(searchBeginStrs[t]);
            String jsonstr1 = jsonstr.substring(beginpos + addPos[t]);
            int endpos = jsonstr1.indexOf(searchEndStrs[t]);
            while (beginpos > 0 && endpos > 0) {
                Src_resources srcRe = new Src_resources();
                String str = jsonstr1.substring(1, endpos);
                srcRe.setFile_path(str);
                str = str.replace("${ref_path}/", "");
                String[] stringArr = str.split("/");
                List<String> file_dir = new ArrayList<String>();
                int i = 0;
                for (String st : stringArr) {
                    if (i < stringArr.length - 1) {
                        file_dir.add(st);
                    }
                    i++;
                }
                srcRe.setFile_name(stringArr[stringArr.length - 1]);
                srcRe.setFile_dir(file_dir);
                srcRes.add(srcRe);
                beginpos = jsonstr1.indexOf(searchBeginStrs[t]);
                jsonstr1 = jsonstr1.substring(beginpos + addPos[t]);
                endpos = jsonstr1.indexOf(searchEndStrs[t]);
            }
        }

        return srcRes;
    }

    /**
     * 批量获取词语详情
     *
     * @param ids uuid集合
     * @return
     */
    @Override
    public Map<String, Word> getListLCByIds(List<String> ids) {
        if(ids == null || ids.size() == 0){
            return new HashMap<>();
        }
        Map<String, Word> words = new HashMap<>();
        String url = lcUrl + "/" + lcVersion + "/coursewareobjects/list?include=TI,CG";
        for (String id : ids) {
            url += "&rid=" + id;
        }

        try {
            WafSecurityHttpClient httpClient = new WafSecurityHttpClient();
            Object obj = httpClient.getForObject(url, Object.class);
            String str = JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue);
            Map<String, JSONObject> wordMaps = (Map<String, JSONObject>) JSONObject.parse(str);

            for (Map.Entry<String, JSONObject> e : wordMaps.entrySet()) {
                words.put(e.getKey(), getJson(e.getValue()));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return words;
        }
        return words;
    }

}
