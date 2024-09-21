package com.wizard.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

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
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_iscrizione")
    private Date dataIscrizione;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_annullamento")
    private Date dataAnnullamento;

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

	public Date getDataIscrizione() {
		return dataIscrizione;
	}

	public void setDataIscrizione(Date dataIscrizione) {
		this.dataIscrizione = dataIscrizione;
	}

	public Date getDataAnnullamento() {
		return dataAnnullamento;
	}

	public void setDataAnnullamento(Date dataAnnullamento) {
		this.dataAnnullamento = dataAnnullamento;
	}
	
}