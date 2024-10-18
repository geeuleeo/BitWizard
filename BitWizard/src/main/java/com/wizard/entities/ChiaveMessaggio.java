package com.wizard.entities;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;

public class ChiaveMessaggio implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
    @Column(name = "viaggio_id")
    private Long viaggioId;

    @Column(name = "utente_id")
    private Long utenteId;
    
    @Column(name = "immagine_id")
    private int immagineId;
    
    @Column(name = "utente_id")
    private Date data;

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

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

}
