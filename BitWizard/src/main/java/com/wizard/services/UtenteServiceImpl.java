package com.wizard.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.wizard.entities.Tag;
import com.wizard.entities.Utente;
import com.wizard.entities.UtenteTag;
import com.wizard.repos.TagDAO;
import com.wizard.repos.UtenteDAO;
import com.wizard.repos.UtenteTagDAO;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

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
    
    @Autowired
    private UtenteTagDAO utenteTagRepository;
    
    @Transactional
    public Utente salvaUtente(Utente utente, List<Tag> tag) {
    	
        System.out.println("Numero di UtenteTag nell'utente: " + utente.getUtenteTags().size());
        for (UtenteTag utenteTag : utente.getUtenteTags()) {
            System.out.println("UtenteTag: utente=" + utenteTag.getUtente() + ", tag=" + utenteTag.getTag());
        }
        
              // Salva l'utente nel database
        Utente utenteSalvato = dao.save(utente);
        if (utenteSalvato.getUtenteId() == null) {
            throw new RuntimeException("Errore nel salvataggio dell'utente");
        }

        // Gestisci le associazioni con i tag
        if (tag != null && !tag.isEmpty()) {

            // Crea e salva le associazioni
            for (Tag tags : tag) {
            	
                if (tag == null) {
                    throw new RuntimeException("Tag nullo trovato nella lista dei tag recuperati.");
                }
                UtenteTag utenteTag = new UtenteTag();
                utenteTag.setUtente(utenteSalvato);
                utenteTag.setTag(tags);

                // Aggiungi l'associazione alle liste
                utenteSalvato.getUtenteTags().add(utenteTag);
                tags.getUtenteTags().add(utenteTag);

                // Salva l'associazione
                utenteTagRepository.save(utenteTag);
            }
            
        }

        return utenteSalvato;
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utente utente = dao.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));
        
        return new User(utente.getEmail(), utente.getPassword(), new ArrayList<>());
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
    
    public void aggiungiTagAUtente(Utente utente, List<Tag> tags) {
        for (Tag tag : tags) {
            UtenteTag utenteTag = new UtenteTag();
            utenteTag.setUtente(utente);
            utenteTag.setTag(tag);

            utente.getUtenteTags().add(utenteTag);
            utenteTagRepository.save(utenteTag);
        }
    }
    
    public void rimuoviTagDaUtente(Utente utente, Tag tag) {
        UtenteTag utenteTag = utenteTagRepository.findByUtenteAndTag(utente, tag);
        if (utenteTag != null) {
            utente.getUtenteTags().remove(utenteTag);
            utenteTagRepository.delete(utenteTag);
        }
    }

}