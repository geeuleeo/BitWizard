package com.wizard.DTO;

import java.util.Date;

public class MessaggioDTO {
	
    private Long viaggioId;
    private Long utenteId;
    private Long immagineId;
    private String testo;
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
	public Long getImmagineId() {
		return immagineId;
	}
	public void setImmagineId(Long immagineId) {
		this.immagineId = immagineId;
	}
	public String getTesto() {
		return testo;
	}
	public void setTesto(String testo) {
		this.testo = testo;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}

}
