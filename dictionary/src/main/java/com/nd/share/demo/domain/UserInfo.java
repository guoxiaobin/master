package com.nd.share.demo.domain;

import org.apache.commons.collections4.map.LinkedMap;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 用户信息
 *
 * @author 郭晓斌(121017)
 * @version created on 20160601.
 */
@Document
public class UserInfo {

    /**
     * 用户ID
     */
    private String id;
    /**
     * 第三方平台token
     */
    private String accessToken;
    /**
     * 平台用户编号
     */
    private String userId;
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
