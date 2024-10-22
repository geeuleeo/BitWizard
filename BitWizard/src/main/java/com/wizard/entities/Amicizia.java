package com.wizard.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Amicizia {

    @EmbeddedId
    private ChiaveAmicizia chiave;




    @JoinColumn(name = "utente_id1")
    private Long utente_id1;


    @JoinColumn(name = "utente_id2")
    private Long utente_id2;



    @Temporal(TemporalType.DATE)
    private Date dataAmicizia;

    public Amicizia() {
    }

    public Amicizia(ChiaveAmicizia chiave, Long utente_id1, Long utente_id2, Date dataAmicizia) {
        this.chiave = chiave;
        this.utente_id1 = utente_id1;
        this.utente_id2 = utente_id2;
        this.dataAmicizia = dataAmicizia;
    }

    public ChiaveAmicizia getChiave() {
        return chiave;
    }

    public void setChiave(ChiaveAmicizia chiave) {
        this.chiave = chiave;
    }

    public Long getUtente_id1() {
        return utente_id1;
    }

    public void setUtente_id1(Long utente_id1) {
        this.utente_id1 = utente_id1;
    }

    public Long getUtente_id2() {
        return utente_id2;
    }

    public void setUtente_id2(Long utente_id2) {
        this.utente_id2 = utente_id2;
    }

    public Date getDataAmicizia() {
        return dataAmicizia;
    }

    public void setDataAmicizia(Date dataAmicizia) {
        this.dataAmicizia = dataAmicizia;
    }
}
