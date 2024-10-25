package com.wizard.entities;

import java.io.Serializable;
import java.util.Objects;

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
    
    public ChiavePartecipantiViaggio() {}
	
	public ChiavePartecipantiViaggio(Long viaggioId2, Long utenteId2) {
		this.utenteId = utenteId2;
		this.viaggioId = viaggioId2;
	}
	
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
	
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChiavePartecipantiViaggio that = (ChiavePartecipantiViaggio) o;
        return Objects.equals(utenteId, that.utenteId) &&
               Objects.equals(viaggioId, that.viaggioId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(utenteId, viaggioId);
    }

}