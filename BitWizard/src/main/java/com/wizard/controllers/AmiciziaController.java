package com.wizard.controllers;


import com.wizard.DTO.AmicoDTO;
import com.wizard.entities.Amicizia;
import com.wizard.entities.Amicizia.StatoAmicizia;
import com.wizard.entities.Utente;
import com.wizard.repos.UtenteDAO;
import com.wizard.repos.ViaggioDTO;
import com.wizard.services.AmiciziaService;
import com.wizard.services.UtenteService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("api/amicizia")
public class AmiciziaController {
	
    @Autowired
    AmiciziaService amiciziaService;
    
    @Autowired
    UtenteDAO utenteDAO;
    
    @Autowired
    UtenteService utenteService;
    
    /*
    @PostMapping("crea/{utenteId1}/{utenteId2}")
    public ResponseEntity<Amicizia> creaAmicizia(@PathVariable Long utenteId1,@PathVariable Long utenteId2) {

       Amicizia amicizia =  amiciziaService.creaAmicizia(utenteId1,utenteId2);

       if (amicizia == null) {
           return ResponseEntity.notFound().build();
       }

        return ResponseEntity.ok(amicizia);
    }
    */
    
    @PostMapping("/inviaRichiesta/{utenteTargetId}")
    public ResponseEntity<String> inviaRichiestaAmicizia(
            HttpSession session, 
            @PathVariable Long utenteTargetId) {
    	
    	System.out.println(utenteTargetId);
    	
    	Utente utente = (Utente) session.getAttribute("utenteLoggato");
    	
        try {
            amiciziaService.inviaRichiestaAmicizia(utente.getUtenteId(), utenteTargetId);
            return ResponseEntity.ok("Richiesta di amicizia inviata con successo");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nell'invio della richiesta di amicizia: " + e.getMessage());
        }
    }

    @PostMapping("/accetta/{utenteId}/{notificaId}")
    public ResponseEntity<String> accettaRichiestaAmicizia(@PathVariable Long utenteId, @PathVariable Long notificaId, HttpSession session) {
        Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");
        
        if (utenteLoggato == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utente non loggato");
        }

        amiciziaService.accettaRichiesta(utenteLoggato.getUtenteId(), utenteId, notificaId);
        
        return ResponseEntity.ok("Richiesta d'amicizia accettata");
    }

    @PostMapping("/rifiuta/{utenteId}")
    public ResponseEntity<String> rifiutaRichiestaAmicizia(@PathVariable Long utenteId, @PathVariable Long notificaId, HttpSession session) {
    	
        Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");
        
        if (utenteLoggato == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utente non loggato");
        }

        amiciziaService.rifiutaRichiesta(utenteLoggato.getUtenteId(), utenteId, notificaId);
        
        return ResponseEntity.ok("Richiesta d'amicizia rifiutata");
    }

    @GetMapping("trovaAmici")
    public ResponseEntity<List<AmicoDTO>> trovaAmici(HttpSession session) {

        Utente utente = (Utente) session.getAttribute("utenteLoggato");

        if (utente == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Amicizia> amici = amiciziaService.getAmicizie(utente.getUtenteId());

        if (amici == null || amici.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<AmicoDTO> amiciDTO = amici.stream()
            .filter(amicizia -> amicizia.getStato() == StatoAmicizia.ACCETTATO)  // Filtra solo le amicizie accettate
            .map(amicizia -> {
                Utente amico = amicizia.getUtenteInviante().getUtenteId().equals(utente.getUtenteId()) 
                    ? amicizia.getUtenteRicevente() 
                    : amicizia.getUtenteInviante();
                return amiciziaService.toDTO(amico);  // Converte l'utente in AmicoDTO
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(amiciDTO);
    }
    
    @GetMapping("trovaViaggiConAmici")
    public ResponseEntity<List<ViaggioDTO>> trovaViaggiConAmici(HttpSession session) {

            Utente utente = (Utente) session.getAttribute("utenteLoggato");

            List<ViaggioDTO> viaggiAmici = amiciziaService.getViaggiDegliAmici(utente.getUtenteId());
            if(viaggiAmici == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(viaggiAmici);
    }

}
