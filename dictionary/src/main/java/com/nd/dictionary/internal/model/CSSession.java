package com.nd.dictionary.internal.model;

import java.util.Date;
import java.util.Map;

/**
 * 内容服务session模型
 * Created by yanguanyu on 2016/9/1.
 */
public class CSSession {

    private String accessKey;
    private String accessMethod;
    private String accessUrl;
    private Map preview;
    private String uuid;
    private Date expireTime;
    private String sessionId;
    private String distPath;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAccessMethod() {
        return accessMethod;
    }

    public void setAccessMethod(String accessMethod) {
        this.accessMethod = accessMethod;
    }

    public String getAccessUrl() {
        return accessUrl;
    }

    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

    public Map getPreview() {
        return preview;
    }

    public void setPreview(Map preview) {
        this.preview = preview;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getDistPath() {
        return distPath;
    }

    public void setDistPath(String distPath) {
        this.distPath = distPath;
    }
}
