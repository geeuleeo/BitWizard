package com.wizard.entities;

import java.util.Objects;

import jakarta.persistence.Column;

public class ChiaveAmicizia implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "utente_inviante")
    private Long utenteInviante;
    
    @Column(name = "utente_ricevente")
    private Long utenteRicevente;
    
    public ChiaveAmicizia() {}

    public ChiaveAmicizia(Long utenteInviante, Long utenteRicevente) {
    	this.utenteInviante = utenteInviante;
    	this.utenteRicevente = utenteRicevente;
    }
    
    public Long getUtenteInviante() {
		return utenteInviante;
	}

	public void setUtenteInviante(Long utenteInviante) {
		this.utenteInviante = utenteInviante;
	}

	public Long getUtenteRicevente() {
		return utenteRicevente;
	}

	public void setUtenteRicevente(Long utenteRicevente) {
		this.utenteRicevente = utenteRicevente;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChiaveAmicizia that = (ChiaveAmicizia) o;
        return Objects.equals(utenteInviante, that.utenteInviante) &&
               Objects.equals(utenteRicevente, that.utenteRicevente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(utenteInviante, utenteRicevente);
    }
}
