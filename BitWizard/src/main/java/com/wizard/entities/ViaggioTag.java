package com.wizard.entities;

import java.util.Objects;

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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViaggioTag that = (ViaggioTag) o;
        return Objects.equals(viaggio.getViaggioId(), that.viaggio.getViaggioId()) &&
               Objects.equals(tag.getTagId(), that.tag.getTagId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(viaggio.getViaggioId(), tag.getTagId());
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
