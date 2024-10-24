package com.wizard.DTO;

import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ViaggioCreazioneDTO {

    @NotBlank(message = "Il nome del viaggio è obbligatorio")
    @Size(min = 2, max = 100, message = "Il nome del viaggio deve essere compreso tra 2 e 100 caratteri")
    private String nome;

    @NotBlank(message = "Il luogo di partenza è obbligatorio")
    @Size(min = 2, max = 100, message = "Il luogo di partenza deve essere compreso tra 2 e 100 caratteri")
    private String luogoPartenza;

    @NotBlank(message = "Il luogo di arrivo è obbligatorio")
    @Size(min = 2, max = 100, message = "Il luogo di arrivo deve essere compreso tra 2 e 100 caratteri")
    private String luogoArrivo;

    @NotNull(message = "La data di partenza è obbligatoria")
    @Future(message = "La data di partenza deve essere nel futuro")
    private Date dataPartenza;

    @NotNull(message = "La data di ritorno è obbligatoria")
    @Future(message = "La data di ritorno deve essere nel futuro")
    private Date dataRitorno;

    @NotNull(message = "La data di scadenza iscrizione è obbligatoria")
    @Future(message = "La data di scadenza deve essere nel futuro")
    private Date dataScadenza;

    @NotNull(message = "Il numero minimo di partecipanti è obbligatorio")
    @Min(value = 1, message = "Il numero minimo di partecipanti deve essere almeno 1")
    private Integer numPartMin;

    @NotNull(message = "Il numero massimo di partecipanti è obbligatorio")
    @Min(value = 1, message = "Il numero massimo di partecipanti deve essere almeno 1")
    private Integer numPartMax;

    @NotNull(message = "Il prezzo è obbligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "Il prezzo deve essere maggiore di 0")
    private Double prezzo;

    @NotNull(message = "L'età minima è obbligatoria")
    @Min(value = 1, message = "L'età minima deve essere almeno 1")
    private Integer etaMin;

    @NotNull(message = "L'età massima è obbligatoria")
    @Min(value = 1, message = "L'età massima deve essere almeno 1")
    private Integer etaMax;

    private List<TagDTO> tags;

    // Getters e setters
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

    public Integer getNumPartMin() {
        return numPartMin;
    }

    public void setNumPartMin(Integer numPartMin) {
        this.numPartMin = numPartMin;
    }

    public Integer getNumPartMax() {
        return numPartMax;
    }

    public void setNumPartMax(Integer numPartMax) {
        this.numPartMax = numPartMax;
    }

    public Double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(Double prezzo) {
        this.prezzo = prezzo;
    }

    public Integer getEtaMin() {
        return etaMin;
    }

    public void setEtaMin(Integer etaMin) {
        this.etaMin = etaMin;
    }

    public Integer getEtaMax() {
        return etaMax;
    }

    public void setEtaMax(Integer etaMax) {
        this.etaMax = etaMax;
    }

	public List<TagDTO> getTags() {
		return tags;
	}

	public void setTags(List<TagDTO> tags) {
		this.tags = tags;
	}

}
