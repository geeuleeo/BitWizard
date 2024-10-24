package com.wizard.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wizard.DTO.TagDTO;
import com.wizard.DTO.ViaggioCreazioneDTO;
import com.wizard.entities.Agenzia;
import com.wizard.entities.Immagine;
import com.wizard.entities.PartecipantiViaggio;
import com.wizard.entities.Utente;
import com.wizard.entities.Viaggio;
import com.wizard.entities.ViaggioImmagini;
import com.wizard.repos.ImmagineDAO;
import com.wizard.repos.PartecipantiViaggioDAO;
import com.wizard.repos.UtenteDAO;
import com.wizard.repos.ViaggioDAO;
import com.wizard.repos.ViaggioDTO;
import com.wizard.repos.ViaggioImmaginiDAO;
import com.wizard.repos.ViaggioTagDAO;
import com.wizard.services.NotificaService;
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
    
    @Autowired
    private ImmagineDAO immagineDAO;
    
    @Autowired
    private ViaggioImmaginiDAO viaggioImmaginiDAO;
    
    @Autowired
    private ViaggioTagDAO viaggioTagDAO;
    
    @Autowired
    private PartecipantiViaggioDAO partecipantiViaggioDAO;
    
    @Autowired
    private NotificaService notificaService;

    @PostMapping(value = "/crea", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<?> creaViaggio(
            @RequestPart("viaggioDTO") @Valid ViaggioCreazioneDTO viaggioDTO,
            @RequestPart(value = "immagineCopertina", required = false) MultipartFile immagineCopertina,
            @RequestPart(value = "immagini", required = false) List<MultipartFile> immagini,
            HttpSession session) {

        try {
            // Recupera il creatore dalla sessione
            Utente creatore = (Utente) session.getAttribute("utenteLoggato");
            Agenzia agenzia = (Agenzia) session.getAttribute("agenziaLoggata");
            if (creatore == null) {
                if (agenzia == null) {
                    throw new IllegalArgumentException("Agenzia o Utente  non presenti nella sessione");
                }
            }

            // Ottieni la lista di tag dal DTO
            List<TagDTO> tagDTOs = viaggioDTO.getTags();

            // Creazione del viaggio dall'oggetto DTO
            Viaggio nuovoViaggio = createViaggioFromDTO(viaggioDTO);

            if (creatore == null) {
                nuovoViaggio.setAgenziaId(agenzia.getAziendaId());
            }else {
                nuovoViaggio.setCreatoreId(creatore.getUtenteId());
            }

            // Gestione dell'immagine di copertina, se presente
            if (immagineCopertina != null && !immagineCopertina.isEmpty()) {
                handleProfileImage(nuovoViaggio, immagineCopertina);
            }
            // Salva il viaggio con i tag associati
            //Controllo se nella session c'è un'azienda o un utente

            if(agenzia==null){
                Viaggio viaggioSalvato = viaggioService.salvaViaggio(nuovoViaggio, tagDTOs);
                // Gestione delle altre immagini, se presenti
                if (immagini != null && !immagini.isEmpty()) {
                    for (MultipartFile immagine : immagini) {
                        if (!immagine.isEmpty()) {
                            // Crea l'entità Immagine e la salva
                            Immagine immagineEntity = new Immagine();
                            immagineEntity.setImg(immagine.getBytes());
                            immagineDAO.save(immagineEntity);

                            // Crea l'entità ViaggioImmagini per l'associazione
                            ViaggioImmagini viaggioImmagine = new ViaggioImmagini();
                            viaggioImmagine.setViaggio(viaggioSalvato);
                            viaggioImmagine.setImmagine(immagineEntity);

                            // Salva l'associazione nel repository
                            viaggioImmaginiDAO.save(viaggioImmagine);
                        }
                    }
                }
                return new ResponseEntity<>(viaggioSalvato, HttpStatus.CREATED);

            }else {
                Viaggio viaggioSalvato= viaggioService.salvaViaggioAgenzia(nuovoViaggio, tagDTOs);
                // Gestione delle altre immagini, se presenti
                if (immagini != null && !immagini.isEmpty()) {
                    for (MultipartFile immagine : immagini) {
                        if (!immagine.isEmpty()) {
                            // Crea l'entità Immagine e la salva
                            Immagine immagineEntity = new Immagine();
                            immagineEntity.setImg(immagine.getBytes());
                            immagineDAO.save(immagineEntity);

                            // Crea l'entità ViaggioImmagini per l'associazione
                            ViaggioImmagini viaggioImmagine = new ViaggioImmagini();
                            viaggioImmagine.setViaggio(viaggioSalvato);
                            viaggioImmagine.setImmagine(immagineEntity);

                            // Salva l'associazione nel repository
                            viaggioImmaginiDAO.save(viaggioImmagine);
                        }
                    }
                }

                return new ResponseEntity<>(viaggioSalvato, HttpStatus.CREATED);
            }
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
    
    @PutMapping("/modifica/{viaggioId}")
    public ResponseEntity<ViaggioDTO> modificaViaggio(
            @PathVariable Long viaggioId,
            @RequestBody ViaggioCreazioneDTO viaggioDTO) {
        List<TagDTO> tagDTOs = viaggioDTO.getTags();
        ViaggioDTO updatedViaggio = viaggioService.modificaViaggio(viaggioId, viaggioDTO, tagDTOs);
        return ResponseEntity.ok(updatedViaggio);
    }
    
    @GetMapping("/{viaggioId}/immagine")
    public ResponseEntity<byte[]> getImmagineViaggio(@PathVariable Long viaggioId) {
        Viaggio viaggio = viaggioDAO.findById(viaggioId)
            .orElseThrow(() -> new RuntimeException("Viaggio non trovato"));

        if (viaggio.getImmagineCopertina() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        byte[] immagine = viaggio.getImmagineCopertina().getImg();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(immagine, headers, HttpStatus.OK);
    }
    
    @PostMapping("/iscriviti/{viaggioId}")
    public ResponseEntity<?> iscriviUtenteAlViaggio(@PathVariable Long viaggioId, HttpSession session) {
        
        System.out.println("Inizio dell'iscrizione per il viaggio con ID: " + viaggioId);
        
        try {
            // Recupera l'id dell'utente dalla session
        	Utente utente = (Utente) session.getAttribute("utenteLoggato");
        	Long utenteId = utente.getUtenteId();
            if (utenteId == null) {
                System.out.println("Utente non autenticato. Nessun utenteId trovato nella sessione.");
                throw new IllegalStateException("Utente non autenticato");
            } else {
                System.out.println("Utente autenticato con ID: " + utenteId);
            }

            // Recupera il viaggio
            System.out.println("Tentativo di recuperare il viaggio con ID: " + viaggioId);
            Viaggio viaggio = viaggioDAO.findById(viaggioId)
                .orElseThrow(() -> {
                    System.out.println("Viaggio con ID " + viaggioId + " non trovato.");
                    return new IllegalArgumentException("Viaggio non trovato");
                });
            System.out.println("Viaggio trovato: " + viaggio.getNome());

            // Recupera l'utente
            System.out.println("Tentativo di recuperare l'utente con ID: " + utenteId);
            Utente partecipante = utenteDAO.findById(utenteId)
                .orElseThrow(() -> {
                    System.out.println("Utente con ID " + utenteId + " non trovato.");
                    return new IllegalArgumentException("Utente non trovato");
                });
            System.out.println("Utente trovato: " + partecipante.getNome() + " " + partecipante.getCognome());

            // Aggiungi il partecipante al viaggio
            System.out.println("Tentativo di iscrivere l'utente al viaggio...");
            PartecipantiViaggio utenteIscritto = viaggioService.addPartecipanteViaggio(partecipante, viaggio);
            System.out.println("Utente iscritto al viaggio con successo!");
            
            // Restituisci la risposta con i dettagli dell'iscrizione
            return ResponseEntity.ok(utenteIscritto);

        } catch (IllegalArgumentException e) {
            System.out.println("Errore: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            System.out.println("Errore generico durante l'iscrizione al viaggio: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'iscrizione al viaggio");
        }
    }
    
    @DeleteMapping("/iscriviti/rimuovi/{viaggioId}")
    public ResponseEntity<?> rimuoviPartecipante(@PathVariable Long viaggioId, HttpSession session) {
    	
    	 try {
             // Recupera l'id dell'utente dalla session
         	Utente utente = (Utente) session.getAttribute("utenteLoggato");
         	Long utenteId = utente.getUtenteId();
             if (utenteId == null) {
                 System.out.println("Utente non autenticato. Nessun utenteId trovato nella sessione.");
                 throw new IllegalStateException("Utente non autenticato");
             } else {
                 System.out.println("Utente autenticato con ID: " + utenteId);
             }
             
            viaggioService.removePartecipanteViaggio(utenteId, viaggioId);
            
            Viaggio viaggio = viaggioDAO.findById(viaggioId)
                    .orElseThrow(() -> {
                        System.out.println("Viaggio con ID " + viaggioId + " non trovato.");
                        return new IllegalArgumentException("Viaggio non trovato");
                    });
            
            try {
	            notificaService.creaNotifichePerAnnullamentoIscrizioneViaggio(viaggio, utente);
	        } catch (Exception e) {
	            System.err.println("Errore durante la creazione delle notifiche per il viaggio con id " + viaggioId);
	        }
            
            return ResponseEntity.ok("Partecipante rimosso con successo");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @PutMapping("/annulla/{viaggioId}")
    public ResponseEntity<?> annullaViaggio(@PathVariable Long viaggioId) {
        
        System.out.println("Inizio dell'annullamento per il viaggio con ID: " + viaggioId);
        try {
            // Recupera il viaggio
            System.out.println("Tentativo di recuperare il viaggio con ID: " + viaggioId);
            Viaggio viaggio = viaggioDAO.findById(viaggioId)
                .orElseThrow(() -> {
                    System.out.println("Viaggio con ID " + viaggioId + " non trovato.");
                    return new IllegalArgumentException("Viaggio non trovato");
                });
            
            System.out.println("Viaggio trovato: " + viaggio.getNome());
            
            // Imposta il flag di cancellazione e salva le modifiche
            viaggio.setDeleted(true);
            viaggioDAO.save(viaggio);  // Salva la modifica nel database
            
            try {
	            notificaService.creaNotifichePerPartecipantiAnnullaViaggio(viaggio);
	        } catch (Exception e) {
	            System.err.println("Errore durante la creazione delle notifiche per il viaggio con id " + viaggioId);
	        }

            System.out.println("Viaggio con ID " + viaggioId + " annullato con successo.");
            return ResponseEntity.ok("Viaggio annullato con successo");

        } catch (IllegalArgumentException e) {
            System.out.println("Errore: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            System.out.println("Errore generico durante l'annullamento del viaggio: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'annullamento del viaggio");
        }
    }
    
    @GetMapping("/utente")
    public ResponseEntity<List<ViaggioDTO>> getViaggiUtente(HttpSession session) {
        Utente utente = (Utente) session.getAttribute("utenteLoggato");

        if (utente == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<ViaggioDTO> viaggi = viaggioService.findViaggiByUtenteId(utente.getUtenteId());
        return ResponseEntity.ok(viaggi);
    }
    
    @GetMapping("/utente/finito")
    public ResponseEntity<List<ViaggioDTO>> getViaggiUtenteFiniti(HttpSession session) {
        Utente utente = (Utente) session.getAttribute("utenteLoggato");

        if (utente == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // Ottieni la data corrente
        Date dataCorrente = new Date();

        // Recupera i viaggi dell'utente e filtra quelli finiti (data di ritorno passata)
        List<ViaggioDTO> viaggiFiniti = viaggioService.findViaggiByUtenteId(utente.getUtenteId()).stream()
            .filter(viaggio -> viaggio.getDataRitorno().before(dataCorrente)) // Filtra i viaggi la cui data di ritorno è passata
            .collect(Collectors.toList());

        return ResponseEntity.ok(viaggiFiniti);
    }
    
    @GetMapping("/utente/iscritto")
    public ResponseEntity<List<ViaggioDTO>> getViaggiUtenteIscritto(HttpSession session) {
        Utente utente = (Utente) session.getAttribute("utenteLoggato");

        if (utente == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        List<Viaggio> viaggi = viaggioService.trovaViaggiPerUtente(utente.getUtenteId());

     // Mappa i viaggi in ViaggioDTO
        List<ViaggioDTO> viaggiDTO = viaggi.stream()
                                           .map(viaggioService::toDTO)
                                           .collect(Collectors.toList());
        
        return ResponseEntity.ok(viaggiDTO);
    }

    @GetMapping("/creatore")
    public ResponseEntity<List<ViaggioDTO>> getViaggiByCreatoreId(HttpSession session){
    	
    	Utente utente = (Utente) session.getAttribute("utenteLoggato");

        if (utente == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        Long creatoreId = utente.getUtenteId();

        Utente creatore = utenteDAO.findById(creatoreId).orElse(null);
        if (creatore == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

       List<ViaggioDTO> viaggi = viaggioService.findViaggiByCreatore(creatoreId);

        if (viaggi == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
       return ResponseEntity.ok(viaggi);

    }

    @GetMapping("/altroUtente")
    public ResponseEntity<List<ViaggioDTO>> getViaggiAltroUtente(@RequestParam Long utenteId) {

            Utente utente = utenteDAO.findById(utenteId).orElse(null);
            if (utente == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            List<ViaggioDTO> viaggi = viaggioService.findViaggiByUtenteId(utenteId);
            if (viaggi == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(viaggi);


    }

    @GetMapping("lista")
    public ResponseEntity<?> getViaggi() {

    	try{
    		List<ViaggioDTO> allViaggi = viaggioService.getAllViaggi();

    		if (allViaggi.isEmpty()) {
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    					.body("lista non trovata");
    		}
    		return ResponseEntity.ok(allViaggi);
    	}
    	catch (Exception e) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    			.body("Errore interno: " + e.getMessage());
    	}

    }

    @GetMapping("/filtra/tag")
    public ResponseEntity<?> getViaggiByTag(@RequestParam Integer tagId) {
    	
    	try {
                List<ViaggioDTO> viaggi = viaggioService.getViaggiByTag(tagId);
                
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
                List<ViaggioDTO> viaggi = viaggioService.getViaggiByEta(min, max);
                
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
                List<ViaggioDTO> viaggi = viaggioService.getViaggiByDestinazione(destinazione);
                
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
            List<ViaggioDTO> viaggi = viaggioService.getViaggiByPartenza(partenza);

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
            List<ViaggioDTO> viaggi = viaggioService.getViaggiByPrezzo(min,max);

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
        immagineDAO.save(immagine);
        System.out.println("creata immagine con id: " + immagine.getIdImg());
        viaggio.setImmagineCopertina(immagine);
    }
    
}