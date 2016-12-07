package com.nd.share.demo.service.Entity.Word;

import java.util.List;

public class Other_explains {

	private Bx bx;

	private String label;

	private String other_explains;

	private List<Refs> refs;

	private String title;

	private String mp3;

	public void setBx(Bx bx) {
		this.bx = bx;
	}

	public Bx getBx() {
		return this.bx;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}

	public void setOther_explains(String other_explains) {
		this.other_explains = other_explains;
	}

	public String getOther_explains() {
		return this.other_explains;
	}

	public void setRefs(List<Refs> refs) {
		this.refs = refs;
	}

	public List<Refs> getRefs() {
		return this.refs;
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

}
