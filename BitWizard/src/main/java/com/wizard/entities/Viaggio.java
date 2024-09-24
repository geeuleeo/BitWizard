package com.wizard.entities;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Viaggio {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long viaggioId;

    private String nome;

    @Column(name = "luogo_partenza")
    private String luogoPartenza;

    @Column(name = "luogo_arrivo")
    private String luogoArrivo;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_partenza")
    private Date dataPartenza;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_ritorno")
    private Date dataRitorno;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_scadenza")
    private Date dataScadenza;

    @Column(name = "num_part_min")
    private int numPartMin;

    @Column(name = "num_part_max")
    private int numPartMax;

    private double prezzo;

    @Column(name = "creatore_id", nullable = false)
    private Long creatoreId;

    @Column(name = "eta_min")
    private int etaMin;

    @Column(name = "eta_max")
    private int etaMax;

    @ManyToOne
    @JoinColumn(name = "stato_id")
    private Stato stato;

    private boolean deleted;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creato_il")
    private Date creatoIl;

    @OneToMany(mappedBy = "viaggio")
    private List<PartecipantiViaggio> partecipanti;

    @ManyToMany
    @JoinTable(
        name = "immagini_viaggio",
        joinColumns = @JoinColumn(name = "viaggio_id"),
        inverseJoinColumns = @JoinColumn(name = "id_img")
    )
    private List<Immagine> immaginiViaggio;
	
	
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
	public Stato getStato() {
		return stato;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public Date getCreatoIl() {
		return creatoIl;
	}
	public void setCreatoIl(Date creatoIl) {
		this.creatoIl = creatoIl;
	}
	public List<Immagine> getImmaginiViaggio() {
		return immaginiViaggio;
	}
	public void setImmaginiViaggio(List<Immagine> immaginiViaggio) {
		this.immaginiViaggio = immaginiViaggio;
	}
	public void setStato(Stato stato) {
		this.stato = stato;
	}
	public Long getCreatoreId() {
		return creatoreId;
	}
	public void setCreatoreId(Long creatoreId) {
		this.creatoreId = creatoreId;
	}
	
}