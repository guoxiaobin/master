package com.nd.share.demo.service.Entity;

/**
 * 选择题
 *
 * @author 郭晓斌(121017)
 * @version created on 20160823.
 */
public class SimpleChoice {
    // 答案的ID
    private String identifier;
    // 是否固定位置
    private String fixed;
    // 选项文本
    private String text;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String isFixed() {
        return fixed;
    }

    public void setFixed(String fixed) {
        this.fixed = fixed;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
