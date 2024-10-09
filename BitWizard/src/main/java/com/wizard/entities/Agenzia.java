package com.wizard.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Agenzia {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long aziendaId;

	private String nome;
	
	private String descrizione;

	public Long getAziendaId() {
		return aziendaId;
	}
	
	private String password;

	public void setAziendaId(Long aziendaId) {
		this.aziendaId = aziendaId;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNome() {return nome;}


	public void setNome(String nome) {this.nome = nome;}

}
