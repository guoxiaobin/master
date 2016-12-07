package com.nd.share.demo.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.nd.gaea.client.http.WafSecurityHttpClient;
import com.nd.share.demo.domain.AppUpdInfo;
import com.nd.share.demo.repository.dao.AppUpdInfoDao;
import com.nd.share.demo.repository.dao.BaseDao;
import com.nd.share.demo.service.AppService;

/**
 * 热修复检测
 * 
 * @author jinmj
 * @date : 2016年6月14日 下午3:29:55
 */
@Service
public class AppServiceImpl implements AppService {
	
	private static final Logger logger = LoggerFactory.getLogger(AppUpdInfoDao.class);
	
	@Value("${app.factory.uri}")	
	private String appHost = ""; 
	
	@Value("${app.factory.env}")	
	private String appEnv = ""; 
	
	@Value("${app.factory.android}")	
	private String appAndroid = "";
	
	@Resource
	private AppUpdInfoDao appUpdInfoDao;
	@Resource
	private MongoTemplate mongoTemplate;
	
	@Override
	public AppUpdInfo getAppVersionInfo(Integer version, Integer type) {
		return appUpdInfoDao.getAppVersionInfo(version, type);
	}

    /**
     * 保存App更新信息
     */
    @Override
    public AppUpdInfo saveAppVersionInfo(Integer version, Integer type , String dicType) {
        if(type==1){
            String url = appHost + appEnv + "/{version}?packageType={packageType}";         
            AppUpdInfo appUpdInfo = new AppUpdInfo();
            try {
                WafSecurityHttpClient httpClient = new WafSecurityHttpClient();
                Map<String, Object> param = new HashMap<>();
                param.put("version", version);
                param.put("packageType", appAndroid);
                Object obj = httpClient.getForObject(url, Object.class, param);
                String str = JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue);
                JSONObject jsonobject = (JSONObject) JSONObject.parse(str);
                JSONObject jsonobj = (JSONObject) JSONObject.parse(jsonobject.get("data").toString());
                String download_url = jsonobj.get("download_url").toString();
                int version_code = Integer.parseInt((String) jsonobj.get("version_code"));
                long size = Long.parseLong(jsonobj.get("size").toString());
                appUpdInfo.setAppVersion(version_code);
                appUpdInfo.setPkgVersion(version_code);
                appUpdInfo.setUrl(download_url);
                appUpdInfo.setSize(size);
                appUpdInfo.setDescription("");  
                appUpdInfo.setType(1);
            } catch (Exception e) {
                logger.error(e.getMessage());               
            }
            return appUpdInfoDao.updateAppverInfo(appUpdInfo,dicType);
        }
        return appUpdInfoDao.getAppVersionInfo(version, type);
    }

}
