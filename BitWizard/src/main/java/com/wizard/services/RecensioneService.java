package com.wizard.services;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.wizard.entities.Recensione;
import com.wizard.repos.RecensioneDTO;

@Service
public interface RecensioneService {
	
	Recensione salvaRecensione(Long viaggioId, Long utenteId, String testo, int rating, Date date);
	
	List<Recensione> trovaRecensioniPerUtente(Long utenteId);
	
	List<Recensione> trovaRecensioniPerViaggio(Long viaggioId);

	List<Recensione> trovaRecensioniDeiViaggiCreatiDaUtente(Long creatoreId);

	List<Recensione> trovaRecensioni();

	List<RecensioneDTO> caricaRecensioneViaggio(Long viaggioId);

}