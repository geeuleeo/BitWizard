package com.wizard.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Tag {
	
	@Id
	@Column(name = "tag_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tagId;
	
	@Column(name = "tipo_tag")
	private String tipoTag;
	
	@Column(name = "img_tag")
	private String imgTag;
	
	@JsonIgnore
    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UtenteTag> utenteTags = new ArrayList<>();

	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	public String getTipoTag() {
		return tipoTag;
	}

	public void setTipoTag(String tipoTag) {
		this.tipoTag = tipoTag;
	}

	public String getImgTag() {
		return imgTag;
	}

	public void setImgTag(String imgTag) {
		this.imgTag = imgTag;
	}

	public List<UtenteTag> getUtenteTags() {
		return utenteTags;
	}

	public void setUtenteTags(List<UtenteTag> utenteTags) {
		this.utenteTags = utenteTags;
	}

}