package com.nd.share.demo.managebg.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 白名单
 *
 * @author 郭晓斌(121017)
 * @version created on 20160805.
 */
@Document
public class MBWhiteList {

    @Id
    private String id;
    private String ip;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
