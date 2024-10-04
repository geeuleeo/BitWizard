package com.wizard.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wizard.entities.Recensione;

@Service
public interface RecensioneService {
	
	Recensione salvaRecensione(Long viaggioId, Long utenteId, String testo, int rating);
	
	List<Recensione> trovaRecensioniPerUtente(Long utenteId);
	
	List<Recensione> trovaRecensioniPerViaggio(Long viaggioId);

	List<Recensione> trovaRecensioniDeiViaggiCreatiDaUtente(Long creatoreId);

	List<Recensione> trovaRecensioni();

}
