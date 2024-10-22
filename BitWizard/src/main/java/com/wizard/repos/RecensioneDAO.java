package com.wizard.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wizard.entities.ChiaveRecensione;
import com.wizard.entities.Recensione;

public interface RecensioneDAO extends JpaRepository<Recensione, ChiaveRecensione> {
	
	List<Recensione> findByIdUtenteId(Long utenteId);
	
	List<Recensione> findByIdViaggioId(Long viaggioId);



}