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
	
	@ManyToOne
    @JoinColumn(name = "immagine_id", referencedColumnName = "id_img", nullable = true)
	private Immagine immagineProfilo;
	
	@ManyToOne
    @JoinColumn(name = "ruolo_id", nullable = false)
	private Ruolo ruolo;
	
	private int punteggio;
	
	@ManyToOne
    @JoinColumn(name = "livello_id")
	private Livello livello;
	
	@Column(name = "icona_livello")
	private String iconaLivello;
	
	@ManyToMany
    @JoinTable(
        name = "tag_utente",
        joinColumns = @JoinColumn(name = "utente_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tag;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creato_il")
	private Date creatoIl;
	
	private boolean deleted;
	
	
	@OneToMany(mappedBy = "utente")
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
	public List<Tag> getTag() {
		return tag;
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
	public void setTag(List<Tag> tag) {
		this.tag = tag;
	}
	public Immagine getImmagineProfilo() {
		return immagineProfilo;
	}
	public void setImmagineProfilo(Immagine immagineProfilo) {
		this.immagineProfilo = immagineProfilo;
	}
	
}