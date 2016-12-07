package com.nd.share.demo.service.Entity.Word;

import java.util.List;

public class Refs {

	private Bx bx;

	private String label;

	private Ref_book ref_book;

	private Ref_chapter ref_chapter;

	private List<Ref_related_words> ref_related_words;

	private Ref_sentence ref_sentence;

	private List<Ref_key_words> ref_key_words;

	private String title;

	private String mp3;

	private Ts ts;

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

	public void setRef_book(Ref_book ref_book) {
		this.ref_book = ref_book;
	}

	public Ref_book getRef_book() {
		return this.ref_book;
	}

	public void setRef_chapter(Ref_chapter ref_chapter) {
		this.ref_chapter = ref_chapter;
	}

	public Ref_chapter getRef_chapter() {
		return this.ref_chapter;
	}

	public List<Ref_related_words> getRef_related_words() {
		return ref_related_words;
	}

	public void setRef_related_words(List<Ref_related_words> ref_related_words) {
		this.ref_related_words = ref_related_words;
	}

	public void setRef_sentence(Ref_sentence ref_sentence) {
		this.ref_sentence = ref_sentence;
	}

	public Ref_sentence getRef_sentence() {
		return this.ref_sentence;
	}

	public List<Ref_key_words> getRef_key_words() {
		return ref_key_words;
	}

	public void setRef_key_words(List<Ref_key_words> ref_key_words) {
		this.ref_key_words = ref_key_words;
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

	public void setTs(Ts ts) {
		this.ts = ts;
	}

	public Ts getTs() {
		return this.ts;
	}

}
