package com.wizard.services;

import java.util.List;

import com.wizard.entities.Utente;
import com.wizard.entities.Viaggio;
import com.wizard.repos.ViaggioDTO;

public interface ViaggioService {
	// HttpSession session
	Viaggio salvaViaggio(ViaggioDTO viaggioDTO);
	
	Utente addPartecipanteViaggio(Utente partecipante, Viaggio viaggio);
	
	List<Viaggio> getViaggiByTag (Integer tagId);
	
	List<Viaggio> getViaggiByEta(Integer min, Integer max);
	
	List<Viaggio> getViaggiByDestinazione(String destinazione);
	
}