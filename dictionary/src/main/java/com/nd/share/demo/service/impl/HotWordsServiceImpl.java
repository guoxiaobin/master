package com.nd.share.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.nd.share.demo.domain.Hotwords;
import com.nd.share.demo.repository.dao.HotwordsDao;
import com.nd.share.demo.repository.dao.WordSearchLogDao;
import com.nd.share.demo.service.HotWordsService;
import com.nd.share.demo.service.WordsInfoService;
import com.nd.share.demo.service.Entity.Word.Word;

import org.apache.commons.collections4.map.LinkedMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 热词
 *
 * @author 郭晓斌(121017)
 * @version created on 20160601.
 */
@Service
public class HotWordsServiceImpl implements HotWordsService {

    @Resource
    private HotwordsDao hotwordsDao;

    @Value("${hotword.limit}")
    private int hotwordCount = 0;
    
    @Resource
    private WordsInfoService wordsInfoService;

    /**
     * 获取热词列表
     */
    @Override
    public JSONObject getHotwords() {
        List<Hotwords> hotwordses = hotwordsDao.getHotwords("count", 2, hotwordCount);
        JSONObject json = new JSONObject();
        List<Map<String, Object>> hotwordsMaps = new ArrayList<>();

        if (!hotwordses.isEmpty()) {
            for (Hotwords h : hotwordses) {
                LinkedMap<String, Object> map = new LinkedMap<>();
                String id = h.getIdentifier();
                if(id == null){
                    continue;
                }
                String ch = h.getCharacter();
                if(ch == null || ch.equals("") || ch.contains("<img")){
                    continue;
                }
                /*List<String> tmplist = new ArrayList<String>();
                tmplist.add(id);
                List<Word> wordlist = wordsInfoService.getListLCById(tmplist);
                if(wordlist.size() == 0){
                    continue;
                }
                String title = wordlist.get(0).getTitle();
                if(title.contains("<img")){
                    continue;
                }*/
                map.put("identifier", h.getIdentifier());
                map.put("character", h.getCharacter());
                hotwordsMaps.add(map);
            }
        }

        json.put("items", hotwordsMaps);
        return json;
    }


    @Override
    public void saveHotword(String identifier, int count, Date createTime,
            Date UpdateTime) {
        Hotwords hw = new Hotwords();
        List<String> ids = new ArrayList<String>();
        ids.add(identifier);
        List<Word> wordlist = wordsInfoService.getListLCById(ids);
        if(wordlist.size() == 0){
            return ;
        }
        hw.setCount(count);
        hw.setCreateTime(createTime);
        hw.setUpdateTime(UpdateTime);
        hw.setIdentifier(identifier);
        hw.setCharacter(wordlist.get(0).getTitle());
        hotwordsDao.saveHotword(hw);
    }
}
