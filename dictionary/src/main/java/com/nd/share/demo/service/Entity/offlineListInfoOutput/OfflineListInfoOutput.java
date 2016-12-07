package com.nd.share.demo.service.Entity.offlineListInfoOutput;

import java.util.List;
import java.util.Map;

/**
 * 输出资源包列表
 * 
 * @author 黄梦飞(920225)
 * @version created on 2016年6月23日下午9:00:15.
 */
public class OfflineListInfoOutput {
    private String type;
    private String title;
    private List<String> others;
    private long size;
    private List<Map<String,Object>> pkgs;
    
    
    public List<Map<String, Object>> getPkgs() {
        return pkgs;
    }
    public void setPkgs(List<Map<String, Object>> pkgs) {
        this.pkgs = pkgs;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<String> getOthers() {
        return others;
    }
    public void setOthers(List<String> others) {
        this.others = others;
    }
    public long getSize() {
        return size;
    }
    public void setSize(long size) {
        this.size = size;
    }
    
    
    
    
}
