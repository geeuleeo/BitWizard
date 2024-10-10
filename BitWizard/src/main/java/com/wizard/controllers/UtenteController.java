package com.wizard.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

import jakarta.servlet.http.HttpSession;

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
    public ResponseEntity<?> signUp(
            @RequestParam("nome") String nome,
            @RequestParam("cognome") String cognome,
            @RequestParam("numeroTelefono") String numeroTelefono,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("dataNascita") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dataNascita,
            @RequestParam(value = "imgProfilo", required = false) MultipartFile imgProfilo,  // Modificato per gestire MultipartFile
            @RequestParam("descrizione") String descrizione,
            @RequestParam("ruoloId") int ruoloId,
            @RequestParam(value = "tagIds", required = false) List<Long> tagIds,
            HttpSession session) {

        try {
            // Controlla se l'email esiste già
            if (utenteService.existByEmail(email)) {
                return new ResponseEntity<>("Email già esistente", HttpStatus.CONFLICT);
            }

            // Recupera il ruolo dal database
            Ruolo ruolo = ruoloDAO.findById(ruoloId)
                    .orElseThrow(() -> new RuntimeException("Ruolo non trovato"));

            // Crea un nuovo utente
            Utente nuovoUtente = new Utente();
            nuovoUtente.setNome(nome);
            nuovoUtente.setCognome(cognome);
            nuovoUtente.setNumeroTelefono(numeroTelefono);
            nuovoUtente.setEmail(email);
            nuovoUtente.setPassword(passwordEncoder.encode(password));
            nuovoUtente.setDataNascita(dataNascita);
            nuovoUtente.setDescrizione(descrizione);
            nuovoUtente.setRuolo(ruolo);
            nuovoUtente.setDeleted(false);
            nuovoUtente.setCreatoIl(new Date());

            // Gestione dell'immagine profilo
            if (imgProfilo != null && !imgProfilo.isEmpty()) {
                try {
                    // Controllo sul tipo di file (es. immagini JPEG o PNG)
                    String contentType = imgProfilo.getContentType();
                    if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
                        throw new IllegalArgumentException("Formato immagine non valido. Sono accettati solo JPEG e PNG.");
                    }

                    // Ottiene i byte dell'immagine e salva
                    byte[] imgBytes = imgProfilo.getBytes();
                    Immagine immagine = new Immagine();
                    immagine.setImg(imgBytes);  // Assicurati che la tua classe Immagine abbia questo campo
                    immagineDAO.save(immagine);

                    // Imposta l'immagine sull'utente
                    nuovoUtente.setImmagine(immagine);
                } catch (IOException e) {
                    throw new RuntimeException("Errore durante il caricamento dell'immagine", e);
                }
            }

            // Salva l'utente e associa i tag
            Utente utenteSalvato = utenteService.salvaUtente(nuovoUtente, tagIds);
            return new ResponseEntity<>(utenteSalvato, HttpStatus.CREATED);

        } catch (Exception e) {
            // Restituisce una risposta di errore
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Errore nella creazione dell'utente");
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
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