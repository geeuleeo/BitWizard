package com.wizard.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Amicizia {
	
	public enum StatoAmicizia {
	    IN_ATTESA,
	    ACCETTATO,
	    RIFIUTATO
	}

    @EmbeddedId
    private ChiaveAmicizia chiave;

    @MapsId("utenteInviante")
    @ManyToOne
    @JoinColumn(name = "utente_inviante")
    private Utente utenteInviante;

    @MapsId("utenteRicevente")
    @ManyToOne
    @JoinColumn(name = "utente_ricevente")
    private Utente utenteRicevente;

    @Temporal(TemporalType.DATE)
    private Date dataAmicizia;
    
    @Enumerated(EnumType.STRING)
    private StatoAmicizia stato;

    public Amicizia() {
    }

    public Amicizia(ChiaveAmicizia chiave, Utente utenteInviante, Utente utenteRicevente, Date dataAmicizia, StatoAmicizia stato) {
        this.chiave = chiave;
        this.utenteInviante = utenteInviante;
        this.utenteRicevente = utenteRicevente;
        this.dataAmicizia = dataAmicizia;
        this.stato = stato;
    }

    public ChiaveAmicizia getChiave() {
        return chiave;
    }

    public void setChiave(ChiaveAmicizia chiave) {
        this.chiave = chiave;
    }

    public Utente getUtenteInviante() {
		return utenteInviante;
	}

	public void setUtenteInviante(Utente utenteInviante) {
		this.utenteInviante = utenteInviante;
	}

	public Utente getUtenteRicevente() {
		return utenteRicevente;
	}

	public void setUtenteRicevente(Utente utenteRicevente) {
		this.utenteRicevente = utenteRicevente;
	}

	public Date getDataAmicizia() {
        return dataAmicizia;
    }

    public void setDataAmicizia(Date dataAmicizia) {
        this.dataAmicizia = dataAmicizia;
    }

	public StatoAmicizia getStato() {
		return stato;
	}

	public void setStato(StatoAmicizia stato) {
		this.stato = stato;
	}
    
}
