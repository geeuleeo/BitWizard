package com.wizard.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wizard.DTO.MessaggioDTO;
import com.wizard.entities.Messaggio;
import com.wizard.entities.Viaggio;

@Service
public interface MessaggioService {
	
	Messaggio salvaMessaggio (Messaggio messaggio);
	
	List<MessaggioDTO> caricaMessaggiViaggio (Viaggio viaggio);

}
