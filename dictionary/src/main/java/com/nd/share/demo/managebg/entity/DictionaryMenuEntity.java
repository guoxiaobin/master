package com.nd.share.demo.managebg.entity;

import com.nd.share.demo.managebg.domain.MBDictionaryMenus;

import java.util.List;

/**
 * 辞典Entity
 *
 * @author 郭晓斌(121017)
 * @version created on 20160726.
 */
public class DictionaryMenuEntity {
    private String dicName;
    private int typeId;
    private List<MBDictionaryMenus> dictionaryMenusList;

    public String getDicName() {
        return dicName;
    }

    public void setDicName(String dicName) {
        this.dicName = dicName;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public List<MBDictionaryMenus> getDictionaryMenusList() {
        return dictionaryMenusList;
    }

    public void setDictionaryMenusList(List<MBDictionaryMenus> dictionaryMenusList) {
        this.dictionaryMenusList = dictionaryMenusList;
    }
}
