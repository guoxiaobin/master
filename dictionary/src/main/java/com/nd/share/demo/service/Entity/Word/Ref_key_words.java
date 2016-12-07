package com.nd.share.demo.service.Entity.Word;

public class Ref_key_words {

	private String title;

	private String label;

	private String mp3;

	private Key_word_description key_word_description;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Key_word_description getKey_word_description() {
		return key_word_description;
	}

	public void setKey_word_description(
			Key_word_description key_word_description) {
		this.key_word_description = key_word_description;
	}

	public String getMp3() {
		return mp3;
	}

	public void setMp3(String mp3) {
		this.mp3 = mp3;
	}

}
