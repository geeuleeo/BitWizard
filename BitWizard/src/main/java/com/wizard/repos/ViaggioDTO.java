package com.wizard.repos;

import java.util.Date;
import java.util.List;

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
    private Long statoId;
    private List<Integer> tagIds;
    private List<Integer> immagineIds;
    private List<PartecipantiViaggioDTO> partecipanti;
    
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
	public Long getStatoId() {
		return statoId;
	}
	public void setStatoId(Long statoId) {
		this.statoId = statoId;
	}
	public List<Integer> getTagIds() {
		return tagIds;
	}
	public void setTagIds(List<Integer> tagIds) {
		this.tagIds = tagIds;
	}
	public List<Integer> getImmagineIds() {
		return immagineIds;
	}
	public void setImmagineIds(List<Integer> immagineIds) {
		this.immagineIds = immagineIds;
	}
	public List<PartecipantiViaggioDTO> getPartecipanti() {
		return partecipanti;
	}
	public void setPartecipanti(List<PartecipantiViaggioDTO> partecipanti) {
		this.partecipanti = partecipanti;
	}

}
