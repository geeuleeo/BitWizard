package com.wizard.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.wizard.entities.Utente;

public interface UtenteService {

    List<Utente> getAllUtenti();
    
	public Utente loggaUtente(String username,String password);

	// public Utente modificaUtente(String username, String newPassword);

	Utente authenticate(String username, String password);

	Utente salvaUtente(String nome, String cognome, String email, String password);

	Set<String> getUtenti();

	Optional<Utente> getUtente(Long id);

}