package com.wizard.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wizard.DTO.MessaggioDTO;
import com.wizard.DTO.UtenteMessaggioDTO;
import com.wizard.entities.Immagine;
import com.wizard.entities.Messaggio;
import com.wizard.entities.Utente;
import com.wizard.entities.Viaggio;
import com.wizard.repos.ImmagineDAO;
import com.wizard.repos.MessaggioDAO;
import com.wizard.repos.UtenteDAO;
import com.wizard.repos.ViaggioDAO;
import com.wizard.services.MessaggioService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@RestController
public class MessaggioController {
	
	@Autowired
	MessaggioService messaggioService;
	
	@Autowired
	ImmagineDAO immagineDAO; 
	
	@Autowired
	ViaggioDAO viaggioDAO;
	
	@Autowired
	UtenteDAO utenteDAO;
	
	@Autowired
	private MessaggioDAO messaggioDAO;
	
	@PostMapping("/crea/messaggio")
	public ResponseEntity<?> inviaMessaggio(@RequestPart("viaggioDTO") @Valid MessaggioDTO messaggioDTO,
	                                       @RequestPart(value = "immagineMessaggio", required = false) MultipartFile immagineMessaggio,
	                                       HttpSession session) {
	    
	    try {
	        // Recupera il creatore dalla sessione
	        Utente creatore = (Utente) session.getAttribute("utenteLoggato");
	        if (creatore == null) {
	            return new ResponseEntity<>("Creatore non trovato nella sessione.", HttpStatus.UNAUTHORIZED);
	        }

	        // Creazione del messaggio dall'oggetto DTO
	        Messaggio nuovoMessaggio = createMessaggioFromDTO(messaggioDTO);
	        nuovoMessaggio.setUtente(creatore);

	        // Gestione dell'immagine di copertina, se presente
	        if (immagineMessaggio != null && !immagineMessaggio.isEmpty()) {
	            handleImage(nuovoMessaggio, immagineMessaggio);
	        }

	        // Salva il messaggio
	        Messaggio messaggioSalvato = messaggioService.salvaMessaggio(nuovoMessaggio);

	        return new ResponseEntity<>(messaggioSalvato, HttpStatus.CREATED);

	    } catch (IllegalArgumentException e) {
	        // Restituisce una risposta di errore specifico
	        Map<String, String> errorResponse = new HashMap<>();
	        errorResponse.put("error", e.getMessage());
	        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	    } catch (Exception e) {
	        // Restituisce una risposta di errore generico
	        Map<String, String> errorResponse = new HashMap<>();
	        errorResponse.put("error", "Errore nella creazione del messaggio");
	        errorResponse.put("message", e.getMessage());
	        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@GetMapping("/carica/messaggi/{viaggioId}")
	public ResponseEntity<?> caricaMessaggi(@PathVariable Long viaggioId, HttpSession session) {
	    
	    Optional<Viaggio> optionalViaggio = viaggioDAO.findById(viaggioId);
	    
	    if (optionalViaggio.isPresent()) {
	        // Viaggio trovato
	        Viaggio viaggio = optionalViaggio.get();
	        List<MessaggioDTO> messaggiDTO = messaggioService.caricaMessaggiViaggio(viaggio);
	        
	        // Restituisci la lista di messaggi come risposta
	        return ResponseEntity.ok(messaggiDTO);
	    } else {
	        // Viaggio non trovato, restituisce un errore 404
	        Map<String, String> errorResponse = new HashMap<>();
	        errorResponse.put("error", "Viaggio non trovato con ID: " + viaggioId);
	        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	    }
	}
	
    @GetMapping("/messaggio/utente/immagine")
    public ResponseEntity<byte[]> getImmagineViaggio(HttpSession session) {
    	
    	Utente utente = (Utente) session.getAttribute("utenteLoggato");
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato nella sessione.");
        }

        if (utente.getImmagine() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        byte[] immagine = utente.getImmagine().getImg();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(immagine, headers, HttpStatus.OK);
    }
    
    @GetMapping("/messaggio/utente/nome/{utenteId}")
    public ResponseEntity<?> getNomeUtente(@PathVariable Long utenteId) {
        Optional<Utente> optionalUtente = utenteDAO.findById(utenteId);
        
        if (optionalUtente.isPresent()) {
            Utente utente = optionalUtente.get();
            UtenteMessaggioDTO utenteMessaggioDTO = new UtenteMessaggioDTO();
            utenteMessaggioDTO.setNome(utente.getNome());
            utenteMessaggioDTO.setCognome(utente.getCognome());
            return ResponseEntity.ok(utenteMessaggioDTO);
        } else {
            // Restituisci un errore 404 con un messaggio chiaro
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Utente non trovato con ID: " + utenteId);
        }
    }
	
	private Messaggio createMessaggioFromDTO(MessaggioDTO messaggioDTO) {
	    Messaggio messaggio = new Messaggio();
	    
	    // Recupero del Viaggio tramite l'ID dal DTO
	    Optional<Viaggio> optionalViaggio = viaggioDAO.findById(messaggioDTO.getViaggioId());
	    
	    // Verifica se il viaggio è presente
	    if (optionalViaggio.isPresent()) {
	        Viaggio viaggio = optionalViaggio.get();  // Estrae l'oggetto Viaggio dall'Optional
	        messaggio.setViaggio(viaggio);  // Imposta il viaggio nel messaggio
	    } else {
	        // Gestisci il caso in cui il viaggio non esista
	        throw new RuntimeException("Viaggio non trovato con ID: " + messaggioDTO.getViaggioId());
	    }
	    
	 // Recupero del Viaggio tramite l'ID dal DTO
	    Optional<Utente> optionalUtente = utenteDAO.findById(messaggioDTO.getUtenteId());
	    
	    // Verifica se il viaggio è presente
	    if (optionalUtente.isPresent()) {
	        Utente utente = optionalUtente.get();  // Estrae l'oggetto Viaggio dall'Optional
	        messaggio.setUtente(utente);  // Imposta il viaggio nel messaggio
	    } else {
	        // Gestisci il caso in cui il viaggio non esista
	        throw new RuntimeException("Utente non trovato con ID: " + messaggioDTO.getUtenteId());
	    }
	    
	    // Imposta altri campi del messaggio (ad esempio testo, data, utente, immagine, ecc.)
	    messaggio.setTesto(messaggioDTO.getTesto());
	    messaggio.setData(new Date());  // Imposta la data corrente o prendi dal DTO se disponibile
	    
	    return messaggio;
	}
	
    private void handleImage(Messaggio messaggio, MultipartFile imgMessaggio) throws IOException {
        String contentType = imgMessaggio.getContentType();
        if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)) {
            throw new IllegalArgumentException("Formato immagine non valido. Sono accettati solo JPEG e PNG.");
        }

        byte[] imgBytes = imgMessaggio.getBytes();
        Immagine immagine = new Immagine();
        immagine.setImg(imgBytes);
        immagineDAO.save(immagine);
        System.out.println("creata immagine con id: " + immagine.getIdImg());
        messaggio.setImmagine(immagine);
    }

}
