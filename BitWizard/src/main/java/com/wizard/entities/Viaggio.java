package com.wizard.entities;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Viaggio {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	@ManyToOne
    @JoinColumn(name = "creatore_id", referencedColumnName = "utenteId")
	private Utente creatore;
	private int etaMax;
	private int etaMin;
	private String stato;
	
	@OneToMany(mappedBy = "viaggio")
    private List<PartecipantiViaggio> partecipanti;
	
	
	public List<PartecipantiViaggio> getPartecipanti() {
		return partecipanti;
	}
	public void setPartecipanti(List<PartecipantiViaggio> partecipanti) {
		this.partecipanti = partecipanti;
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
	public Utente getCreatore() {
		return creatore;
	}
	public void setCreatore(Utente creatore) {
		this.creatore = creatore;
	}
	public int getEtaMax() {
		return etaMax;
	}
	public void setEtaMax(int etaMax) {
		this.etaMax = etaMax;
	}
	public int getEtaMin() {
		return etaMin;
	}
	public void setEtaMin(int etaMin) {
		this.etaMin = etaMin;
	}
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}

}
