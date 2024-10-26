package com.wizard.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@FilterDef(name = "deletedFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filters({
    @Filter(name = "deletedFilter", condition = "deleted = :isDeleted")
})
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


	@Column(name = "creatore_id")
    private Long creatoreId;


	@Column(name = "agenzia_id")
	private Long agenziaId;

    @Column(name = "eta_min")
    private int etaMin;

    @Column(name = "eta_max")
    private int etaMax;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stato_id")
    private Stato stato;
    
    @Column(name = "deleted")
    private boolean deleted;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creato_il")
    private Date creatoIl;

    @OneToMany(mappedBy = "viaggio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<PartecipantiViaggio> partecipanti;

    @OneToMany(mappedBy = "viaggio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ViaggioImmagini> immaginiViaggio;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "immagine_id")
    private Immagine immagineCopertina;
	
    @OneToMany(mappedBy = "viaggio", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<ViaggioTag> viaggioTags = new HashSet<>();
	
	public Set<PartecipantiViaggio> getPartecipanti() {
		return partecipanti;
	}
	public void setPartecipanti(Set<PartecipantiViaggio> partecipanti) {
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
	public List<ViaggioImmagini> getImmaginiViaggio() {
		return immaginiViaggio;
	}
	public void setImmaginiViaggio(List<ViaggioImmagini> immaginiViaggio) {
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
    public Immagine getImmagineCopertina() {
		return immagineCopertina;
	}
	public void setImmagineCopertina(Immagine immagineCopertina) {
		this.immagineCopertina = immagineCopertina;
	}
	public Set<ViaggioTag> getViaggioTags() {
		return viaggioTags;
	}
	public void setViaggioTags(Set<ViaggioTag> nuoviTags) {
		this.viaggioTags = nuoviTags;
	}
	public Long getAgenziaId() {
		return agenziaId;
	}

	public void setAgenziaId(Long agenziaId) {
		this.agenziaId = agenziaId;
	}
	
	public void addPartecipante(PartecipantiViaggio partecipante) {
        if (partecipanti == null) {
            partecipanti = new HashSet<PartecipantiViaggio>();
        }
        partecipanti.add(partecipante);
        partecipante.setViaggio(this);
    }
	
}