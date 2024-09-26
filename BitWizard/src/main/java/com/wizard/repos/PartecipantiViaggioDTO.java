package com.wizard.repos;

import java.util.Date;

public class PartecipantiViaggioDTO {
	
    private Long utenteId;
    private Date dataPartecipazione;
    private StatoDTO statoPartecipazione;
    
	public Long getUtenteId() {
		return utenteId;
	}
	public void setUtenteId(Long utenteId) {
		this.utenteId = utenteId;
	}
	public Date getDataPartecipazione() {
		return dataPartecipazione;
	}
	public void setDataPartecipazione(Date dataPartecipazione) {
		this.dataPartecipazione = dataPartecipazione;
	}
	public StatoDTO getStatoPartecipazione() {
		return statoPartecipazione;
	}
	public void setStatoPartecipazione(StatoDTO statoPartecipazione) {
		this.statoPartecipazione = statoPartecipazione;
	}
	
}
