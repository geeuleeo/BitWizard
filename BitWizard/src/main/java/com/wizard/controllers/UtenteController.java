package com.wizard.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wizard.DTO.TagDTO;
import com.wizard.DTO.UtenteMessaggioDTO;
import com.wizard.DTO.UtenteRegistrationDTO;
import com.wizard.entities.Immagine;
import com.wizard.entities.Recensione;
import com.wizard.entities.Ruolo;
import com.wizard.entities.Utente;
import com.wizard.exceptions.EmailAlreadyExistsException;
import com.wizard.exceptions.RuoloNotFoundException;
import com.wizard.repos.ImmagineDAO;
import com.wizard.repos.PartecipantiViaggioDAO;
import com.wizard.repos.RuoloDAO;
import com.wizard.repos.UtenteDAO;
import com.wizard.repos.UtenteDTO;
import com.wizard.services.NotificaService;
import com.wizard.services.RecensioneService;
import com.wizard.services.UtenteService;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/utente")
public class UtenteController {

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private ImmagineDAO immagineDAO;
    
    @Autowired
    private RuoloDAO ruoloDAO;
    
    @Autowired
    private UtenteDAO utenteDAO;
    
    @Autowired
    private RecensioneService recensioneService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private PartecipantiViaggioDAO partecipantiDAO;
    
    @Autowired
    private NotificaService notificaService;
    
    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<?> signUp(
            @RequestPart("utenteDTO") @Valid UtenteRegistrationDTO utenteDTO,
            @RequestPart(value = "imgProfilo", required = false) MultipartFile imgProfilo) {
    	
    	System.out.println(utenteDTO.getTags());

        try {
            if (utenteService.existByEmail(utenteDTO.getEmail())) {
                throw new EmailAlreadyExistsException("Email già esistente");
            }

            Ruolo ruolo = ruoloDAO.findById(1)
                    .orElseThrow(() -> new RuoloNotFoundException("Ruolo non trovato"));

            Utente nuovoUtente = createUtenteFromDTO(utenteDTO, ruolo);

            if (imgProfilo != null && !imgProfilo.isEmpty()) {
                handleProfileImage(nuovoUtente, imgProfilo);
            } else {
                Optional<Immagine> optionalImg = immagineDAO.findById(114);
                if (optionalImg.isPresent()) {
                    Immagine immagine = optionalImg.get();
                    nuovoUtente.setImmagine(immagine);
                } else {
                    // Gestisci il caso in cui l'immagine predefinita non è presente nel database
                    throw new IllegalArgumentException("Immagine predefinita non trovata nel database.");
                }
            }
            
            List<TagDTO> tagDTOs = utenteDTO.getTags();
            
            Utente utenteSalvato = utenteService.salvaUtente(nuovoUtente, tagDTOs);
            
            try {
	            notificaService.creaNotificaBenvenuto(nuovoUtente);
	        } catch (Exception e) {
	            System.err.println("Errore durante la creazione delle notifiche per il l'utente con id " + nuovoUtente.getUtenteId());
	        }
            
            return new ResponseEntity<>(utenteSalvato, HttpStatus.CREATED);

        } catch (EmailAlreadyExistsException | RuoloNotFoundException e) {
            // Restituisci un oggetto JSON di errore
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Restituisci un oggetto JSON di errore generico
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Errore nella creazione dell'utente");
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping(value = "/modifica", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<?> modificaUtente(
            @RequestPart("utenteDTO") @Valid UtenteDTO utenteDTO,
            @RequestPart(value = "imgProfilo", required = false) MultipartFile imgProfilo,
            HttpSession session) {
    	
    	System.out.println(utenteDTO.getTags());

        try {
        	
        	List<TagDTO> tagDTOs = utenteDTO.getTags();
        	
        	Utente utente = (Utente) session.getAttribute("utenteLoggato");

        	UtenteDTO updatedUtente = utenteService.modificaUtente(utente.getUtenteId(), utenteDTO, tagDTOs);
        	
            return ResponseEntity.ok(updatedUtente);

        } catch (EmailAlreadyExistsException | RuoloNotFoundException e) {
            // Restituisci un oggetto JSON di errore
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Restituisci un oggetto JSON di errore generico
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Errore nella creazione dell'utente");
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // metodo per ottenere un'utente dall'id
    @GetMapping("/{utenteId}")
    public ResponseEntity<?> getUtente(@PathVariable Long utenteId) {
        Optional<Utente> optionalUtente = utenteDAO.findById(utenteId) ;
        
        // Verifica se il viaggio è presente
	    if (optionalUtente.isPresent()) {
	        Utente utente = optionalUtente.get();
	        return ResponseEntity.ok(utente);
	    } else {
	        // Gestisci il caso in cui il viaggio non esista
	        throw new RuntimeException("Utente non trovato con ID: " + utenteId);
	    }

    }
    
    @GetMapping("/session")
    public ResponseEntity<Long> getUtenteLoggato(HttpSession session) {
        Utente utente = (Utente) session.getAttribute("utenteLoggato");
        Long utenteId = utente.getUtenteId();
        if (utenteId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(utenteId);
    }
    
    @GetMapping("/nome/{utenteId}")
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

    private Utente createUtenteFromDTO(UtenteRegistrationDTO dto, Ruolo ruolo) {
        Utente utente = new Utente();
        utente.setNome(dto.getNome());
        utente.setCognome(dto.getCognome());
        utente.setNumeroTelefono(dto.getNumeroTelefono());
        utente.setEmail(dto.getEmail());
        utente.setPassword(passwordEncoder.encode(dto.getPassword()));
        utente.setDataNascita(dto.getDataNascita());
        utente.setDescrizione(dto.getDescrizione());
        utente.setRuolo(ruolo);
        utente.setDeleted(false);
        utente.setCreatoIl(new Date());
        return utente;
    }

    private void handleProfileImage(Utente utente, MultipartFile imgProfilo) throws IOException {
        String contentType = imgProfilo.getContentType();
        if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)) {
            throw new IllegalArgumentException("Formato immagine non valido. Sono accettati solo JPEG e PNG.");
        }

        byte[] imgBytes = imgProfilo.getBytes();
        Immagine immagine = new Immagine();
        immagine.setImg(imgBytes);
        utente.setImmagine(immagine);
    }
    
    @GetMapping("/recensione/{utenteId}")
    public List<Recensione> trovaRecensioniPerUtente(@PathVariable Long utenteId) {
        return recensioneService.trovaRecensioniPerUtente(utenteId);
    }

    @GetMapping("/recensione/viaggio/{viaggioId}")
    public List<Recensione> trovaRecensioniPerViaggio(@PathVariable Long viaggioId) {
        return recensioneService.trovaRecensioniPerViaggio(viaggioId);
    }
    
    //rivedere
    @GetMapping("/viaggio/{utenteId}")
    public List<Recensione> trovaRecensioniDeiViaggiCreatiDaUtente(@PathVariable Long creatoreId) {
        return recensioneService.trovaRecensioniDeiViaggiCreatiDaUtente(creatoreId);
    }
    
    /*
    @GetMapping("/top-creatori")
    public List<Object[]> trovaCreatoriConViaggiMigliori() {
        return recensioneService.trovaCreatoriConViaggiMigliori();
    }
    */
    
    @GetMapping("/me")
    public ResponseEntity<UtenteDTO> caricaDatiUtente(HttpSession session) {
    	
        Utente utente = utenteService.getUtente(session);

        UtenteDTO utenteDTO = utenteService.getUtenteDTO(utente.getUtenteId());
        
        return ResponseEntity.ok(utenteDTO);
    }
    
    /*
    @GetMapping("/me/viaggi")
    public ResponseEntity<List<Viaggio>> caricaDatiUtenteViaggi(HttpSession session) {
        
        // Recupera l'utente dalla sessione
    	Utente utente = (Utente) session.getAttribute("utenteLoggato");
        
        // Ottieni la lista delle partecipazioni
        List<PartecipantiViaggio> partecipazioni = utente.getPartecipazioni();
        
        // Crea una lista vuota per raccogliere i viaggi
        List<Viaggio> viaggi = new ArrayList<>();
        
        // Itera sulle partecipazioni e aggiungi i viaggi alla lista
        for (PartecipantiViaggio partecipazione : partecipazioni) {
            Viaggio viaggio = partecipazione.getViaggio();
            viaggi.add(viaggio);
        }
        
        // Restituisci la lista di viaggi come risposta
        return ResponseEntity.ok(viaggi);
    }
    */

}