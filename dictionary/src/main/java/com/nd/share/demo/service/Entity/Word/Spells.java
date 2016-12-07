package com.nd.share.demo.service.Entity.Word;

import java.util.List;

public class Spells {

	private List<Explains> explains;

	private Jx_ts jx_ts;

	private String remark;

	private String label;

	private String title;

	private String mp3;

	private List<Words> words;

	private Xzs xzs;

	private Link link;

	public void setExplains(List<Explains> explains) {
		this.explains = explains;
	}

	public List<Explains> getExplains() {
		return this.explains;
	}

	public void setJx_ts(Jx_ts jx_ts) {
		this.jx_ts = jx_ts;
	}

	public Jx_ts getJx_ts() {
		return this.jx_ts;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public String getMp3() {
		return mp3;
	}

	public void setMp3(String mp3) {
		this.mp3 = mp3;
	}

	public void setWords(List<Words> words) {
		this.words = words;
	}

	public List<Words> getWords() {
		return this.words;
	}

	public void setXzs(Xzs xzs) {
		this.xzs = xzs;
	}

	public Xzs getXzs() {
		return this.xzs;
	}

	public Link getLink() {
		return link;
	}

	public void setLink(Link link) {
		this.link = link;
	}
}
