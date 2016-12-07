package com.nd.share.demo.constants;

/**
 * 常量配置文件
 *
 * @author 郭晓斌(121017)
 * @version created on 20160527.
 */
public class Constants {
    public enum GroupType {
        //枚举索引类型 K 小知识;D 辨析 ;H 提示
        K("knowledge"), D("discriminate"), H("hint");

        private final String value;

        GroupType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * restful api 版本
     */
    public static final String VERSION_01 = "/v0.1";
    public static final String VERSION_CURRENT = VERSION_01;
    public static final String MAPPING_API = "/api";

}
