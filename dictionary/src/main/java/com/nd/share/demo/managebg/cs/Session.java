package com.nd.share.demo.managebg.cs;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

/**
 * Created by Kylin on 2016/6/23 0023.
 */
@Data
@NoArgsConstructor
public class Session {

    private String accessKey;
    @JSONField(name = "access_method")
    private String accessMethod;
    @JSONField(name = "access_url")
    private String accessUrl;
    private Map preview;
    private String uuid;
    @JSONField(name = "expire_time")
    private Date expireTime;
    @JSONField(name = "session_id")
    private String sessionId;
    @JSONField(name = "dist_path")
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
