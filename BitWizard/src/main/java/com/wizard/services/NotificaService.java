package com.wizard.services;

import org.springframework.stereotype.Service;

import com.wizard.entities.Utente;
import com.wizard.entities.Viaggio;

@Service
public interface NotificaService {

	void creaNotifichePerPartecipanti(Viaggio viaggio);
	
	void creaNotifichePerPartecipantiAnnullaViaggio(Viaggio viaggio);

	void creaNotificaBenvenuto(Utente nuovoUtente);

	void creaNotifichePerAnnullamentoIscrizioneViaggio(Viaggio viaggio, Utente utente);

	void creaNotifichePerIscrizioneViaggio(Viaggio viaggio, Utente utente);

	void creaNotifichePerCreazioneViaggio(Viaggio viaggio);

	void creaNotifichePerAggiuntoAmicizia(Viaggio viaggio);

}
