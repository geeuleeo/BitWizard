package com.wizard.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UtenteDTO utenteDTO) {

        // 1. Controlla se l'email esiste già
        if (utenteService.existByEmail(utenteDTO.getEmail())) {
            return new ResponseEntity<>("Email già esistente", HttpStatus.CONFLICT);
        }

        // 2. Salva l'immagine nel database
        Immagine immagineSalvata = null;
        if (utenteDTO.getImgProfilo() != null) {
            immagineSalvata = immagineDAO.save(utenteDTO.getImgProfilo());
        }

        // 3. Recupera il ruolo dal database
        Ruolo ruolo = ruoloDAO.findById(utenteDTO.getRuolo().getRuoloId())
                                     .orElseThrow(() -> new RuntimeException("Ruolo non trovato"));

        // 4. Salva l'utente con il ruolo e gli ID dei tag
        Utente nuovoUtente = utenteService.salvaUtente(
            utenteDTO.getNome(),
            utenteDTO.getCognome(),
            utenteDTO.getNumeroTelefono(),
            utenteDTO.getEmail(),
            utenteDTO.getPassword(),
            utenteDTO.getDataNascita(),
            utenteDTO.getDescrizione(),
            (immagineSalvata != null) ? immagineSalvata.getIdImg() : null,
            utenteDTO.getTagIds(),  // Passa la lista di ID dei tag
            ruolo  // Passa il ruolo recuperato dal database
        );

        return new ResponseEntity<>(nuovoUtente, HttpStatus.CREATED);
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