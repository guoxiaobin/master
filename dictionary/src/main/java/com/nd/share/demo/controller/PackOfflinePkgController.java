package com.nd.share.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.nd.share.demo.constants.Constants;
import com.nd.share.demo.domain.*;
import com.nd.share.demo.repository.dao.BaseDao;
import com.nd.share.demo.repository.dao.ThresholdStatusDao;
import com.nd.share.demo.repository.dao.WordUpdListDao;
import com.nd.share.demo.service.PackOfflinePkgService;
import com.nd.share.demo.task.OfflinePkgTimerTask;
import com.nd.share.demo.util.ReadLcBaseDatasUtil;
import com.nd.share.demo.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 郭晓斌(121017)
 * @version created on 20160708.
 */
@RestController
@RequestMapping(value = Constants.VERSION_CURRENT)
public class PackOfflinePkgController {

    @Resource
    private BaseDao baseDao;
    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private PackOfflinePkgService packOfflinePkgService;

    @Value(value = "${env.dir}")
    private String envDir;


    @RequestMapping(value = "/offlinepkg/pack", method = RequestMethod.GET)
    private JSONObject packOfflinePkg(){
        JSONObject jsonObject = new JSONObject();
        jsonObject = packOfflinePkgService.packOfflinePkg();
        return jsonObject;
    }

    /**
     * lc数据更新通知
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/get/lcupdatedatas/{clean}", method = RequestMethod.POST)
    private JSONObject getLcUpdateDatas(@PathVariable String clean, @RequestBody Map<String,String> map){   	
    	
        JSONObject jsonObject = new JSONObject();
        // 获取表名
        String tlbLcBaseDatas = baseDao.getCollectionName(BaseDao.Collection.LCBASEDATAS.getName());
        
//        if( "SAYCLEANCLEANCLEAN".equals(clean) ){ mongoTemplate.dropCollection(tlbLcBaseDatas);	}
        
        for (Map.Entry<String,String> e : map.entrySet()){
            Query query = Query.query(Criteria.where("key").is(e.getKey()));
            LcBaseDatas lcBaseDatas = new LcBaseDatas();
            List<LcBaseDatas> lcBaseDatasList = mongoTemplate.find(query,LcBaseDatas.class,tlbLcBaseDatas);
            if(lcBaseDatasList != null && lcBaseDatasList.size() > 0){
                lcBaseDatasList.get(0).setValue(e.getValue());
                lcBaseDatas = lcBaseDatasList.get(0);
            }else{
                String type = "";
                lcBaseDatas.setKey(e.getKey());
                lcBaseDatas.setValue(e.getValue());
                if(e.getValue().contains("png")){
                    type = "img";
                }else if(e.getValue().contains("mp3")){
                    type = "mp3";
                }else{
                    type = "word";
                }
                lcBaseDatas.setType(type);
            }
            // 保存
            mongoTemplate.save(lcBaseDatas,tlbLcBaseDatas);
        }

        jsonObject.put("detail",map);
        return jsonObject;
    }

    /**
     * lc基础数据保存
     *
     * @param type
     * @return
     */
    @RequestMapping(value = "/save/lcdbaseatas/{type}", method = RequestMethod.GET)
    private JSONObject saveLcBaseDatas(@PathVariable int type) {
        JSONObject jsonObject = new JSONObject();
        Map<String, String> fileNameMap = new HashMap<>();
        String dataType = "";
        switch (type) {
            // 词语详情
            case 1:
                dataType = "word";
                // 移除数据
                this.removeDatas(dataType);
                fileNameMap.put("0", "files/" + envDir + "/words/fileNameUuidMap.txt");
                break;
            // 图片
            case 2:
                dataType = "img";
                // 移除数据
                this.removeDatas(dataType);
                fileNameMap.put("0", "files/" + envDir + "/img/garbledPngCsUriMap.txt");
                fileNameMap.put("1", "files/" + envDir + "/img/imgCsUriMap.txt");
                break;
            // 音频
            case 3:
                dataType = "mp3";
                // 移除数据
                this.removeDatas(dataType);
                fileNameMap.put("0", "files/" + envDir + "/mp3/mp3NameCsPathMap.txt");
                break;
        }

        // 获取数据映射集合
        Map<String, String> datasMap = ReadLcBaseDatasUtil.getMapFromFile(fileNameMap);
        // 入库
        Date date = new Date();
        for(Map.Entry<String, String> e : datasMap.entrySet()){
            LcBaseDatas lcBaseDatas = new LcBaseDatas();
            lcBaseDatas.setKey(e.getKey());
            lcBaseDatas.setValue(e.getValue());
            lcBaseDatas.setType(dataType);
            lcBaseDatas.setTime(date);
            mongoTemplate.save(lcBaseDatas,baseDao.getCollectionName(BaseDao.Collection.LCBASEDATAS.getName()));
        }

        jsonObject.put("data",datasMap);
        return jsonObject;
    }

    /**
     * 移除数据
     * @param type
     */
    private void removeDatas(String type){
        // 获取表名
        String tlbLcBaseDatas = baseDao.getCollectionName(BaseDao.Collection.LCBASEDATAS.getName());
        if(mongoTemplate.collectionExists(tlbLcBaseDatas)){
            Query query = Query.query(Criteria.where("type").is(type));
            mongoTemplate.remove(query,tlbLcBaseDatas);
        }
    }
}
