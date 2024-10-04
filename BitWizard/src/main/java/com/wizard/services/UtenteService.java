package com.wizard.services;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.wizard.entities.Ruolo;
import com.wizard.entities.Utente;

import jakarta.servlet.http.HttpSession;

@Service
public interface UtenteService {

    List<Utente> getAllUtenti();

	Utente salvaUtente(String nome, String cognome, String numeroTelefono, String email, String password, Date dataNascita, String descrizione, int immagineId, List<Integer> tag, Ruolo ruolo);

	boolean existByEmail(String email);

	Utente getUtente(HttpSession session);

}