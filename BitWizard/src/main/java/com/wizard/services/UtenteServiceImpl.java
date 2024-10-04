package com.wizard.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.wizard.entities.Ruolo;
import com.wizard.entities.Tag;
import com.wizard.entities.Utente;
import com.wizard.entities.UtenteTag;
import com.wizard.repos.TagDAO;
import com.wizard.repos.UtenteDAO;
import com.wizard.repos.UtenteTagDAO;

import jakarta.servlet.http.HttpSession;

@Service
public class UtenteServiceImpl implements UtenteService {
	
	@Autowired
    private UtenteDAO dao;
	
	@Autowired
    private UtenteTagDAO utenteTagDAO;
	
	@Autowired
	private TagDAO tagDAO;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Override
    public Utente salvaUtente(String nome, String cognome, String numeroTelefono, String email, String password,
                              Date dataNascita, String descrizione, int immagineId, List<Integer> tagIds, Ruolo ruolo) {
        
        Utente u = new Utente();
        
        u.setNome(nome);
        u.setCognome(cognome);
        u.setEmail(email);
        u.setNumeroTelefono(numeroTelefono);
        u.setDataNascita(dataNascita);
        u.setDescrizione(descrizione);
        u.setDeleted(false);
        u.setCreatoIl(new Date());  // Imposta la data di creazione
        u.setRuolo(ruolo);
        u.setImmagineId(immagineId);
        
        u.setPassword(passwordEncoder.encode(password));  // Codifica la password

        // 1. Salva l'utente senza i tag per ora
        Utente utenteSalvato = dao.save(u);
        System.out.println("Utente salvato con ID: " + utenteSalvato.getUtenteId());

        if (tagIds != null && !tagIds.isEmpty()) {
            for (Integer tagId : tagIds) {
                Optional<Tag> tag = tagDAO.findById(tagId);
                if (tag.isPresent()) {
                    UtenteTag utenteTag = new UtenteTag();
                    utenteTag.setUtente(utenteSalvato); // Associa l'utente
                    utenteTag.setTag(tag.get()); // Associa il tag

                    // Salva l'associazione nella tabella utente_tag
                    utenteTagDAO.save(utenteTag);
                } else {
                    System.out.println("Tag con ID " + tagId + " non trovato.");
                }
            }
        }
        
        return utenteSalvato;
    }
	
	@Override
    public boolean existByEmail(String email) {
        return dao.findByEmail(email).isPresent();
    }

    @Override
    public List<Utente> getAllUtenti() {
        return dao.findAll();
    }

    @Override
    public Utente getUtente(HttpSession session) {
        // Recupera l'ID utente dalla sessione
        Object utenteIdObj = session.getAttribute("utenteId");
        
        if (utenteIdObj == null) {
            throw new IllegalStateException("Nessun utente loggato.");
        }

        Long utenteId = (Long) utenteIdObj;
        
        return dao.findById(utenteId)
                  .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + utenteId));
    }

	
}