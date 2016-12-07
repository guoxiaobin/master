package com.nd.share.demo.service.context.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.nd.share.demo.service.context.IDictionarySource;

/**
 * @author jinmj
 * @date : 2016年6月21日 下午1:57:23
 */
@Service
public class DictionarySourceImpl implements IDictionarySource {


	@Override
	public int getId() {
		try {
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpServletRequest request = attributes.getRequest();
			String dictionarySource = getTypeIdFromRequset(request);
			return Integer.parseInt(dictionarySource.substring(dictionarySource.indexOf("=") + 1));
		} catch (Exception e) {
			return -1;
		}
	}

	private String getTypeIdFromRequset(HttpServletRequest request) {
		if (!StringUtils.isEmpty(request.getHeader("DictionarySource"))) {
			return request.getHeader("DictionarySource");
		}
		return null;
	}

}