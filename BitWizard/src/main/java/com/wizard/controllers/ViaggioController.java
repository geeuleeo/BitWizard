package com.wizard.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wizard.DTO.TagDTO;
import com.wizard.DTO.ViaggioCreazioneDTO;
import com.wizard.entities.Immagine;
import com.wizard.entities.PartecipantiViaggio;
import com.wizard.entities.Utente;
import com.wizard.entities.Viaggio;
import com.wizard.repos.UtenteDAO;
import com.wizard.repos.ViaggioDAO;
import com.wizard.services.ViaggioService;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/viaggi")
public class ViaggioController {
	
    @Autowired
    private ViaggioService viaggioService;
    
    @Autowired
    private ViaggioDAO viaggioDAO;
    
    @Autowired
    private UtenteDAO utenteDAO;
    
    @PostMapping(value = "/crea", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<?> creaViaggio(
            @RequestPart("viaggioDTO") @Valid ViaggioCreazioneDTO viaggioDTO,
            @RequestPart(value = "immagineCopertina", required = false) MultipartFile immagineCopertina,
            HttpSession session) {

        try {
            // Recupera il creatore dalla sessione
            Utente creatore = (Utente) session.getAttribute("utenteLoggato");
            if (creatore == null) {
                throw new IllegalArgumentException("Creatore non trovato nella sessione.");
            }

            // Ottieni la lista di tag dal DTO
            List<TagDTO> tagDTOs = viaggioDTO.getTags();

            // Creazione del viaggio dall'oggetto DTO
            Viaggio nuovoViaggio = createViaggioFromDTO(viaggioDTO);
            
            nuovoViaggio.setCreatoreId(creatore.getUtenteId());
            
            // Gestione dell'immagine di copertina, se presente
            if (immagineCopertina != null && !immagineCopertina.isEmpty()) {
                handleProfileImage(nuovoViaggio, immagineCopertina);
            }

            // Salva il viaggio con i tag associati
            Viaggio viaggioSalvato = viaggioService.salvaViaggio(nuovoViaggio, tagDTOs);

            return new ResponseEntity<>(viaggioSalvato, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            // Restituisce una risposta di errore specifico
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Restituisce una risposta di errore generico
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Errore nella creazione del viaggio");
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    private Viaggio createViaggioFromDTO(ViaggioCreazioneDTO viaggioDTO) {
        Viaggio viaggio = new Viaggio();
        viaggio.setNome(viaggioDTO.getNome());
        viaggio.setLuogoPartenza(viaggioDTO.getLuogoPartenza());
        viaggio.setLuogoArrivo(viaggioDTO.getLuogoArrivo());
        viaggio.setDataPartenza(viaggioDTO.getDataPartenza());
        viaggio.setDataRitorno(viaggioDTO.getDataRitorno());
        viaggio.setDataScadenza(viaggioDTO.getDataScadenza());
        viaggio.setNumPartMin(viaggioDTO.getNumPartMin());
        viaggio.setNumPartMax(viaggioDTO.getNumPartMax());
        viaggio.setEtaMin(viaggioDTO.getEtaMin());
        viaggio.setEtaMax(viaggioDTO.getEtaMax());
        viaggio.setPrezzo(viaggioDTO.getPrezzo());
        viaggio.setCreatoIl(new Date());
        return viaggio;
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
            PartecipantiViaggio utenteIscritto = viaggioService.addPartecipanteViaggio(partecipante, viaggio);

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
    
    private void handleProfileImage(Viaggio viaggio, MultipartFile imgProfilo) throws IOException {
        String contentType = imgProfilo.getContentType();
        if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)) {
            throw new IllegalArgumentException("Formato immagine non valido. Sono accettati solo JPEG e PNG.");
        }

        byte[] imgBytes = imgProfilo.getBytes();
        Immagine immagine = new Immagine();
        immagine.setImg(imgBytes);
        viaggio.setImmagineCopertina(immagine);
    }
    
}