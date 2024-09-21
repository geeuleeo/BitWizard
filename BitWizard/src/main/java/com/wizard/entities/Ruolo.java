package com.wizard.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Ruolo {
	
	@Id
	@Column(name = "ruolo_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ruoloId;
	
	private String descrizione;

	public int getRuoloId() {
		return ruoloId;
	}

	public void setRuoloId(int ruoloId) {
		this.ruoloId = ruoloId;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

}
