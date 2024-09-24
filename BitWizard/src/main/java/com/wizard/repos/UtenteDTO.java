package com.wizard.repos;

import java.util.Date;
import java.util.List;

import com.wizard.entities.Immagine;
import com.wizard.entities.Ruolo;

public class UtenteDTO {
	
    private String nome;
    private String cognome;
    private String numeroTelefono;
    private String email;
    private String password;
    private Date dataNascita;
    private String descrizione;
    private Immagine imgProfilo;
    private List<Integer> tagIds;
    private Ruolo ruolo;
    
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
	public String getNumeroTelefono() {
		return numeroTelefono;
	}
	public void setNumeroTelefono(String numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getDataNascita() {
		return dataNascita;
	}
	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public Immagine getImgProfilo() {
		return imgProfilo;
	}
	public void setImgProfilo(Immagine imgProfilo) {
		this.imgProfilo = imgProfilo;
	}
	public List<Integer> getTagIds() {
		return tagIds;
	}
	public void setTagIds(List<Integer> tagIds) {
		this.tagIds = tagIds;
	}
	public Ruolo getRuolo() {
		return ruolo;
	}
	public void setRuolo(Ruolo ruolo) {
		this.ruolo = ruolo;
	}

}