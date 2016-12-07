package com.nd.share.demo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 热修复更新
 * 
 * @author 郭晓斌(121017)
 * @version created on 20160614.
 */
@Document
public class AppUpdInfo {

	@Id
	@JsonIgnore
	private String id;
	@JsonIgnore
	private int type;
	private int appVersion;
	private int pkgVersion;
	private String url;
	private long size;
	private String description;
	@JsonIgnore
	private String createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(int appVersion) {
		this.appVersion = appVersion;
	}

	public int getPkgVersion() {
		return pkgVersion;
	}

	public void setPkgVersion(int pkgVersion) {
		this.pkgVersion = pkgVersion;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
