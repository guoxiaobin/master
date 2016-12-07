/*
 * Copyright (c) 2016.  ND.
 * gouxiaobin.
 */

package com.nd.share.demo.config;

import com.nd.gaea.rest.config.WafWebSecurityConfigurerAdapter;
import com.nd.gaea.rest.security.services.impl.MacTokenService;
import com.nd.share.demo.util.SpringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

/**
 * 接口安全控制
 * 
 * @author 郭晓斌(121017)
 * @version created on 20160527.
 */
@Configuration
@EnableWebMvcSecurity
@PropertySource(value = {"classpath:managebg.properties"})
public class ApiSecurityConfig extends WafWebSecurityConfigurerAdapter {

	/**
	 * 获取管理后台uri
	 */
	@Value("${access.uri}")
	private String uri;

	@Override
	protected void onConfigure(HttpSecurity http) throws Exception {
		// http.authorizeRequests().antMatchers("/*").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/**").permitAll()
				//.antMatchers(HttpMethod.GET, "/*/words/collections?timestamp=0").authenticated()
				.antMatchers(HttpMethod.POST, "/*/get/lcupdatedatas/*").permitAll()
				.antMatchers(HttpMethod.POST, "/*/offlinepkg/updlistinfo").permitAll()
				.antMatchers(HttpMethod.POST, "/*/update/wordsinfo").permitAll()
				.antMatchers(HttpMethod.POST, "/*/offlinepkg/updlist_full").permitAll()
				.antMatchers(HttpMethod.POST, getAccessUri()).permitAll()
				.antMatchers("/**").authenticated();
	}
	
	@Bean
	@Primary
	public MacTokenService macTokenService() {
		return new MacTokenServiceImpl();
	}

	@Bean
	public static SpringUtil springUtil() {
		return new SpringUtil();
	}

	/**
	 * 设定管理后台请求
	 *
	 * @return
     */
	private String[] getAccessUri(){
		String[] uris = uri.split(";");
		return uris;
	}
}
