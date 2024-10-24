package com.wizard.repos;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wizard.DTO.TagDTO;
import com.wizard.customs.StringToLongListDeserializer;
import com.wizard.entities.Utente;

public class ViaggioDTO {
	
	private Long viaggioId;
    private String nome;
    private String luogoPartenza;
    private String luogoArrivo;
    private Date dataPartenza;
    private Date dataRitorno;
    private Date dataScadenza;
    private int numPartMin;
    private int numPartMax;
    private double prezzo;
    private Long creatoreId;
    private int etaMin;
    private int etaMax;
    @JsonDeserialize(using = StringToLongListDeserializer.class)
    private List<TagDTO> tagDTOs;
    private byte[] immagineCopertina;
    private List<byte []> immagini;
    private List<PartecipantiViaggioDTO> partecipanti;
    private String nomeCreatore;
    private String cognomeCreatore;
    private Long idCreatore;
    private byte[] immagineProfiloCreatore;

	private Long agenziaId;
	private String nomeAgenzia;


	public Long getAgenziaId() {
		return agenziaId;
	}

	public void setAgenziaId(Long agenziaId) {
		this.agenziaId = agenziaId;
	}

	public String getNomeAgenzia() {
		return nomeAgenzia;
	}

	public void setNomeAgenzia(String nomeAgenzia) {
		this.nomeAgenzia = nomeAgenzia;
	}

	public String getNomeCreatore() {
		return nomeCreatore;
	}
	public void setNomeCreatore(String nomeCreatore) {
		this.nomeCreatore = nomeCreatore;
	}
	public String getCognomeCreatore() {
		return cognomeCreatore;
	}
	public void setCognomeCreatore(String cognomeCreatore) {
		this.cognomeCreatore = cognomeCreatore;
	}
	public Long getIdCreatore() {
		return idCreatore;
	}
	public void setIdCreatore(Long idCreatore) {
		this.idCreatore = idCreatore;
	}
	public byte[] getImmagineProfiloCreatore() {
		return immagineProfiloCreatore;
	}
	public void setImmagineProfiloCreatore(byte[] immagineProfiloCreatore) {
		this.immagineProfiloCreatore = immagineProfiloCreatore;
	}
	public List<byte[]> getImmagini() {
		return immagini;
	}
	public void setImmagini(List<byte[]> immagini) {
		this.immagini = immagini;
	}
	public Long getViaggioId() {
		return viaggioId;
	}
	public void setViaggioId(Long viaggioId) {
		this.viaggioId = viaggioId;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getLuogoPartenza() {
		return luogoPartenza;
	}
	public void setLuogoPartenza(String luogoPartenza) {
		this.luogoPartenza = luogoPartenza;
	}
	public String getLuogoArrivo() {
		return luogoArrivo;
	}
	public void setLuogoArrivo(String luogoArrivo) {
		this.luogoArrivo = luogoArrivo;
	}
	public Date getDataPartenza() {
		return dataPartenza;
	}
	public void setDataPartenza(Date dataPartenza) {
		this.dataPartenza = dataPartenza;
	}
	public Date getDataRitorno() {
		return dataRitorno;
	}
	public void setDataRitorno(Date dataRitorno) {
		this.dataRitorno = dataRitorno;
	}
	public Date getDataScadenza() {
		return dataScadenza;
	}
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}
	public int getNumPartMin() {
		return numPartMin;
	}
	public void setNumPartMin(int numPartMin) {
		this.numPartMin = numPartMin;
	}
	public int getNumPartMax() {
		return numPartMax;
	}
	public void setNumPartMax(int numPartMax) {
		this.numPartMax = numPartMax;
	}
	public double getPrezzo() {
		return prezzo;
	}
	public void setPrezzo(double prezzo) {
		this.prezzo = prezzo;
	}
	public Long getCreatoreId() {
		return creatoreId;
	}
	public void setCreatoreId(Long creatoreId) {
		this.creatoreId = creatoreId;
	}
	public int getEtaMin() {
		return etaMin;
	}
	public void setEtaMin(int etaMin) {
		this.etaMin = etaMin;
	}
	public int getEtaMax() {
		return etaMax;
	}
	public void setEtaMax(int etaMax) {
		this.etaMax = etaMax;
	}
	public List<PartecipantiViaggioDTO> getPartecipanti() {
		return partecipanti;
	}
	public void setPartecipanti(List<PartecipantiViaggioDTO> partecipanti) {
		this.partecipanti = partecipanti;
	}
	public byte[] getImmagineCopertina() {
		return immagineCopertina;
	}
	public void setImmagineCopertina(byte[] immagineCopertina) {
		this.immagineCopertina = immagineCopertina;
	}
	public List<TagDTO> getTagDTOs() {
		return tagDTOs;
	}
	public void setTagDTOs(List<TagDTO> tagDTOs) {
		this.tagDTOs = tagDTOs;
	}
	

}
