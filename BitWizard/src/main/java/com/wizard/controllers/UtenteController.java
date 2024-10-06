package com.wizard.controllers;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wizard.entities.Immagine;
import com.wizard.entities.Recensione;
import com.wizard.entities.Ruolo;
import com.wizard.entities.Utente;
import com.wizard.repos.ImmagineDAO;
import com.wizard.repos.RecensioneDTO;
import com.wizard.repos.RuoloDAO;
import com.wizard.repos.UtenteDTO;
import com.wizard.services.RecensioneService;
import com.wizard.services.UtenteService;


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
    
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UtenteDTO utenteDTO) {
    	System.out.println("Tag IDs ricevuti: " + utenteDTO.getTagIds());
        // 1. Controlla se l'email esiste già
        if (utenteService.existByEmail(utenteDTO.getEmail())) {
            return new ResponseEntity<>("Email già esistente", HttpStatus.CONFLICT);
        }

        // 2. Recupera il ruolo dal database
        Ruolo ruolo = ruoloDAO.findById(utenteDTO.getRuoloId())
                              .orElseThrow(() -> new RuntimeException("Ruolo non trovato"));

        // 3. Crea un nuovo utente
        Utente nuovoUtente = new Utente();
        nuovoUtente.setNome(utenteDTO.getNome());
        nuovoUtente.setCognome(utenteDTO.getCognome());
        nuovoUtente.setNumeroTelefono(utenteDTO.getNumeroTelefono());
        nuovoUtente.setEmail(utenteDTO.getEmail());
        nuovoUtente.setPassword(passwordEncoder.encode(utenteDTO.getPassword()));
        nuovoUtente.setDataNascita(utenteDTO.getDataNascita());
        nuovoUtente.setDescrizione(utenteDTO.getDescrizione());
        nuovoUtente.setRuolo(ruolo);
        nuovoUtente.setDeleted(false);
        nuovoUtente.setCreatoIl(new Date());

        // 4. Gestisci l'immagine se presente
        if (utenteDTO.getImgProfilo() != null && !utenteDTO.getImgProfilo().isEmpty()) {
            try {
                byte[] imgBytes = Base64.getDecoder().decode(utenteDTO.getImgProfilo());
                Immagine immagine = new Immagine();
                immagineDAO.save(immagine);
                nuovoUtente.setImmagine(immagine);
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>("Formato immagine non valido", HttpStatus.BAD_REQUEST);
            }
        }

        // 5. Salva l'utente e associa i tag
        try {
            Utente utenteSalvato = utenteService.salvaUtente(nuovoUtente, utenteDTO.getTagIds());
            System.out.println("Salvataggio UtenteTag: utente_id=" + utenteSalvato.getUtenteId() + ", tag_id=" + utenteSalvato.getUtenteTags());
            return new ResponseEntity<>(utenteSalvato, HttpStatus.CREATED);
        } catch (RuntimeException e) {
        	e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
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
}