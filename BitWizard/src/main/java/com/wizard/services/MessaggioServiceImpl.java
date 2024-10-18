package com.wizard.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wizard.DTO.MessaggioDTO;
import com.wizard.entities.Messaggio;
import com.wizard.entities.Viaggio;
import com.wizard.repos.MessaggioDAO;

@Service
public class MessaggioServiceImpl implements MessaggioService{
	
	private MessaggioDAO messaggioDAO;

	@Override
	public Messaggio salvaMessaggio(Messaggio messaggio) {
		
		MessaggioDTO messaggioDTO = new MessaggioDTO();
		
		messaggioDTO.setUtenteId(messaggio.getUtente().getUtenteId());
		messaggioDTO.setViaggioId(messaggio.getViaggio().getViaggioId());
		messaggioDTO.setData(messaggio.getData());
		messaggioDTO.setTesto(messaggio.getTesto());
		
		messaggioDAO.save(messaggio);
		
		return messaggio;
	}

	@Override
	public List<MessaggioDTO> caricaMessaggiViaggio(Viaggio viaggio) {

		List<MessaggioDTO> messaggioDTO = messaggioDAO.findByViaggio(viaggio);
		
		return messaggioDTO;
	}


}
