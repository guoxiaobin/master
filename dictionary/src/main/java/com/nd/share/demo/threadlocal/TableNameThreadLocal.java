package com.nd.share.demo.threadlocal;

/**
 * 安全线程
 *
 * @author 郭晓斌(121017)
 * @version created on 20160622.
 * @note 安全线程
 */
public class TableNameThreadLocal extends ThreadLocal<String> {

    private TableNameThreadLocal() {
    }

    private static TableNameThreadLocal instance = new TableNameThreadLocal();

    public static TableNameThreadLocal getInstance() {
        return instance;
    }


}
