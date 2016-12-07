/*
 * Copyright (c) 2016.  ND.
 * gouxiaobin.
 */

package com.nd.share.demo.config;

import com.nd.gaea.client.http.WafHttpClient;
import com.nd.gaea.client.http.WafSecurityHttpClient;
import com.nd.gaea.client.support.DeliverBearerAuthorizationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 郭晓斌(121017)
 * @version created on 20160527.
 */
@Configuration
public class ApplicationConfig {
    @Bean
    public WafSecurityHttpClient wafSecurityHttpClient() {
        WafSecurityHttpClient wafSecurityHttpClient = new WafSecurityHttpClient();
        wafSecurityHttpClient.setBearerAuthorizationProvider(new DeliverBearerAuthorizationProvider());
        return wafSecurityHttpClient;
    }

    @Bean
    public WafHttpClient wafHttpClient() {
        WafHttpClient wafHttpClient = new WafHttpClient();
        return wafHttpClient;
    }
}
