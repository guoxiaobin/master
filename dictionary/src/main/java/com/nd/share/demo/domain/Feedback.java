package com.nd.share.demo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 意见反馈
 *
 * @author 郭晓斌(121017)
 * @version created on 20160531.
 */
@Document
public class Feedback {

    @Id
    private String id;
    /**
     * 反馈类型（1 意见反馈；2是纠错）
     */
    private int type;
    private String content;
    private String imgurl;
    private String platformVersion;
    private String platformModel;
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public void setPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
    }

    public String getPlatformModel() {
        return platformModel;
    }

    public void setPlatformModel(String platformModel) {
        this.platformModel = platformModel;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
