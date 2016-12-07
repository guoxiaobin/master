/**
 * 
 */
package com.nd.share.demo.task;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.nd.share.demo.domain.AppUpdInfo;
import com.nd.share.demo.domain.DictionaryTypeList;
import com.nd.share.demo.repository.dao.BaseDao;
import com.nd.share.demo.service.AppService;

/**
 * 热修复集中定时更新
 * @author 黄梦飞(920225)
 * @version created on 2016年8月3日下午7:23:36.
 */
@Component
public class AppUpdInfoTimerTask {
    
    private static final Logger logger = LoggerFactory.getLogger(AppUpdInfoTimerTask.class);
    
    @Resource
    private AppService appService;
    @Resource
    private BaseDao baseDao;
    /**
     * 热修复更新
     * @return 0 正在更新,1更新完毕2更新出错
     */
    public int saveAppUpdInfos2Web(){
        int updStatus = 0 ;
        
        String exceptionType = "NONE";
        try {
            @SuppressWarnings("unchecked")
            List<DictionaryTypeList> dictionaryTypeLists = baseDao.getDictionaryTypes();
            for (DictionaryTypeList d : dictionaryTypeLists) {
                // 辞典类型
                String dicType = d.getType();
                exceptionType = dicType;
                AppUpdInfo aui = appService.saveAppVersionInfo(1, 1, "_"+dicType);
                if(aui.getId() == null || aui.getId().equals("")){
                    logger.info("AppUpdInfoTimerTask.saveAppUpdInfos2Web:[Dictionary {}] AppUpdInfoUpdatedFailed.", dicType);
                }
                logger.info("AppUpdInfoTimerTask.saveAppUpdInfos2Web:[Dictionary {}] AppUpdInfoUpdatedSuccessfully.", dicType);
            }
            updStatus = 1;
        } catch (Exception e) {
            updStatus = 2;
            logger.info("AppUpdInfoTimerTask.saveAppUpdInfos2Web:[Dictionary {}] AppUpdInfoExcutedFail..", exceptionType);
        }
        
        
        return updStatus;
    }
}
