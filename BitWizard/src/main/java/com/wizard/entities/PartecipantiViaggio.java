package com.wizard.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
public class PartecipantiViaggio {
	
	@EmbeddedId
    private ChiavePartecipantiViaggio id;

    @ManyToOne
    @MapsId("viaggioId")
    @JoinColumn(name = "viaggio_id")
    private Viaggio viaggio;

    @ManyToOne
    @MapsId("utenteId")
    @JoinColumn(name = "utente_id")
    private Utente utente;

    private String statoPartecipazione;

	public ChiavePartecipantiViaggio getId() {
		return id;
	}

	public void setId(ChiavePartecipantiViaggio id) {
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

	public String getStatoPartecipazione() {
		return statoPartecipazione;
	}

	public void setStatoPartecipazione(String statoPartecipazione) {
		this.statoPartecipazione = statoPartecipazione;
	}

}
