package com.nd.share.demo.managebg.vm;

/**
 * 辞典viewmodel
 *
 * @author 郭晓斌(121017)
 * @version created on 20160721.
 */
public class DictionaryCommon {
    private String type;
    private int dicType;
    private String key;
    private String value;
    private String operateType;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDicType() {
        return dicType;
    }

    public void setDicType(int dicType) {
        this.dicType = dicType;
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

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }
}
