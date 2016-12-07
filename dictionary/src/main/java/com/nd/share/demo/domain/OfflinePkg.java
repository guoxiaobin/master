package com.nd.share.demo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 离线包
 * 
 * @author 黄梦飞(920225)
 * @version created on 2016年6月14日上午11:21:07.
 */
@Document
public class OfflinePkg {

    @Id
    private String id;
    /**
     * 包版本号
     */
    private int version;
    /**
     * 下载地址
     */
    private String url;
    /**
     * 1增量包 2全量包
     */
    private String flag;
    /**
     * 类型ID
     */
    private String typeId;
    /**
     * 包大小
     */
    private long size;
    /**
     * 创建日期
     */
    private Date createTime;

    
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
    
    
	









}
