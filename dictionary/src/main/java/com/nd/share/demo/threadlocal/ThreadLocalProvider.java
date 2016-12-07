package com.nd.share.demo.threadlocal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 获取线程安全变量
 *
 * @author 郭晓斌(121017)
 * @version created on 20160622.
 * @note 线程安全变量
 */
@Component(value = "threadLocalProvider")
public class ThreadLocalProvider {

    public String getTableName() {
        String tableThreadName = TableNameThreadLocal.getInstance().get();
        return tableThreadName;
    }
}
