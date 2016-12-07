package com.nd.dictionary.internal.model;

import java.util.List;
import java.util.Map;

/**
 * LC资源模型
 * Created by yanguanyu on 2016/9/1.
 */
public class LCResource {
    private String identifier;
    private String title;
    private String description;
    private String language;
    private Map<String, Object> preview;
    private List<String> tags;
    private Map<String, Object> customProperties;
    private Map<String, Object> categories;
    private TechInfo techInfo;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Map<String, Object> getPreview() {
        return preview;
    }

    public void setPreview(Map<String, Object> preview) {
        this.preview = preview;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Map<String, Object> getCustomProperties() {
        return customProperties;
    }

    public void setCustomProperties(Map<String, Object> customProperties) {
        this.customProperties = customProperties;
    }

    public Map<String, Object> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, Object> categories) {
        this.categories = categories;
    }

    public TechInfo getTechInfo() {
        return techInfo;
    }

    public void setTechInfo(TechInfo techInfo) {
        this.techInfo = techInfo;
    }

    public static class TechInfo {
        private Href href;

        public Href getHref() {
            return href;
        }

        public void setHref(Href href) {
            this.href = href;
        }
    }

    public static class Href {
        private String format;
        private int size;
        private String location;

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }
}
