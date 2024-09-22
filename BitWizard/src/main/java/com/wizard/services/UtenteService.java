package com.wizard.services;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.wizard.entities.Immagine;
import com.wizard.entities.Ruolo;
import com.wizard.entities.Tag;
import com.wizard.entities.Utente;

@Service
public interface UtenteService {

    List<Utente> getAllUtenti();

	Utente salvaUtente(String nome, String cognome, String numeroTelefono, String email, String password, Date dataNascita, String descrizione, Immagine imgProfilo, List<Tag> tag, Ruolo ruolo);

	boolean existByEmail(String email);

}