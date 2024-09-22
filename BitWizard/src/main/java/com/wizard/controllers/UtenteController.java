package com.wizard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wizard.entities.Utente;
import com.wizard.repos.UtenteDTO;
import com.wizard.services.UtenteService;

@Controller
@RequestMapping("api/utente")
public class UtenteController {
	
    @Autowired
    private UtenteService utenteService;
    
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UtenteDTO utenteDTO) {
        
        if (utenteService.existByEmail(utenteDTO.getEmail())) {
            return new ResponseEntity<>("Email già esistente", HttpStatus.CONFLICT);
        }
        
        Utente nuovoUtente = utenteService.salvaUtente(
            utenteDTO.getNome(),
            utenteDTO.getCognome(),
            utenteDTO.getNumeroTelefono(),
            utenteDTO.getEmail(),
            utenteDTO.getPassword(),
            utenteDTO.getDataNascita(),
            utenteDTO.getDescrizione(),
            utenteDTO.getImgProfilo(),
            utenteDTO.getTag(),
            utenteDTO.getRuolo()
        );
        
        return new ResponseEntity<>(nuovoUtente, HttpStatus.CREATED);
    }
    
}