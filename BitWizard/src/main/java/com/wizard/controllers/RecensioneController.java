package com.wizard.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wizard.entities.Recensione;
import com.wizard.entities.Utente;
import com.wizard.entities.Viaggio;
import com.wizard.repos.RecensioneDTO;
import com.wizard.repos.ViaggioDAO;
import com.wizard.services.RecensioneService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/recensione")
public class RecensioneController {
	
	@Autowired
	private RecensioneService recensioneService;
	
	@Autowired
	private ViaggioDAO viaggioDAO;
	
    
    @PostMapping("/crea")
    public ResponseEntity<?> creaRecensione(@RequestBody RecensioneDTO recensioneDTO, HttpSession session) {
        
    	Utente creatore = (Utente) session.getAttribute("utenteLoggato");
    	
    	System.out.println(recensioneDTO.getTesto());
    	
    	try {
            Recensione recensione = new Recensione();
            
            recensioneDTO.setData(new Date()); 
            		
            recensione = recensioneService.salvaRecensione(
                recensioneDTO.getViaggioId(),
                creatore.getUtenteId(),
                recensioneDTO.getTesto(),
                recensioneDTO.getRating(),
                recensioneDTO.getData()
            );
            return new ResponseEntity<>(recensione, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
	@GetMapping("/viaggio/{viaggioId}")
	public ResponseEntity<?> caricaMessaggi(@PathVariable Long viaggioId, HttpSession session) {
		
		System.out.println("inizio caricamento recensioni");
	    
	    Optional<Viaggio> optionalViaggio = viaggioDAO.findById(viaggioId);
	    
	    if (optionalViaggio.isPresent()) {
	    	
	    	System.out.println("caricamento delle recensioni");
	    	
	        // Viaggio trovato
	        Viaggio viaggio = optionalViaggio.get();
	        
	        List<RecensioneDTO> recensioneDTO = recensioneService.caricaRecensioneViaggio(viaggioId);
	        
	        System.out.println(recensioneDTO);
	        
	        // Restituisci la lista di messaggi come risposta
	        return ResponseEntity.ok(recensioneDTO);
	        
	    } else {
	        // Viaggio non trovato, restituisce un errore 404
	        Map<String, String> errorResponse = new HashMap<>();
	        errorResponse.put("error", "Viaggio non trovato con ID: " + viaggioId);
	        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	    }
	    
	}

}
