package com.nd.dictionary.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 生字卡模型
 * Created by yanguanyu on 2016/9/7.
 */
public class WordCard {
    private String word;
    @JsonProperty("spellAssets")
    public List<SpellAsset> spellAssets;

    public static class SpellAsset {
        private SpellTarget target;
        @JsonProperty("spellMultimediaPictureAssets")
        private List<MultimediaAsset> spellMultimediaPictureAssets;
        @JsonProperty("spellMultimediaVideoAssets")
        private List<MultimediaAsset> spellMultimediaVideoAssets;

        public SpellTarget getTarget() {
            return target;
        }

        public void setTarget(SpellTarget target) {
            this.target = target;
        }

        public List<MultimediaAsset> getSpellMultimediaPictureAssets() {
            return spellMultimediaPictureAssets;
        }

        public void setSpellMultimediaPictureAssets(List<MultimediaAsset> spellMultimediaPictureAssets) {
            this.spellMultimediaPictureAssets = spellMultimediaPictureAssets;
        }

        public List<MultimediaAsset> getSpellMultimediaVideoAssets() {
            return spellMultimediaVideoAssets;
        }

        public void setSpellMultimediaVideoAssets(List<MultimediaAsset> spellMultimediaVideoAssets) {
            this.spellMultimediaVideoAssets = spellMultimediaVideoAssets;
        }
    }

    public static class SpellTarget {
        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class MultimediaAsset {
        private String label;
        @JsonProperty("orderNumber")
        private int orderNumber;
        @JsonProperty("relationType")
        private String relationType;
        private List<String> tags;
        private AssetTarget target;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public int getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(int orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getRelationType() {
            return relationType;
        }

        public void setRelationType(String relationType) {
            this.relationType = relationType;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public AssetTarget getTarget() {
            return target;
        }

        public void setTarget(AssetTarget target) {
            this.target = target;
        }
    }

    public static class AssetTarget {
        private String title;
        private String format;
        private String location;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<SpellAsset> getSpellAssets() {
        return spellAssets;
    }

    public void setSpellAssets(List<SpellAsset> spellAssets) {
        this.spellAssets = spellAssets;
    }
}
