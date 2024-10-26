package com.wizard.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Utente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long utenteId;
	
	private String nome;
	private String cognome;
	
	@Column(unique = true, nullable = false)
	private String email;
	
	private String numeroTelefono;
	private String password;
	
	@Temporal(TemporalType.DATE)
	private Date dataNascita;
	
	private String descrizione;
	
    @OneToOne
    @JoinColumn(name = "immagine_id")
    private Immagine immagine;
    
    @JsonIgnore
    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<UtenteTag> utenteTags = new HashSet<>();
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ruolo_id", nullable = false)
	private Ruolo ruolo;
	
	private int punteggio;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livello_id")
	private Livello livello;
	
	@Column(name = "icona_livello")
	private String iconaLivello;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creato_il")
	private Date creatoIl;
	
	private boolean deleted;
	
	@OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JsonManagedReference
    private List<PartecipantiViaggio> partecipazioni;	
	
	public Long getUtenteId() {
		return utenteId;
	}
	public void setUtenteId(Long utenteId) {
		this.utenteId = utenteId;
	}
	public List<PartecipantiViaggio> getPartecipazioni() {
		return partecipazioni;
	}
	public void setPartecipazioni(List<PartecipantiViaggio> partecipazioni) {
		this.partecipazioni = partecipazioni;
	}
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNumeroTelefono() {
		return numeroTelefono;
	}
	public void setNumeroTelefono(String numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
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
	public Ruolo getRuolo() {
		return ruolo;
	}
	public int getPunteggio() {
		return punteggio;
	}
	public void setPunteggio(int punteggio) {
		this.punteggio = punteggio;
	}
	public Livello getLivello() {
		return livello;
	}
	public String getIconaLivello() {
		return iconaLivello;
	}
	public void setIconaLivello(String iconaLivello) {
		this.iconaLivello = iconaLivello;
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
	public void setRuolo(Ruolo ruolo) {
		this.ruolo = ruolo;
	}
	public void setLivello(Livello livello) {
		this.livello = livello;
	}
	public Immagine getImmagine() {
		return immagine;
	}
	public void setImmagine(Immagine immagine) {
		this.immagine = immagine;
	}
	public Set<UtenteTag> getUtenteTags() {
		return utenteTags;
	}
	public void setUtenteTags(Set<UtenteTag> utenteTags) {
		this.utenteTags = utenteTags;
	}
	
    public void addUtenteTag(UtenteTag utenteTag) {
        this.utenteTags.add(utenteTag);
        utenteTag.setUtente(this);
    }
	
}