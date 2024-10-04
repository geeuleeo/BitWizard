package com.wizard.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ChiaveRecensione {
	
    @Column(name = "id_viaggio")
    private Long viaggioId;

    @Column(name = "id_utente")
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

}