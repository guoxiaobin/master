package com.nd.share.demo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 增量阀值状态
 *
 * @author 郭晓斌(121017)
 * @version created on 20160613.
 */
@Document
public class ThresholdStatus {

    @Id
    private String id;
    private String typeId;
    private int incCount;
    private int incDay;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public int getIncCount() {
        return incCount;
    }

    public void setIncCount(int incCount) {
        this.incCount = incCount;
    }

    public int getIncDay() {
        return incDay;
    }

    public void setIncDay(int incDay) {
        this.incDay = incDay;
    }
}
