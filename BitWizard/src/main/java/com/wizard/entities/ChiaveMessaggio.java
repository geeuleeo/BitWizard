package com.wizard.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

public class ChiaveMessaggio implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
    @Column(name = "viaggio_id")
    private Long viaggioId;

    @Column(name = "utente_id")
    private Long utenteId;
    
    @Column(name = "messaggio_id")
    private Long messaggioId;

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

	public Long getMessaggioId() {
		return messaggioId;
	}

	public void setMessaggioId(Long messaggioId) {
		this.messaggioId = messaggioId;
	}
	
}
