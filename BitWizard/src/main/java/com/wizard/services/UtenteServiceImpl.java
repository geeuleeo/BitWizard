package com.wizard.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.wizard.DTO.TagDTO;
import com.wizard.entities.Tag;
import com.wizard.entities.Utente;
import com.wizard.entities.UtenteTag;
import com.wizard.repos.ImmagineDAO;
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
    
    @Autowired
	private ImmagineDAO immagineDAO;
    
    public Utente salvaUtente(Utente utente, List<TagDTO> tagDTOs) {
        // Salva l'immagine se presente
        if (utente.getImmagine() != null) {
            immagineDAO.save(utente.getImmagine());
        }

        // Salva l'utente
        Utente utenteSalvato = dao.save(utente);

        // Gestisci i tag
        if (tagDTOs != null && !tagDTOs.isEmpty()) {
            for (TagDTO tagDTO : tagDTOs) {
                Tag tag = tagDAO.findById(tagDTO.getTagId())
                    .orElseGet(() -> {
                        Tag newTag = new Tag();
                        newTag.setTipoTag(tagDTO.getTipoTag());
                        return tagDAO.save(newTag);
                    });

                UtenteTag utenteTag = new UtenteTag();
                utenteTag.setUtente(utenteSalvato);
                utenteTag.setTag(tag);
                utenteTagRepository.save(utenteTag);

                utenteSalvato.getUtenteTags().add(utenteTag);
            }
        }

        return utenteSalvato;
    }

    private Tag findOrCreateTag(TagDTO tagDTO) {
        return tagDAO.findById(tagDTO.getTagId())
                .orElseGet(() -> {
                    Tag newTag = new Tag();
                    newTag.setTipoTag(tagDTO.getTipoTag());
                    return tagDAO.save(newTag);
                });
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