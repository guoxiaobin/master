package com.nd.share.demo.config;

import javax.servlet.http.HttpServletRequest;
import com.nd.gaea.rest.security.services.impl.MacTokenService;
 
/**
 * MAC token服务类
 * @author 110825
 * @since 0.9.7
 */
public class MacTokenServiceImpl extends MacTokenService {
 
    @Override
    public boolean disableMacTokenAuthentication(HttpServletRequest request) {
        //默认返回false不关闭，用户可根据当前request信息，决定是否跳过Mac token认证
        return false;
    }
 
}
