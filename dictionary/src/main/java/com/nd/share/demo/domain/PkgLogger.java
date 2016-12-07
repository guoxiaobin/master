package com.nd.share.demo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 离线包打包日志
 *
 *
 * @author 郭晓斌(121017)
 * @version edit on 2016年7月05日
 */
@Document
public class PkgLogger {
    @Id
    private String id;
    private String dicType;
    private String content;
    private String srcExcption;
    private Date createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDicType() {
        return dicType;
    }

    public void setDicType(String dicType) {
        this.dicType = dicType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSrcExcption() {
        return srcExcption;
    }

    public void setSrcExcption(String srcExcption) {
        this.srcExcption = srcExcption;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
