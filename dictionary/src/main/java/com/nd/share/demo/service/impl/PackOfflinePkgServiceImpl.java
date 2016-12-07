package com.nd.share.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.nd.share.demo.domain.DictionaryTypeList;
import com.nd.share.demo.domain.ThresholdPkg;
import com.nd.share.demo.domain.ThresholdStatus;
import com.nd.share.demo.repository.dao.BaseDao;
import com.nd.share.demo.service.PackOfflinePkgService;
import com.nd.share.demo.task.OfflinePkgTimerTask;
import com.nd.share.demo.util.StringUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/3 0003.
 */
@Service
public class PackOfflinePkgServiceImpl implements PackOfflinePkgService {

    @Resource
    private BaseDao baseDao;
    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private OfflinePkgTimerTask offlinePkgTimerTask;

    /**
     * 离线包打包
     *
     * @return
     */
    @Override
    public JSONObject packOfflinePkg(){
        JSONObject jsonObject = new JSONObject();

        if(OfflinePkgTimerTask.pkgStutas == 1){
            jsonObject.put("msg","服务器正在打包，请稍后再打包");
            jsonObject.put("time", StringUtil.getDateStr(new Date()));
            return jsonObject;
        }

        List<DictionaryTypeList> dictionaryTypeLists = baseDao.getDictionaryTypes();
        for (DictionaryTypeList d : dictionaryTypeLists) {
            // 辞典类型
            String dicType = d.getType();
            String thresholdStatus = BaseDao.Collection.THRESHOLDSTATUS.getName() + "_" + dicType;
            String thresholdPkg = BaseDao.Collection.THRESHOLDPKG.getName() + "_" + dicType;

            // 获取增量包阀值信息
            List<ThresholdPkg> thresholdPkgs = mongoTemplate.findAll(ThresholdPkg.class,thresholdPkg);
            Map<String, Integer> countMap = new HashMap<>();
            Map<String, Integer> dayMap = new HashMap<>();
            for(ThresholdPkg t :thresholdPkgs){
                countMap.put(t.getTypeId(),t.getCount());
                dayMap.put(t.getTypeId(),t.getDay());
            }

            // 更新增量包状态值
            List<ThresholdStatus> thresholdStatuses = mongoTemplate.findAll(ThresholdStatus.class,thresholdStatus);
            for (ThresholdStatus t :thresholdStatuses){
                t.setIncCount(countMap.get(t.getTypeId()));
                t.setIncDay(dayMap.get(t.getTypeId()));
                mongoTemplate.save(t,thresholdStatus);
            }
        }

        // 离线包打包
        int status = offlinePkgTimerTask.packOfflinePkg();

        if(Integer.compare(0,status) == 0) {
            jsonObject.put("msg", "打包成功");
        }
        if(Integer.compare(2,status) == 0) {
            jsonObject.put("msg", "服务端打包出现异常请联系管理人员");
        }
        jsonObject.put("time", StringUtil.getDateStr(new Date()));

        return jsonObject;
    }
}
