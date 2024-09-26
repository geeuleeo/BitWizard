package com.wizard.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wizard.entities.Utente;
import com.wizard.entities.Viaggio;
import com.wizard.repos.IscrizioneDTO;
import com.wizard.repos.UtenteDAO;
import com.wizard.repos.ViaggioDAO;
import com.wizard.repos.ViaggioDTO;
import com.wizard.services.ViaggioService;


@RestController
@RequestMapping("/api/viaggi")
public class ViaggioController {
	
    @Autowired
    private ViaggioService viaggioService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private ViaggioDAO viaggioDAO;
    
    @Autowired
    private UtenteDAO utenteDAO;
    
 // HttpSession session
    @PostMapping("/crea")
    public ResponseEntity<?> creaViaggio(@RequestBody ViaggioDTO viaggioDTO) {
        try {
            if (viaggioDTO.getCreatoreId() == null) {
                throw new IllegalArgumentException("Creatore mancante nel payload");
            }

            Viaggio viaggio = viaggioService.salvaViaggio(viaggioDTO);

            // Usa l'ObjectMapper per serializzare l'oggetto Viaggio
            String viaggioJson = objectMapper.writeValueAsString(viaggio);

            return new ResponseEntity<>(viaggioJson, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore nella creazione del viaggio: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    
    @PostMapping("/iscriviti")
    public ResponseEntity<?> iscriviUtenteAlViaggio(@RequestBody IscrizioneDTO iscrizioneDTO) {
        try {
            // Recupera l'utente e il viaggio usando i rispettivi ID
            Viaggio viaggio = viaggioDAO.findById(iscrizioneDTO.getViaggioId())
                .orElseThrow(() -> new IllegalArgumentException("Viaggio non trovato"));
            
            Utente partecipante = utenteDAO.findById(iscrizioneDTO.getUtenteId())
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
            
            // Chiama il servizio per aggiungere il partecipante al viaggio
            Utente utenteIscritto = viaggioService.addPartecipanteViaggio(partecipante, viaggio);
            
            // Restituisce una risposta di successo
            return ResponseEntity.ok(utenteIscritto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'iscrizione al viaggio");
        }
    }


    @GetMapping("/filtra/tag")
    public ResponseEntity<?> getViaggiByTag(@RequestParam Integer tagId) {
    	
    	try {
                List<Viaggio> viaggi = viaggioService.getViaggiByTag(tagId);
                
                if (viaggi.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                         .body("Nessun viaggio trovato per il tag ID: " + tagId);
                }
                
                return ResponseEntity.ok(viaggi);

            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                     .body("Parametro tagId non valido: " + e.getMessage());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                     .body("Errore interno: " + e.getMessage());
            }
        }

        @GetMapping("/filtra/eta")
        public ResponseEntity<?> getViaggiByEta(@RequestParam Integer min, @RequestParam Integer max) {
            
        	try {
                List<Viaggio> viaggi = viaggioService.getViaggiByEta(min, max);
                
                if (viaggi.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                         .body("Nessun viaggio trovato per l'intervallo di età: " + min + " - " + max);
                }
                
                return ResponseEntity.ok(viaggi);

            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                     .body("Intervallo di età non valido: " + e.getMessage());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                     .body("Errore interno: " + e.getMessage());
            }
        }
        
        @GetMapping("/filtra/destinazione")
        public ResponseEntity<?> getViaggiByDestinazione(@RequestParam String destinazione) {
            try {
                // Cerca i viaggi per destinazione (luogoArrivo)
                List<Viaggio> viaggi = viaggioService.getViaggiByDestinazione(destinazione);
                
                if (viaggi.isEmpty()) {
                    // Restituisce 404 se non ci sono viaggi trovati per la destinazione
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                         .body("Nessun viaggio trovato per la destinazione: " + destinazione);
                }

                // Restituisce 200 OK con i viaggi trovati
                return ResponseEntity.ok(viaggi);

            } catch (IllegalArgumentException e) {
                // Gestione di errori legati a parametri non validi
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                     .body("Parametro destinazione non valido: " + e.getMessage());
            } catch (Exception e) {
                // Gestione generica di errori imprevisti
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                     .body("Errore interno: " + e.getMessage());
            }
        }

    
}