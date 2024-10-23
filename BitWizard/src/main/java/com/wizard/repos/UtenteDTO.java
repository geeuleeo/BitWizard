package com.wizard.repos;

import java.util.Date;
import java.util.List;

import com.wizard.DTO.TagDTO;

public class UtenteDTO {
	
	private Long utenteId;
    private String nome;
    private String cognome;
    private String numeroTelefono;
    private String email;
    private Date dataNascita;
    private String descrizione;
    private byte[] imgProfilo;
    private int ruoloId;
    private List<TagDTO> tags;
    
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
	public List<TagDTO> getTags() {
		return tags;
	}
	public void setTags(List<TagDTO> tags) {
		this.tags = tags;
	}
	public Long getUtenteId() {
		return utenteId;
	}
	public void setUtenteId(Long utenteId) {
		this.utenteId = utenteId;
	}

}