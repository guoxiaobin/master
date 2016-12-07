package com.nd.share.demo.service.Entity.collectWords;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * 收藏词语
 * 
 * @author 黄梦飞(920225)
 * @version created on 2016年6月30日下午5:39:01.
 */
@Document
public class CollectWordOutPut {
    /**
     * 词语id
      */
    private String identifier;
    private String character;
    private int type;
    private String explain;
    private List<CollectWordSpell> spells;
    private String spellindex;
    private long createTime;
    public String getIdentifier() {
        return identifier;
    }
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    public String getCharacter() {
        return character;
    }
    public void setCharacter(String character) {
        this.character = character;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public List<CollectWordSpell> getSpells() {
        return spells;
    }
    public void setSpells(List<CollectWordSpell> spells) {
        this.spells = spells;
    }
    public String getSpellindex() {
        return spellindex;
    }
    public void setSpellindex(String spellindex) {
        this.spellindex = spellindex;
    }
    public long getCreateTime() {
        return createTime;
    }
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    

    
    
}
