package com.wizard.DTO;

import java.util.Date;

public class NotificaDTO {
	
	private Long notificaId;

	private Long utenteId;
	
	private String testo;
	
	private Date data;

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

	public Long getNotificaId() {
		return notificaId;
	}

	public void setNotificaId(Long notificaId) {
		this.notificaId = notificaId;
	}
	
}
