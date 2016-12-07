/*
 * Copyright (c) 2016.  ND.
 * gouxiaobin.
 */

package com.nd.share.demo.config;

import com.nd.gaea.rest.AbstractWafWebApplicationInitializer;

/**
 * web项目的启动配置
 *
 * @author 郭晓斌(121017)
 * @version created on 20160527.
 * @note web项目的启动配置
 */
public class WafWebApplicationInitializer extends AbstractWafWebApplicationInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {

        return new Class<?>[]{ApplicationConfig.class, ApiSecurityConfig.class, MongoConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebConfig.class};
    }
    
    @Override
	public boolean isDisableSecurity() {
		return true;
	}
}
