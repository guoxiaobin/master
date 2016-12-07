package com.nd.share.demo.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jinmj
 * @date : 2016年7月8日 上午10:19:47
 */
@Service
public class ManagerBackInterceptor implements HandlerInterceptor {

    /**
     * 获取管理后台uri
     */
    @Value("${interceptor.uri}")
    private String interceptorUri;
    @Value("${unlogin.uri}")
    private String unloginUri;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        for (String s : interceptorUri.split(";")) {
            if (s.equals(request.getServletPath())) {
                // 无需验证登录状态
                if (unLoginUri(request.getServletPath())) {
                    return true;
                }
                // 防止非正常访问拦截（如：未登录）
                if ("0".equals(request.getSession().getAttribute("status"))) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    /**
     * 无需登录Uri
     *
     * @param uri
     * @return
     */
    private boolean unLoginUri(String uri) {
        boolean access = false;
        String[] unloginUris = unloginUri.split(";");
        for (int i = 0; i < unloginUris.length; i++) {
            if (uri.equals(unloginUris[i])) {
                access = true;
                break;
            }
        }
        return access;
    }

}
