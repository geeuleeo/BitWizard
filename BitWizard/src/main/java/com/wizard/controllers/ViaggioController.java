package com.wizard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wizard.entities.Utente;
import com.wizard.entities.Viaggio;
import com.wizard.repos.ViaggioDTO;
import com.wizard.services.ViaggioService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/viaggi")
public class ViaggioController {
	
    @Autowired
    private ViaggioService viaggioService;
    
 // HttpSession session
    @PostMapping("/crea")
    public ResponseEntity<?> creaViaggio(@RequestBody ViaggioDTO viaggioDTO) {
    	
    	System.out.println("Tag IDs nel DTO: " + viaggioDTO.getTagIds());
    	
        try {
        	if (viaggioDTO.getCreatoreId() == null) {
                throw new IllegalArgumentException("Creatore mancante nel payload");
            }

            Viaggio viaggio = viaggioService.salvaViaggio(viaggioDTO);

            return new ResponseEntity<>(viaggio, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore nella creazione del viaggio: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
