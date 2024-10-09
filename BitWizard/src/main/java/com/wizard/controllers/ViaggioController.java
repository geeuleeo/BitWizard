package com.wizard.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<?> creaViaggio(
            @RequestParam("nome") String nome,
            @RequestParam("luogoPartenza") String luogoPartenza,
            @RequestParam("luogoArrivo") String luogoArrivo,
            @RequestParam("dataPartenza") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dataPartenza,
            @RequestParam("dataRitorno") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dataRitorno,
            @RequestParam("numPartMin") int numPartMin,
            @RequestParam("numPartMax") int numPartMax,
            @RequestParam("prezzo") double prezzo,
            @RequestParam(value = "immagineCopertina", required = false) MultipartFile immagineCopertina,
            HttpSession session) {
        
        try {
            // Recupera il creatore dalla sessione
            Utente creatore = (Utente) session.getAttribute("utenteLoggato");
            if (creatore == null) {
                throw new IllegalArgumentException("Creatore non trovato nella sessione.");
            }

            ViaggioDTO viaggioDTO = new ViaggioDTO();
            viaggioDTO.setNome(nome);
            viaggioDTO.setLuogoPartenza(luogoPartenza);
            viaggioDTO.setLuogoArrivo(luogoArrivo);
            viaggioDTO.setDataPartenza(dataPartenza);
            viaggioDTO.setDataRitorno(dataRitorno);
            viaggioDTO.setNumPartMin(numPartMin);
            viaggioDTO.setNumPartMax(numPartMax);
            viaggioDTO.setPrezzo(prezzo);
            viaggioDTO.setCreatoreId(creatore.getUtenteId());

            // Se è presente un'immagine, converti l'immagine in byte[]
            if (immagineCopertina != null && !immagineCopertina.isEmpty()) {
                viaggioDTO.setImmagineCopertina(immagineCopertina.getBytes());
            }

            // Salva il viaggio
            Viaggio viaggio = viaggioService.salvaViaggio(viaggioDTO);
            return new ResponseEntity<>(viaggio, HttpStatus.CREATED);
        } catch (Exception e) {
            // Restituisce una risposta di errore
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