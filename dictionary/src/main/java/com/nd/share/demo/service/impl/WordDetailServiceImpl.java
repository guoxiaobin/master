package com.nd.share.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.nd.share.demo.domain.WordDetail;
import com.nd.share.demo.repository.dao.WordDetailDao;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nd.share.demo.common.response.ErrorCode;
import com.nd.share.demo.common.response.RestfulException;
import com.nd.share.demo.constants.Messages;
import com.nd.share.demo.repository.dao.WordSearchLogDao;
import com.nd.share.demo.service.WordDetailService;
import com.nd.share.demo.service.WordsInfoService;

/**
 * 词语详情
 * 
 * @author 郭晓斌(121017)
 * @version created on 20160602.
 */
@Service
public class WordDetailServiceImpl implements WordDetailService {

	@Resource
	private WordsInfoService wordsInfoService;
	@Resource
	private WordSearchLogDao wordSearchLogDao;
	@Resource
	private WordDetailDao wordDetailDao;

	/**
	 * 获取词语详情
	 * 
	 * @param identifier
	 * @return
	 */
	@Override
	public JSONObject getWordDetailJson(String identifier) {
//		List<String> ids = new ArrayList<String>();
//		ids.add(identifier);
//		JSONArray array = (JSONArray) JSONArray.toJSON(wordsInfoService.getListLCById(ids));
//		JSONObject json = null;
//		try {
//			json = (JSONObject) JSONObject.toJSON(array.get(0));
//		} catch (Exception e) {
//			e.printStackTrace();
//            throw new RestfulException(ErrorCode.NOT_FOUND, Messages.INTERNAL_SERVER_ERROR, String.format("WordDetailServiceImpl, msg:%s", e.getMessage()));
//		}

		JSONObject json = new JSONObject();
		List<WordDetail> wordDetailList = wordDetailDao.getWordDetailList(identifier);
		if(wordDetailList != null && wordDetailList.size() > 0){
			json = JSONObject.parseObject(wordDetailList.get(0).getContent(),JSONObject.class);
		}

		return json;
	}

	/**
	 * 保存搜索词语
	 * 
	 * @param identifier
	 *            词语id
	 * @param character
	 *            词语
	 */
	@Override
	public void saveSearchWord(String identifier, String character) {
		wordSearchLogDao.saveSearchWord(identifier, character);
	}

}
