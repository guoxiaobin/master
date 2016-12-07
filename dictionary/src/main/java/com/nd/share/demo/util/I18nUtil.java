/*
 * Copyright (c) 2016.  ND.
 * Jungle.
 */

package com.nd.share.demo.util;

import org.springframework.context.support.MessageSourceAccessor;


/**
 * 获取多语言消息帮助类
 *
 * @author 杨文军(132500)
 * @version 2015年12月16日
 * @note 多语言
 */
public class I18nUtil {

    /**
     * 获取bean
     *
     * @return
     */
    public static MessageSourceAccessor getMessageSourceAccessor() {
        return SpringUtil.getBean(MessageSourceAccessor.class);
    }

    /**
     * 获取多语言配置消息
     *
     * @param code 消息代码
     * @return String 消息
     */
    public static String getMessage(String code) {
        return getMessageSourceAccessor().getMessage(code);
    }

    /**
     * 获取多语言配置消息
     *
     * @param code 消息代码
     * @param args 消息参数
     * @return String 消息
     */
    public static String getMessage(String code, Object[] args) {
        return getMessageSourceAccessor().getMessage(code, args);
    }

    /**
     * 获取多语言配置消息（带默认消息）
     *
     * @param code
     * @param defaultMessage
     * @return
     */
    public static String getMessage(String code, String defaultMessage) {
        return getMessageSourceAccessor().getMessage(code, defaultMessage);
    }
}
