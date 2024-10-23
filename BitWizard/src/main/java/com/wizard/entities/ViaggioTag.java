package com.wizard.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ViaggioTag {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "viaggio_id", nullable = false)
    @JsonIgnore
    private Viaggio viaggio;

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    @JsonIgnore
    private Tag tag;
    
    public ViaggioTag() {}

    public ViaggioTag(Viaggio viaggio, Tag tag) {
        this.viaggio = viaggio;
        this.tag = tag;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Viaggio getViaggio() {
		return viaggio;
	}

	public void setViaggio(Viaggio viaggio) {
		this.viaggio = viaggio;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

}
