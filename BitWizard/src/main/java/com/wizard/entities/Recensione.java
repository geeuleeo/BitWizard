package com.wizard.entities;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
public class Recensione {
	
    @EmbeddedId
    private ChiaveRecensione id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("viaggioId")
    @JoinColumn(name = "id_viaggio")
    @JsonBackReference
    private Viaggio viaggio;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("utenteId")
    @JoinColumn(name = "id_utente")
    @JsonBackReference
    private Utente utente;

    private String testo;

    private int rating;
    
    private Date data;

	public ChiaveRecensione getId() {
		return id;
	}

	public void setId(ChiaveRecensione id) {
		this.id = id;
	}

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

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
	
}
