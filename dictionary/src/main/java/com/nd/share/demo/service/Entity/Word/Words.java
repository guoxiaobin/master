package com.nd.share.demo.service.Entity.Word;

import java.util.List;

public class Words {

	private List<Explains> explains;

	private String remark;

	private String label;

	private Spell spell;

	private String title;

	private String mp3;

	private Xzs xzs;

	public void setExplains(List<Explains> explains) {
		this.explains = explains;
	}

	public List<Explains> getExplains() {
		return this.explains;
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

	public void setSpell(Spell spell) {
		this.spell = spell;
	}

	public Spell getSpell() {
		return this.spell;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setXzs(Xzs xzs) {
		this.xzs = xzs;
	}

	public Xzs getXzs() {
		return this.xzs;
	}

	public String getMp3() {
		return mp3;
	}

	public void setMp3(String mp3) {
		this.mp3 = mp3;
	}

}
