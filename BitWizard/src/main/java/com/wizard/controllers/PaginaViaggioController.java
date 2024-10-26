package com.wizard.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.wizard.DTO.TagDTO;
import com.wizard.entities.Agenzia;
import com.wizard.entities.Utente;
import com.wizard.entities.Viaggio;
import com.wizard.repos.AgenziaDAO;
import com.wizard.repos.PartecipantiViaggioDTO;
import com.wizard.repos.UtenteDAO;
import com.wizard.repos.ViaggioDAO;
import com.wizard.repos.ViaggioDTO;

import jakarta.servlet.http.HttpSession;

@RestController
public class PaginaViaggioController {
	
	@Autowired
	private ViaggioDAO viaggioDAO;
	
	@Autowired
	private UtenteDAO utenteDAO;

    @Autowired
    private AgenziaDAO agenziaDAO;
	
	/*
	@GetMapping("/api/session/viaggio")
	public ResponseEntity<Long> getViaggioIdFromSession(HttpSession session) {
	    Long viaggioId = (Long) session.getAttribute("viaggioId");
	    if (viaggioId == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }
	    return ResponseEntity.ok(viaggioId);
	}
	*/

    @GetMapping("/viaggio/{id}")
    public ViaggioDTO caricaPaginaViaggio(@PathVariable Long id, HttpSession session) {
        System.out.println("Caricamento dettagli per il viaggio con ID: " + id);

        if (id == null) {
            throw new IllegalArgumentException("ID del viaggio non può essere nullo");
        }

        // Recupera le informazioni del viaggio
        Viaggio viaggio = viaggioDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Viaggio non trovato"));

        // Salva l'id del viaggio nella sessione
        session.setAttribute("viaggioId", id);

        // Crea un DTO per il viaggio
        ViaggioDTO viaggioDTO = new ViaggioDTO();
        viaggioDTO.setViaggioId(viaggio.getViaggioId());
        viaggioDTO.setDataPartenza(viaggio.getDataPartenza());
        viaggioDTO.setDataRitorno(viaggio.getDataRitorno());
        viaggioDTO.setDataScadenza(viaggio.getDataScadenza());
        viaggioDTO.setEtaMax(viaggio.getEtaMax());
        viaggioDTO.setEtaMin(viaggio.getEtaMin());
        viaggioDTO.setLuogoArrivo(viaggio.getLuogoArrivo());
        viaggioDTO.setLuogoPartenza(viaggio.getLuogoPartenza());
        viaggioDTO.setNome(viaggio.getNome());
        viaggioDTO.setNumPartMax(viaggio.getNumPartMax());
        viaggioDTO.setNumPartMin(viaggio.getNumPartMin());
        viaggioDTO.setPrezzo(viaggio.getPrezzo());

        // Controlla se esiste un creatoreId o un agenziaId
        if (viaggio.getCreatoreId() != null) {
            Optional<Utente> creatore = utenteDAO.findById(viaggio.getCreatoreId());
            if (creatore.isPresent()) {
                Utente creatoreInfo = creatore.get();
                viaggioDTO.setNomeCreatore(creatoreInfo.getNome());
                viaggioDTO.setCognomeCreatore(creatoreInfo.getCognome());
                if (creatoreInfo.getImmagine() != null) {
                    viaggioDTO.setImmagineProfiloCreatore(creatoreInfo.getImmagine().getImg());
                }
            }
            viaggioDTO.setCreatoreId(viaggio.getCreatoreId()); // Imposta creatoreId
        } else if (viaggio.getAgenziaId() != null) {
            Optional<Agenzia> agenzia = agenziaDAO.findById(viaggio.getAgenziaId());
            if (agenzia.isPresent()) {
                Agenzia agenziaInfo = agenzia.get();
                viaggioDTO.setNomeAgenzia(agenziaInfo.getNome()); // Aggiungi nome agenzia al DTO
                // Aggiungi altri dettagli dell'agenzia se necessario
            }
            viaggioDTO.setAgenziaId(viaggio.getAgenziaId()); // Imposta agenziaId
        }

        // Immagine copertina e altre logiche rimangono invariate
        if (viaggio.getImmagineCopertina() != null) {
            viaggioDTO.setImmagineCopertina(viaggio.getImmagineCopertina().getImg());
        }

        // Mappiamo le immagini del viaggio in byte[], verificando se la lista non è nulla o vuota
        List<byte[]> immaginiViaggio = (viaggio.getImmaginiViaggio() != null && !viaggio.getImmaginiViaggio().isEmpty())
                ? viaggio.getImmaginiViaggio().stream()
                .map(viaggioImmagine -> viaggioImmagine.getImmagine().getImg())
                .collect(Collectors.toList())
                : new ArrayList<>();  // Ritorna una lista vuota se nessuna immagine è presente

        viaggioDTO.setImmagini(immaginiViaggio);
        
        List<PartecipantiViaggioDTO> partecipantiViaggioDTOs = (viaggio.getPartecipanti() != null && !viaggio.getPartecipanti().isEmpty())
        	    ? viaggio.getPartecipanti().stream()
        	        .map(partecipante -> {
        	            PartecipantiViaggioDTO partecipanteDTO = new PartecipantiViaggioDTO();
        	            partecipanteDTO.setNome(partecipante.getUtente().getNome());
        	            partecipanteDTO.setCognome(partecipante.getUtente().getCognome());
        	            partecipanteDTO.setUtenteId(partecipante.getUtente().getUtenteId());
        	            return partecipanteDTO;
        	        })
        	        .collect(Collectors.toList())  // Cambiato in toList per restituire una List
        	    : new ArrayList<>();
        	viaggioDTO.setPartecipanti(partecipantiViaggioDTOs);

        // Mappiamo i tag del viaggio, verificando se la lista non è nulla o vuota
        List<TagDTO> tagDTOs = (viaggio.getViaggioTags() != null && !viaggio.getViaggioTags().isEmpty())
                ? viaggio.getViaggioTags().stream()
                .map(viaggioTag -> new TagDTO(viaggioTag.getTag().getTagId(), viaggioTag.getTag().getTipoTag()))
                .collect(Collectors.toList())
                : new ArrayList<>();  // Ritorna una lista vuota se nessun tag è presente
        viaggioDTO.setTagDTOs(tagDTOs);

        // Restituisce le informazioni del viaggio
        return viaggioDTO;
    }
    
    @GetMapping("/viaggio/{viaggioId}/partecipanti")
    public ResponseEntity<List<PartecipantiViaggioDTO>> getPartecipantiViaggio(@PathVariable Long viaggioId) {
        Viaggio viaggio = viaggioDAO.findById(viaggioId)
            .orElseThrow(() -> new IllegalArgumentException("Viaggio non trovato"));

        List<PartecipantiViaggioDTO> partecipantiDTOs = viaggio.getPartecipanti().stream()
                .map(partecipante -> {
                    PartecipantiViaggioDTO partecipanteDTO = new PartecipantiViaggioDTO();
                    partecipanteDTO.setNome(partecipante.getUtente().getNome());
                    partecipanteDTO.setCognome(partecipante.getUtente().getCognome());
                    partecipanteDTO.setUtenteId(partecipante.getUtente().getUtenteId());
                    // Aggiungi altri campi necessari per il partecipante
                    return partecipanteDTO;
                })
                .collect(Collectors.toList());
        
        System.out.println("Numero di partecipanti trovati: " + partecipantiDTOs.size());

        return ResponseEntity.ok(partecipantiDTOs);
    }
    
    @GetMapping("/viaggio/completo/{id}")
    public ResponseEntity<Boolean> controlloDisponibilitaViaggio(@PathVariable Long id) {
        
        System.out.println("Caricamento dettagli per il viaggio con ID: " + id);
        
        if (id == null) {
            throw new IllegalArgumentException("ID del viaggio non può essere nullo");
        }
        
        // Recupera le informazioni del viaggio
        Viaggio viaggio = viaggioDAO.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Viaggio non trovato"));
        
        // Verifica se il viaggio ha raggiunto il numero massimo di partecipanti
        boolean completo = viaggio.getPartecipanti().size() >= viaggio.getNumPartMax();
        
        // Restituisci lo stato di disponibilità
        return ResponseEntity.ok(completo);
    }
    
    @GetMapping("/viaggio/terminato/{viaggioId}")
    public ResponseEntity<Boolean> getViaggioTerminato(@PathVariable("viaggioId") Long viaggioId) {
        
        // Recupera le informazioni del viaggio
        Viaggio viaggio = viaggioDAO.findById(viaggioId)
            .orElseThrow(() -> new IllegalArgumentException("Viaggio non trovato"));
        
        Date dataFine = viaggio.getDataRitorno();
        Date dataCorrente = new Date(); // Ottieni la data corrente
        
        // Verifica se il viaggio è terminato confrontando le date
        boolean terminato = dataFine.before(dataCorrente);
        
        // Restituisci lo stato del viaggio
        return ResponseEntity.ok(terminato);
    }
	
}
