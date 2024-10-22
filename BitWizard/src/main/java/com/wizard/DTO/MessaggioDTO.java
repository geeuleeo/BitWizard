package com.wizard.DTO;

import java.util.Date;

public class MessaggioDTO {
	
    private Long viaggioId;
    private Long utenteId;
    private String testo;
    private Date data;
    private int immagineId;
    
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
	public int getImmagineId() {
		return immagineId;
	}
	public void setImmagineId(int immagineId) {
		this.immagineId = immagineId;
	}

}
