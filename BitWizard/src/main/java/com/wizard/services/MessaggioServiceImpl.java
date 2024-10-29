package com.wizard.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wizard.DTO.MessaggioDTO;
import com.wizard.entities.ChiaveMessaggio;
import com.wizard.entities.Messaggio;
import com.wizard.entities.Viaggio;
import com.wizard.repos.MessaggioDAO;

@Service
public class MessaggioServiceImpl implements MessaggioService{
	
	@Autowired
	private MessaggioDAO messaggioDAO;

	@Override
	public Messaggio salvaMessaggio(Messaggio messaggio) {
		
		Long ultimoMessaggioId = messaggioDAO.findUltimoMessaggioId(messaggio.getViaggio().getViaggioId(), messaggio.getUtente().getUtenteId());
		Long nuovoMessaggioId = (ultimoMessaggioId == null) ? 1 : ultimoMessaggioId + 1;
	    
	    // Crea e assegna la chiave composta
	    ChiaveMessaggio chiaveMessaggio = new ChiaveMessaggio();
	    chiaveMessaggio.setUtenteId(messaggio.getUtente().getUtenteId());
	    chiaveMessaggio.setViaggioId(messaggio.getViaggio().getViaggioId());
	    chiaveMessaggio.setMessaggioId(nuovoMessaggioId);
	    System.out.println("Chiave composta creata con Utente ID: " + chiaveMessaggio.getUtenteId() 
	                       + " e Viaggio ID: " + chiaveMessaggio.getViaggioId());
	    
	    messaggio.setChiavemessaggio(chiaveMessaggio);
	    
	    // Salva il messaggio nel database
	    System.out.println("Tentativo di salvataggio del messaggio con testo: " + messaggio.getTesto());
	    Messaggio messaggioSalvato = messaggioDAO.save(messaggio);
	    System.out.println("Messaggio salvato con successo con ID: " + messaggioSalvato.getChiavemessaggio());

	    return messaggioSalvato;
	}

	@Override
	public List<MessaggioDTO> caricaMessaggiViaggio(Viaggio viaggio) {
	    // Recupera la lista dei messaggi associati a un viaggio
	    List<Messaggio> messaggiNonOrdinati = messaggioDAO.findByViaggio(viaggio);
	    
	    List<Messaggio> messaggi = ordinaMessaggiPerDataDecrescente(messaggiNonOrdinati);
	    
	    List<MessaggioDTO> messaggiDTO = new ArrayList<>();
	    
	    // Converte ogni Messaggio in MessaggioDTO
	    for (Messaggio messaggio : messaggi) {
	        MessaggioDTO dto = createDTOFromMessaggio(messaggio);
	        messaggiDTO.add(dto);  // Aggiunge il DTO alla lista
	    }
	    
	    return messaggiDTO;
	}
	
	public List<Messaggio> ordinaMessaggiPerDataDecrescente(List<Messaggio> messaggi) {
	    return messaggi.stream()
	                   .sorted(Comparator.comparing(Messaggio::getData).reversed())
	                   .toList();
	}
	
	private MessaggioDTO createDTOFromMessaggio(Messaggio messaggio) {
	    MessaggioDTO messaggioDTO = new MessaggioDTO();
	    
	    // Imposta l'ID del viaggio associato
	    if (messaggio.getViaggio() != null) {
	        messaggioDTO.setViaggioId(messaggio.getViaggio().getViaggioId());
	    }

	    // Imposta l'ID dell'utente associato
	    if (messaggio.getUtente() != null) {
	        messaggioDTO.setUtenteId(messaggio.getUtente().getUtenteId());
	    }

	    // Imposta il testo del messaggio
	    messaggioDTO.setTesto(messaggio.getTesto());

	    // Imposta la data del messaggio
	    messaggioDTO.setData(messaggio.getData());
	    if (messaggio.getImmagine() != null) {
	    	messaggioDTO.setImmagineId(messaggio.getImmagine().getIdImg());
	    }
	    
	    return messaggioDTO;
	}
	
}
