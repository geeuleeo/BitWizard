package com.wizard.controllers;


import com.wizard.DTO.AmicoDTO;
import com.wizard.entities.Amicizia;
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

@Controller
@RequestMapping("api/amicizia")
public class AmiciziaController {
	
    @Autowired
    AmiciziaService amiciziaService;
    
    @Autowired
    UtenteDAO utenteDAO;
    
    @Autowired
    UtenteService utenteService;
    
    @PostMapping("crea/{utenteId1}/{utenteId2}")
    public ResponseEntity<Amicizia> creaAmicizia(@PathVariable Long utenteId1,@PathVariable Long utenteId2) {

       Amicizia amicizia =  amiciziaService.creaAmicizia(utenteId1,utenteId2);

       if (amicizia == null) {
           return ResponseEntity.notFound().build();
       }

        return ResponseEntity.ok(amicizia);
    }

    @PostMapping("/amicizia/accetta/{utenteId}")
    public ResponseEntity<String> accettaRichiestaAmicizia(@PathVariable Long utenteId, HttpSession session) {
    	
    	Utente utente = (Utente) session.getAttribute("utenteLoggato");
    	
        amiciziaService.accettaRichiesta(utente.getUtenteId(), utenteId);
        return ResponseEntity.ok("Richiesta d'amicizia accettata");
    }

    @PostMapping("/amicizia/rifiuta/{utenteId}")
    public ResponseEntity<String> rifiutaRichiestaAmicizia(@PathVariable Long utenteId, HttpSession session) {
    	
    	Utente utente = (Utente) session.getAttribute("utenteLoggato");
    	
        amiciziaService.rifiutaRichiesta(utente.getUtenteId(), utenteId);
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

        List<AmicoDTO> amiciDTO = new ArrayList<>();
        
        for (Amicizia amicizia : amici) {
            // Se utente_id1 è l'utente loggato, prendi l'amico come utente_id2, altrimenti come utente_id1
            Utente amico = amicizia.getUtente_id1().equals(utente.getUtenteId()) 
                ? utenteService.findById(amicizia.getUtente_id2())
                : utenteService.findById(amicizia.getUtente_id1());

            // Converti l'utente amico in DTO
            AmicoDTO amicoDTO = amiciziaService.toDTO(amico);
            amiciDTO.add(amicoDTO);
        }

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
