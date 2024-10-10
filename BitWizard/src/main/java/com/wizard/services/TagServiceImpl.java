package com.wizard.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wizard.entities.Tag;
import com.wizard.repos.TagDAO;

@Service
public class TagServiceImpl implements TagService{

    @Autowired
    private TagDAO tagRepository;  // Interfaccia per l'accesso ai dati

    @Override
    public List<Tag> findAll() {
        return tagRepository.findAll();   // Restituisce tutti i tag dal database
    }

    @Override
    public Optional<Tag> findById(Long id) {
        return tagRepository.findById(id); // Cerca un tag per ID
    }

    @Override
    public Tag save(Tag tag) {
        return tagRepository.save(tag);   // Salva o aggiorna un tag
    }

    @Override
    public void deleteById(Long id) {
        tagRepository.deleteById(id);     // Cancella un tag per ID
    }
	
	
}
