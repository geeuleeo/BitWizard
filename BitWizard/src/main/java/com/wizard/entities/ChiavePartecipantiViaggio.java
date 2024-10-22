package com.wizard.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ChiavePartecipantiViaggio implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    @Column(name = "viaggio_id")
    private Long viaggioId;

    @Column(name = "utente_id")
    private Long utenteId;
	
	public Long getViaggioId() {
		return viaggioId;
	}
	public void setViaggioId(Long viaggioId) {
		this.viaggioId = viaggioId;
	}
	public Long getUtenteId() {
		return utenteId;
	}
	public void setUtenteId(Long utenteId) {
		this.utenteId = utenteId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}