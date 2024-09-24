package com.wizard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wizard.entities.Immagine;
import com.wizard.entities.Ruolo;
import com.wizard.entities.Utente;
import com.wizard.repos.ImmagineDAO;
import com.wizard.repos.RuoloDAO;
import com.wizard.repos.UtenteDTO;
import com.wizard.services.UtenteService;


@RestController
@RequestMapping("/api/utente")
public class UtenteController {

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private ImmagineDAO immagineDAO;
    
    @Autowired
    private RuoloDAO ruoloDAO;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UtenteDTO utenteDTO) {

        // 1. Controlla se l'email esiste già
        if (utenteService.existByEmail(utenteDTO.getEmail())) {
            return new ResponseEntity<>("Email già esistente", HttpStatus.CONFLICT);
        }

        // 2. Salva l'immagine nel database
        Immagine immagineSalvata = immagineDAO.save(utenteDTO.getImgProfilo());

        // 3. Recupera il ruolo dal database
        Ruolo ruolo = ruoloDAO.findById(utenteDTO.getRuolo().getRuoloId())
                                     .orElseThrow(() -> new RuntimeException("Ruolo non trovato"));

        // 4. Salva l'utente con il ruolo e gli ID dei tag
        Utente nuovoUtente = utenteService.salvaUtente(
            utenteDTO.getNome(),
            utenteDTO.getCognome(),
            utenteDTO.getNumeroTelefono(),
            utenteDTO.getEmail(),
            utenteDTO.getPassword(),
            utenteDTO.getDataNascita(),
            utenteDTO.getDescrizione(),
            immagineSalvata.getIdImg(),
            utenteDTO.getTagIds(),  // Passa la lista di ID dei tag
            ruolo  // Passa il ruolo recuperato dal database
        );

        return new ResponseEntity<>(nuovoUtente, HttpStatus.CREATED);
    }
    

}