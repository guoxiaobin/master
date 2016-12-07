package com.nd.share.demo.service;

import com.nd.share.demo.domain.AppUpdInfo;

/**
 * 热修复检测
 * 
 * @author jinmj
 * @date : 2016年6月13日 下午2:55:22
 */
public interface AppService {
	
	/**
     * 热修复信息获取
     *
     * @param Integer version
     * @param Integer type
     * @return
     */	
	AppUpdInfo getAppVersionInfo(Integer version, Integer type);
	/**
	 * 热修复信息更新
	 * @param version
	 * @param type
	 * @return
	 */
	public AppUpdInfo saveAppVersionInfo(Integer version,Integer type,String dicType);
    
}
