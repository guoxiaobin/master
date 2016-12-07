package com.nd.share.demo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nd.share.demo.service.Entity.collectWords.CollectWordSpell;

import java.util.Date;
import java.util.List;

/**
 * 收藏词语
 *
 * @author 郭晓斌(121017)
 * @version created on 20160601.
 */
@Document
public class CollectWord {

    @Id
    private String id;
    /**
     * 词语id
      */
    private String identifier;
    /**
     * 拼音首字母
     */
    private String character;
    private String spellindex;
    private String userId;
    private String entrance;
    private Date createTime;
    /**
     * 发音组合
     */
    private List<CollectWordSpell> spells;

    
    public List<CollectWordSpell> getSpells() {
        return spells;
    }

    public void setSpells(List<CollectWordSpell> spells) {
        this.spells = spells;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
 

    public String getSpellindex() {
        return spellindex;
    }

    public void setSpellindex(String spellindex) {
        this.spellindex = spellindex;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEntrance() {
        return entrance;
    }

    public void setEntrance(String entrance) {
        this.entrance = entrance;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
