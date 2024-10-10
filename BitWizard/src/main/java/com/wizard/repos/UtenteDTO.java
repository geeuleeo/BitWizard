package com.wizard.repos;

import java.util.Date;
import java.util.List;

import com.wizard.entities.Tag;

public class UtenteDTO {
	
    private String nome;
    private String cognome;
    private String numeroTelefono;
    private String email;
    private String password;
    private Date dataNascita;
    private String descrizione;
    private byte[] imgProfilo;
    private int ruoloId;
    private List<Tag> tag;
    
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
	public byte[] getImgProfilo() {
		return imgProfilo;
	}
	public void setImgProfilo(byte[] imgProfilo) {
		this.imgProfilo = imgProfilo;
	}
	public int getRuoloId() {
		return ruoloId;
	}
	public void setRuoloId(int ruoloId) {
		this.ruoloId = ruoloId;
	}
	public List<Tag> getTag() {
		return tag;
	}
	public void setTag(List<Tag> tag) {
		this.tag = tag;
	}

}