/*
 * Copyright (c) 2016.  ND.
 * gouxiaobin.
 */

package com.nd.share.demo.config;

import javax.annotation.Resource;

import com.nd.share.demo.interceptor.ManagerBackInterceptor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.nd.gaea.rest.config.WafWebMvcConfigurerAdapter;

/**
 * Spring mvc配置
 *
 * @author 郭晓斌(121017)
 * @version created on 20160527.
 */
@EnableWebMvc
@ComponentScan(basePackages = {"com.nd"})
@EnableScheduling
@EnableAsync
@Configuration
public class WebConfig extends WafWebMvcConfigurerAdapter {
    @Resource
    private ManagerBackInterceptor managerBackInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
    }



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(managerBackInterceptor);
        super.addInterceptors(registry);
    }
}
