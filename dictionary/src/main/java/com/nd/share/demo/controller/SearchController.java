package com.nd.share.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.nd.share.demo.constants.Constants;
import com.nd.share.demo.domain.WordsIndex;
import com.nd.share.demo.repository.dao.WordsIndexDao;
import com.nd.share.demo.service.Entity.Word.Tags;
import com.nd.share.demo.service.WordsInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author 郭晓斌(121017)
 * @version created on 20160531.
 */
@RestController
@RequestMapping(value = Constants.VERSION_CURRENT)
public class SearchController {

    @Resource
    private WordsInfoService wordsInfoService;
    @Resource
    private WordsIndexDao wordsIndexDao;

    /**
     * 搜索字词(获取模糊搜索字词)
     *
     * @param words 搜索字词
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    private JSONObject getSearch(
            @RequestParam(value = "words", required = true) String words) {
        List<Tags> taglist = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            if (words.charAt(0) >= 0x4e00 && words.charAt(0) <= 0x9fbb) {
                // 获取字索引
                List<WordsIndex> wordsIndexList = wordsIndexDao.searchWordsIndex(words);
                taglist = this.setTags(wordsIndexList);

                // 排序(升序)
                Collections.sort(taglist, new Comparator<Tags>() {
                    @Override
                    public int compare(Tags t1, Tags t2) {
                        if (t1.getType() > t2.getType()) {
                            return 1;
                        } else if (t1.getType() < t2.getType()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });
                jsonObject.put("items", taglist);
            } else {
                jsonObject.put("items", new ArrayList<Tags>());
            }
        } catch (Exception e) {
            jsonObject.put("items", new ArrayList<Tags>());
        }
        return jsonObject;
    }

    /**
     * 设定Tag
     *
     * @param wordsIndexList
     * @return
     */
    private List<Tags> setTags(List<WordsIndex> wordsIndexList) {
        List<Tags> tagsList = new ArrayList<>();
        for(int i = 0; i < wordsIndexList.size(); i++){
            Tags tags = new Tags();
            tags.setId(wordsIndexList.get(i).getIdentifier());
            tags.setTitle(wordsIndexList.get(i).getTitle());
            tags.setType(wordsIndexList.get(i).getType());
            tagsList.add(tags);
        }

        return tagsList;
    }
}
