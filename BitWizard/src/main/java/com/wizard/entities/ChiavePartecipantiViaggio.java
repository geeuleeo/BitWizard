package com.wizard.entities;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class ChiavePartecipantiViaggio implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Long viaggioId;
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
