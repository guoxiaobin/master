package com.nd.share.demo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 增量包阀值配置
 *
 * @author 郭晓斌(121017)
 * @version created on 20160613.
 */
@Document
public class ThresholdPkg {

    @Id
    private String id;
    private String typeId;
    private int count;
    private int day;
    private int pkgCount;
    private int packType;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getPkgCount() {
        return pkgCount;
    }

    public void setPkgCount(int pkgCount) {
        this.pkgCount = pkgCount;
    }

    public int getPackType() {
        return packType;
    }

    public void setPackType(int packType) {
        this.packType = packType;
    }
}
