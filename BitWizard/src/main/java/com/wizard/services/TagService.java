package com.wizard.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.wizard.entities.Tag;

@Service
public interface TagService {
	
    List<Tag> findAll();                 // Recupera tutti i tag
    Optional<Tag> findById(Long id);     // Trova un tag per ID
    Tag save(Tag tag);                   // Salva un nuovo tag o aggiorna un tag esistente
    void deleteById(Long id); 

}
