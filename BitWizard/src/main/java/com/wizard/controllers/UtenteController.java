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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wizard.DTO.TagDTO;
import com.wizard.DTO.UtenteRegistrationDTO;
import com.wizard.entities.Immagine;
import com.wizard.entities.Recensione;
import com.wizard.entities.Ruolo;
import com.wizard.entities.Utente;
import com.wizard.exceptions.EmailAlreadyExistsException;
import com.wizard.exceptions.RuoloNotFoundException;
import com.wizard.repos.ImmagineDAO;
import com.wizard.repos.PartecipantiViaggioDAO;
import com.wizard.repos.RecensioneDTO;
import com.wizard.repos.RuoloDAO;
import com.wizard.repos.UtenteDTO;
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
    private RecensioneService recensioneService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private PartecipantiViaggioDAO partecipantiDAO;
    
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
            }
            
            List<TagDTO> tagDTOs = utenteDTO.getTags();
            
            Utente utenteSalvato = utenteService.salvaUtente(nuovoUtente, tagDTOs);
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
    
    @GetMapping("/session")
    public ResponseEntity<Long> getUtenteLoggato(HttpSession session) {
        Utente utente = (Utente) session.getAttribute("utenteLoggato");
        Long utenteId = utente.getUtenteId();
        if (utenteId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(utenteId);
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
    
    @PostMapping("/recensione")
    public ResponseEntity<?> creaRecensione(@RequestBody RecensioneDTO recensioneDTO) {
        try {
            Recensione recensione = recensioneService.salvaRecensione(
                recensioneDTO.getViaggioId(),
                recensioneDTO.getUtenteId(),
                recensioneDTO.getTesto(),
                recensioneDTO.getRating()
            );
            return new ResponseEntity<>(recensione, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
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

        // Converte l'utente in un DTO
        UtenteDTO utenteDTO = new UtenteDTO();
        utenteDTO.setDataNascita(utente.getDataNascita());
        utenteDTO.setNumeroTelefono(utente.getNumeroTelefono());
        utenteDTO.setUtenteId(utente.getUtenteId());
        utenteDTO.setNome(utente.getNome());
        utenteDTO.setCognome(utente.getCognome());
        utenteDTO.setEmail(utente.getEmail());
        utenteDTO.setDescrizione(utente.getDescrizione());
        utenteDTO.setImgProfilo(utente.getImmagine().getImg());

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