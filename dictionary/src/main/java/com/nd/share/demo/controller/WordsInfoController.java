package com.nd.share.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.nd.share.demo.constants.Constants;
import com.nd.share.demo.service.WordsInfoService;
import com.nd.share.demo.service.cs.CsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author jinmj
 * @date : 2016年6月13日 下午2:52:54
 */
@RestController
@RequestMapping(value = Constants.VERSION_CURRENT + "/update")
public class WordsInfoController {	
	
	@Resource
	private WordsInfoService wordsInfoService;
	
	@Resource
	private CsService csService;

	/**
     * 词条更新信息通知
     *
     * @param List<uuid>
     * @return
     */
    @RequestMapping(value = "/wordsinfo", method = RequestMethod.POST)
    private JSONObject getWordsInfo(@RequestBody Map<String, List<String>> map) {
        JSONObject jsonObject = new JSONObject();
        try{
        	wordsInfoService.saveSearchWord(map.get("items"));
        	jsonObject.put("status", "0");
        } catch (Exception e) {
        	jsonObject.put("status", "1");
        }        
        return jsonObject;
    }
}
