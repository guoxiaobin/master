package com.nd.share.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.nd.share.demo.constants.Constants;
import com.nd.share.demo.domain.CollectWord;
import com.nd.share.demo.service.CollectWordService;
import com.nd.share.demo.service.Entity.collectWords.CollectWordOutPut;
import com.nd.share.demo.service.Entity.collectWords.CollectWordSpell;
import com.nd.share.demo.service.WordDetailService;
import com.nd.share.demo.util.StringUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 郭晓斌(121017)
 * @version created on 20160531.
 */
@RestController
@RequestMapping(value = Constants.VERSION_CURRENT + "/words")
public class WordsController {

    @Resource
    private CollectWordService collectWordService;

    @Resource
    private WordDetailService wordDetailService;

    /**
     * 获取用户收藏字词（服务端->客户端）
     *
     * @param timestamp 时间戳
     * @return
     */
    @RequestMapping(value = "/collections", method = RequestMethod.POST)
    private JSONObject getWorkCollects(@RequestHeader(value = "userId" , required = false) String userId,
                                       @RequestParam(value = "timestamp", required = true) String timestamp) {
        userId = StringUtil.getUserId(userId);
        JSONObject jsonObject = new JSONObject();
        List<CollectWordOutPut> collectWords = collectWordService.getCollectWords(timestamp,userId,null);
        jsonObject.put("items", collectWords);

        return jsonObject;
    }

    /**
     * 获取词语详情
     *
     * @param identifier 词语id
     * @return
     */
    @RequestMapping(value = "/{identifier}/details", method = RequestMethod.GET)
    private JSONObject getWordDetail(@PathVariable String identifier) {
        JSONObject jsonObject = new JSONObject();
        jsonObject = wordDetailService.getWordDetailJson(identifier);
        
		if (jsonObject.get("title") != null	&& !"".equals(jsonObject.get("title"))) {
			// 词语搜索日志入库
			wordDetailService.saveSearchWord(identifier, (String) jsonObject.get("title"));
		}

        return jsonObject;
    }

    /**
     * 收藏词语
     *
     * @param identifier 词语id
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/{identifier}/collect", method = RequestMethod.POST)
    private JSONObject saveWork(@RequestHeader(value = "userId", required = false) String userId,
                                @PathVariable String identifier,
                                @RequestBody Map<String, Object> map) {
        userId = StringUtil.getUserId(userId);
        JSONObject jsonObject = new JSONObject();

        CollectWord collectWord = new CollectWord();
        collectWord.setIdentifier(identifier);
        collectWord.setUserId(userId);
        collectWord.setCreateTime(new Date());
        
        String character = null;
        List<CollectWordSpell> spelllist = new ArrayList<CollectWordSpell>();
        try {
            Object tmpch = map.get("character");
            Object tmpspells = map.get("spells");
            if(tmpspells != null){
                character = (String)tmpch;
                List<Map<String,String>> tmpspelllist = (List<Map<String,String>>)tmpspells;
                for(Map<String,String> i : tmpspelllist){
                    CollectWordSpell tmpsp = new CollectWordSpell();
                    tmpsp.setPronounce(i.get("pronounce"));
                    tmpsp.setSpell(i.get("spell"));
                    spelllist.add(tmpsp);
                }
                collectWord.setCharacter(character);
                collectWord.setSpells(spelllist);
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("message", "收藏失败,请检查参数格式");
            jsonObject.put("code", "DICT/BAD_REQUEST");
            return jsonObject;
        }

        // 保存词语
        int res = collectWordService.save(collectWord);
        if(res == 1){
            jsonObject.put("message", "收藏成功");
            jsonObject.put("code", "DICT/COLLEC_WORD_SUCCES");
        }else if(res == 0){
            jsonObject.put("message", "收藏失败,该词语用户已经收藏过了");
            jsonObject.put("code", "DICT/COLLEC_WORD_EXIST");
        }else{
            jsonObject.put("message", "收藏失败,该词语不存在");
            jsonObject.put("code", "DICT/COLLECT_WORD_ID_ERROR");
        }
        
        return jsonObject;
    }

    /**
     * 获取分享地址
     *
     * @param identifier 词语id
     * @return
     */
    @RequestMapping(value = "/{identifier}/shareurl", method = RequestMethod.GET)
    private JSONObject getShareUrl(@RequestHeader(value = "userId", required = true) String userId,
                                   @RequestHeader(value = "entrance", required = true) String entrance,
                                   @PathVariable String identifier) {
        JSONObject jsonObject = new JSONObject();

        return jsonObject;
    }
    
    @RequestMapping(value = "/{identifier}/is_exist", method = RequestMethod.POST)
    private JSONObject getShareUrl(@RequestHeader(value = "userId", required = false) String userId,
                                   @PathVariable String identifier) {
        JSONObject jsonObject = new JSONObject();
        userId = StringUtil.getUserId(userId);
        int res = collectWordService.isCollectionWordExist(identifier, userId);
        String msg = null;
        if(res == 0){
            msg = "该词语未被收藏";
        }else{
            msg = "该词语已被收藏";
        }
        jsonObject.put("is_exist", res);
        jsonObject.put("msg", msg);
        return jsonObject;
    }

    @RequestMapping(value = "/cancel/collection/{identifier}", method = RequestMethod.DELETE)
    private JSONObject cancelCollection(@RequestHeader(value = "userId", required = false) String userId,
                                   @PathVariable String identifier) {
        JSONObject jsonObject = new JSONObject();
        userId = StringUtil.getUserId(userId);
        int res = collectWordService.cancelCollection(identifier, userId);
        String msg = null;
        if(res == 0){
            msg = "取消收藏成功";
        }else if(res == 1){
            msg = "取消收藏失败";
        }else{
            msg = "该词语未被收藏";
        }
        jsonObject.put("status", res);
        jsonObject.put("msg", msg);
        return jsonObject;
    }
}
