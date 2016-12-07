package com.nd.share.demo.service.cs.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.nd.gaea.WafException;
import com.nd.gaea.client.http.WafSecurityHttpClient;
import com.nd.sdp.cs.common.CsConfig;
import com.nd.sdp.cs.exception.CSSDKException;
import com.nd.sdp.cs.sdk.Dentry;
import com.nd.share.demo.service.cs.CsService;

@Service
@PropertySource(value = {"classpath:cs.properties"})
public class CsServiceImpl implements CsService {
	
	@Value("${cs.host}")	
	private String csHost = ""; 

	@Value("${cs.service.id}")
	private String serviceId = "";
	
	@Value("${cs.service.name}")
	private String serviceName = "";	

	private String session = null;
	
	private long expireAt = 0;
	
	@Override
	public Dentry uploadFile(String uploadfilePath, String saveFileName) {
		Dentry dentry = null;
		try {
			dentry = new Dentry();
		} catch (CSSDKException e) {
			throw new WafException("DIC/ILLEGAL_PARAMS", e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		dentry.setPath("/" + serviceName + "/zip/");			// path
		dentry.setName(saveFileName);									// name
		dentry.setScope(1); 											// 0-私密，1-公开，默认：0，可选
		String fileName = uploadfilePath;
		String session = getSession();
		Dentry resDentryh = null;
		try {
			resDentryh = dentry.upload(serviceName, fileName, null, session, null);
		} catch (Exception e) {
			throw new WafException("DIC/ILLEGAL_PARAMS", e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return resDentryh;
	}
	
	private String getSession() {
		CsConfig.setHost(csHost);
		if (session == null) {
			Map<String, Object> sessionMap = requestSession();
			session = (String) sessionMap.get("session");
			expireAt = (Long) sessionMap.get("expire_at");
			return session;
		} else {
			if (expireAt < System.currentTimeMillis()) {
				Map<String, Object> sessionMap = requestSession();
				session = (String) sessionMap.get("session");
				expireAt = (Long) sessionMap.get("expire_at");
				return session;
			}
		}
		return session;
	}

	private Map<String, Object> requestSession() {
		WafSecurityHttpClient httpClient = new WafSecurityHttpClient();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("path", "/" + serviceName + "/zip"); 	// 必须以 "/"+服务名称作为起始路径(例如：申请的服务名称为:example,path的开头为"/example");
		param.put("uid", "520668"); 							// 用户uid
		param.put("role", "user"); 								// 取值仅限字符串"user"、"admin"(user：只能管理授权的路径下自己的目录项,admin：可以管理授权的路径下全部的目录项)。
		param.put("service_id", serviceId);						// 内容服务申请的 service_id
		param.put("expires", 30*60*60); 							// session过期时间，单位秒
		String url = CsConfig.getHostUrl() + "/sessions";

		Map<String, String> requestResult = null;
		try {
			requestResult = httpClient.postForObject(url, param, Map.class);
		} catch (Exception e) {
			throw new WafException("DIC/ILLEGAL_PARAMS", e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("session", requestResult.get("session"));
		result.put("expire_at", requestResult.get("expire_at"));
		return result;
	}

}
