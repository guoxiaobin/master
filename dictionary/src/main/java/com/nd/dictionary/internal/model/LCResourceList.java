package com.nd.dictionary.internal.model;

import java.util.List;

/**
 * LC资源分包模型
 * Created by yanguanyu on 2016/9/1.
 */
public class LCResourceList {
    private String limit;
    private int total;
    private List<LCResource> items;

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<LCResource> getItems() {
        return items;
    }

    public void setItems(List<LCResource> items) {
        this.items = items;
    }
}
