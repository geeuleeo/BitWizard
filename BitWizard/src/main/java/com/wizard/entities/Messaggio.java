package com.wizard.entities;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Messaggio {
	
	@EmbeddedId
    private ChiaveMessaggio chiavemessaggio;

	@ManyToOne(fetch = FetchType.LAZY)
    @MapsId("viaggioId")
    @JoinColumn(name = "viaggio_id")
    @JsonBackReference
    private Viaggio viaggio;
    
	@ManyToOne(fetch = FetchType.LAZY)
    @MapsId("utenteId")
	@JoinColumn(name = "utente_id", insertable = false, updatable = false)
    @JsonBackReference
    private Utente utente;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "immagine_id", nullable = true)
    @JsonBackReference
    private Immagine immagine;
    
    private String testo;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;

	public Viaggio getViaggio() {
		return viaggio;
	}

	public void setViaggio(Viaggio viaggio) {
		this.viaggio = viaggio;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public Immagine getImmagine() {
		return immagine;
	}

	public void setImmagine(Immagine immagine) {
		this.immagine = immagine;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public ChiaveMessaggio getChiavemessaggio() {
		return chiavemessaggio;
	}

	public void setChiavemessaggio(ChiaveMessaggio chiavemessaggio) {
		this.chiavemessaggio = chiavemessaggio;
	}

}