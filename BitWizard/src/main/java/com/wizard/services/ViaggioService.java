package com.wizard.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wizard.DTO.TagDTO;
import com.wizard.entities.PartecipantiViaggio;
import com.wizard.entities.Utente;
import com.wizard.entities.Viaggio;
import com.wizard.repos.ViaggioDTO;

@Service
public interface ViaggioService {

	Viaggio salvaViaggio(Viaggio nuovoViaggio, List<TagDTO> tagDTOs);
	
	PartecipantiViaggio addPartecipanteViaggio(Utente partecipante, Viaggio viaggio);
	
	List<ViaggioDTO> getViaggiByTag (Integer tagId);
	
	List<ViaggioDTO> getViaggiByEta(Integer min, Integer max);
	
	List<ViaggioDTO> getViaggiByDestinazione(String destinazione);

	List<ViaggioDTO> getViaggiByPartenza(String partenza);

	List<ViaggioDTO> getViaggiByPrezzo(Integer min, Integer max);

	List<ViaggioDTO> findViaggiByUtenteId(Long utenteId);
	
	List<ViaggioDTO> getAllViaggi();
	
}