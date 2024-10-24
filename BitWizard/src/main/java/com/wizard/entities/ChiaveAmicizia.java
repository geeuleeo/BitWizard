package com.wizard.entities;

import jakarta.persistence.Column;

public class ChiaveAmicizia implements java.io.Serializable {

    private static final long serialVersionUID = 1L;


    @Column(name = "utente_1")
    private Long utenteId1;
    
    @Column(name = "utente_2")
    private Long utenteId2;
    
    public ChiaveAmicizia() {}

    public ChiaveAmicizia(Long utente1, Long utente2) {
    	this.utenteId1 = utente1;
    	this.utenteId2 = utente2;
    }
    
	public Long getUtenteId1() {
        return utenteId1;
    }

    public void setUtenteId1(Long utenteId1) {
        this.utenteId1 = utenteId1;
    }

    public Long getUtenteId2() {
        return utenteId2;
    }

    public void setUtenteId2(Long utenteId2) {
        this.utenteId2 = utenteId2;
    }
}
