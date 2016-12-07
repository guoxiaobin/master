package com.nd.share.demo.domain;

import com.nd.share.demo.util.StringUtil;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 热词列表
 *
 * @author 郭晓斌(121017)
 * @version created on 20160718.
 */
@Document
public class LcBaseDatas {

    @Id
    private String id;
    private String key;
    private String value;
    private String time;
    /**
     * 数据类型（如：词语，音频，图片等）
     */
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(Date time) {
        String dateStr = StringUtil.getDateStr1(time);
        this.time = dateStr;
    }
}
