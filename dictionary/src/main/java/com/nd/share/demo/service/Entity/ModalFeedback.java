package com.nd.share.demo.service.Entity;

/**
 * 解析
 *
 * @author 郭晓斌(121017)
 * @version created on 20160823.
 */
public class ModalFeedback {
    private String identifier;
    private String showHide;
    private String content;
    private String sequence = "1";
    private String outcomeIdentifier = "FEEDBACK";

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getShowHide() {
        return showHide;
    }

    public void setShowHide(String showHide) {
        this.showHide = showHide;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getOutcomeIdentifier() {
        return outcomeIdentifier;
    }

    public void setOutcomeIdentifier(String outcomeIdentifier) {
        this.outcomeIdentifier = outcomeIdentifier;
    }
}
