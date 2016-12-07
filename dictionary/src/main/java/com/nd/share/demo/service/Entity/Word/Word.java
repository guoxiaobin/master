package com.nd.share.demo.service.Entity.Word;

import java.util.List;
import java.util.Map;

public class Word {

	private String identifier;

	private List<Tags> tags;

	private List<Src_resources> src_resources;

	private List<Constructs> constructs;

	private String label;

	private List<Links> links;

	private List<Spells> spells;

	private String title;

	private String mp3;

	private Traditional_font traditional_font;

	private Map<String, List<Map<String, Object>>> multimedia;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public List<Tags> getTags() {
		return tags;
	}

	public void setTags(List<Tags> tags) {
		this.tags = tags;
	}

	public List<Src_resources> getSrc_resources() {
		return src_resources;
	}

	public void setSrc_resources(List<Src_resources> src_resources) {
		this.src_resources = src_resources;
	}

	public void setConstructs(List<Constructs> constructs) {
		this.constructs = constructs;
	}

	public List<Constructs> getConstructs() {
		return this.constructs;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}

	public List<Links> getLinks() {
		return links;
	}

	public void setLinks(List<Links> links) {
		this.links = links;
	}

	public void setSpells(List<Spells> spells) {
		this.spells = spells;
	}

	public List<Spells> getSpells() {
		return this.spells;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTraditional_font(Traditional_font traditional_font) {
		this.traditional_font = traditional_font;
	}

	public Traditional_font getTraditional_font() {
		return this.traditional_font;
	}

	public String getMp3() {
		return mp3;
	}

	public void setMp3(String mp3) {
		this.mp3 = mp3;
	}

	public Map<String, List<Map<String, Object>>> getMultimedia() {
		return multimedia;
	}

    public void setMultimedia(Map<String, List<Map<String, Object>>> multimedia) {
        this.multimedia = multimedia;
    }
}
