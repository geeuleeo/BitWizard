package com.wizard.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Stato {
	
	@Id
	@Column(name = "stato_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int statoId;
	
	@Column(name = "tipo_stato", nullable = false)
	private String tipoStato;

	public int getStatoId() {
		return statoId;
	}

	public void setStatoId(int statoId) {
		this.statoId = statoId;
	}

	public String getTipoStato() {
		return tipoStato;
	}

	public void setTipoStato(String tipoStato) {
		this.tipoStato = tipoStato;
	}

}