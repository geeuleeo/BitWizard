package com.wizard.services;

import java.util.List;

import com.wizard.DTO.TagDTO;
import com.wizard.entities.PartecipantiViaggio;
import com.wizard.entities.Utente;
import com.wizard.entities.Viaggio;
import com.wizard.repos.ViaggioDTO;

public interface ViaggioService {

	Viaggio salvaViaggio(Viaggio nuovoViaggio, List<TagDTO> tagDTOs);
	
	PartecipantiViaggio addPartecipanteViaggio(Utente partecipante, Viaggio viaggio);
	
	List<Viaggio> getViaggiByTag (Integer tagId);
	
	List<Viaggio> getViaggiByEta(Integer min, Integer max);
	
	List<Viaggio> getViaggiByDestinazione(String destinazione);

	List<Viaggio> getViaggiByPartenza(String partenza);

	List<Viaggio> getViaggiByPrezzo(Integer min, Integer max);
	
	List<ViaggioDTO> findViaggiByUtenteId(Long utenteId);
	
}