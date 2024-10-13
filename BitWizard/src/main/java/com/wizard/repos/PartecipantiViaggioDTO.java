package com.wizard.repos;

import java.util.Date;

public class PartecipantiViaggioDTO {
	
    private Long utenteId;
    private Date dataPartecipazione;
    private StatoDTO statoPartecipazione;
    private String nome;
    private String cognome;
    
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
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	
}
