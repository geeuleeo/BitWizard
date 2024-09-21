package com.wizard.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Livello {
	
	@Id
	@Column(name = "icona_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int iconaId;
	
	private String descrizione;
	private String requisiti;
	
	public int getIconaId() {
		return iconaId;
	}
	public void setIconaId(int iconaId) {
		this.iconaId = iconaId;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getRequisiti() {
		return requisiti;
	}
	public void setRequisiti(String requisiti) {
		this.requisiti = requisiti;
	}
	
}