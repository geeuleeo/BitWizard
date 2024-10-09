package com.wizard.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wizard.entities.Utente;
import com.wizard.entities.Viaggio;
import com.wizard.repos.UtenteDAO;
import com.wizard.repos.ViaggioDAO;
import com.wizard.repos.ViaggioDTO;
import com.wizard.services.ViaggioService;

import jakarta.servlet.http.HttpSession;


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
    
    @PostMapping("/crea")
    public ResponseEntity<?> creaViaggio(@RequestParam ViaggioDTO viaggioDTO, HttpSession session) {
        try {
            // Recupera il creatoreId dalla sessione
            Utente creatore = (Utente) session.getAttribute("utenteLoggato");
            Long creatoreId = (Long) creatore.getUtenteId(); 
            if (creatoreId == null) {
                throw new IllegalArgumentException("Creatore non trovato nella sessione.");
            }

            // Imposta il creatoreId nel viaggioDTO
            viaggioDTO.setCreatoreId(creatoreId);

            // Salva il viaggio
            Viaggio viaggio = viaggioService.salvaViaggio(viaggioDTO);

            // Usa l'ObjectMapper per serializzare l'oggetto Viaggio
            String viaggioJson = objectMapper.writeValueAsString(viaggio);

            return new ResponseEntity<>(viaggioJson, HttpStatus.CREATED);
        } catch (Exception e) {
        	// Modifica per restituire un oggetto JSON invece di una stringa semplice
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Errore nella creazione del viaggio");
            errorResponse.put("message", e.getMessage());
            
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/viaggio/{id}")
    public ResponseEntity<?> caricaPaginaViaggio(@PathVariable Long id, HttpSession session) {
        // Recupera le informazioni del viaggio
        Viaggio viaggio = viaggioDAO.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Viaggio non trovato"));
        
        // Salva l'id del viaggio nella sessione
        session.setAttribute("viaggioId", id);
        
        // Restituisce le informazioni del viaggio
        return ResponseEntity.ok(viaggio);
    }
    
    @PostMapping("/iscriviti")
    public ResponseEntity<?> iscriviUtenteAlViaggio(HttpSession session) {
        try {
            // Recupera l'id del viaggio dalla session
            Long viaggioId = (Long) session.getAttribute("viaggioId");
            if (viaggioId == null) {
                throw new IllegalStateException("Nessun viaggio selezionato");
            }
            
            // Recupera l'id dell'utente dalla session
            Long utenteId = (Long) session.getAttribute("utenteId");
            if (utenteId == null) {
                throw new IllegalStateException("Utente non autenticato");
            }

            // Recupera il viaggio e l'utente dai rispettivi DAO
            Viaggio viaggio = viaggioDAO.findById(viaggioId)
                .orElseThrow(() -> new IllegalArgumentException("Viaggio non trovato"));
            Utente partecipante = utenteDAO.findById(utenteId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
            
            // Chiama il servizio per aggiungere il partecipante al viaggio
            Utente utenteIscritto = viaggioService.addPartecipanteViaggio(partecipante, viaggio);

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


    @GetMapping("/filtra/partenza")
    public ResponseEntity<?> getViaggiByPartenza(@RequestParam String partenza) {
        try {
            // Cerca i viaggi per destinazione (luogoArrivo)
            List<Viaggio> viaggi = viaggioService.getViaggiByPartenza(partenza);

            if (viaggi.isEmpty()) {
                // Restituisce 404 se non ci sono viaggi trovati per la destinazione
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Nessun viaggio trovato per la destinazione: " + partenza);
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

    @GetMapping("/filtra/prezzo")
    public ResponseEntity<?> getViaggiByPrezzo(@RequestParam Integer min, @RequestParam Integer max) {

        try {
            List<Viaggio> viaggi = viaggioService.getViaggiByPrezzo(min,max);

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

    
}