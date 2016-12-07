package com.nd.share.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.nd.share.demo.common.response.ErrorCode;
import com.nd.share.demo.common.response.RestfulException;
import com.nd.share.demo.constants.Constants;
import com.nd.share.demo.constants.Messages;
import com.nd.share.demo.domain.OfflineListInfo;
import com.nd.share.demo.domain.OfflinePkg;
import com.nd.share.demo.repository.OfflineListInfoRepository;
import com.nd.share.demo.repository.OfflinePkgRepository;
import com.nd.share.demo.service.OfflinePkgService;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 郭晓斌(121017)
 * @version created on 20160531.
 */
@RestController
@RequestMapping(value = Constants.VERSION_CURRENT)
public class OffLinePkgController {

    @Resource
    private OfflinePkgService offlinePkgService;

    /**
     * 获取下载地址列表
     * @param version 版本号,为空则为初次获取
     * @param typeId 类型
     * @return jsonobject
     */
    @RequestMapping(value = "/offlinepkg/urllist", method = RequestMethod.GET)
    private Map<String, Object> getOfflinePkgsAdding(Integer version,String typeId){
    	Map<String, Object> map = new HashMap<String, Object>();
    	List<OfflinePkg> list = null;
    	if(version == null){
    		list = offlinePkgService.getOfflinePkgsAdding(typeId, -1);
    	}else{
    		list = offlinePkgService.getOfflinePkgsAdding(typeId, version);
    	}
    	List<Map<String,Object>> reslist = new ArrayList<Map<String,Object>>();
    	for(OfflinePkg i : list){
    		Map<String,Object> tmpmap = new HashMap<String, Object>();
    		tmpmap.put("flag", i.getFlag());
    		tmpmap.put("version", i.getVersion());
    		tmpmap.put("url", i.getUrl());
    		reslist.add(tmpmap);
    	}
    	map.put("items", reslist);
    	return map;
    }

    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/offlinepkg/updlistinfo", method = RequestMethod.POST)
    private Map<String,Object> getUpdListInfo(@RequestBody Map<String, Object> map) {
    	List<Map<String , Object>> requestlist = null;
    	Object items = map.get("items");
    	if(items == null ){
    		requestlist = new ArrayList<Map<String,Object>>();
    	}else{
    		try{
            	requestlist = (List<Map<String , Object>>)map.get("items");
            } catch (Exception e) {
            	e.printStackTrace();
	            throw new RestfulException(ErrorCode.SERVICE_ERROR_SYSTEM, Messages.INTERNAL_SERVER_ERROR,
	            		String.format("OfflinePkgServiceImpl, msg:%s", e.getMessage()));
            }
    	}
    	Map<String,Object> resmap = new HashMap<String, Object>();
    	resmap.put("items", offlinePkgService.getOfflineListInfo(requestlist,0));

        return resmap;
    }
    
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/offlinepkg/updlist_full", method = RequestMethod.POST)
    private Map<String,Object> getUpdListInfoFull(@RequestBody Map<String, Object> map) {
        List<Map<String , Object>> requestlist = null;
        Object items = map.get("items");
        if(items == null ){
            requestlist = new ArrayList<Map<String,Object>>();
        }else{
            try{
                requestlist = (List<Map<String , Object>>)map.get("items");
            } catch (Exception e) {
                e.printStackTrace();
                throw new RestfulException(ErrorCode.SERVICE_ERROR_SYSTEM, Messages.INTERNAL_SERVER_ERROR, 
                        String.format("OfflinePkgServiceImpl, msg:%s", e.getMessage()));
            }   
        }
        Map<String,Object> resmap = new HashMap<String, Object>();
        resmap.put("items", offlinePkgService.getOfflineListInfo(requestlist,1));
  
        return resmap;
    }
}
