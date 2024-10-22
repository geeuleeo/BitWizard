package com.wizard.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wizard.DTO.TagDTO;
import com.wizard.entities.Recensione;
import com.wizard.entities.Utente;
import com.wizard.entities.Viaggio;
import com.wizard.repos.PartecipantiViaggioDTO;
import com.wizard.repos.RecensioneDTO;
import com.wizard.repos.UtenteDAO;
import com.wizard.repos.ViaggioDAO;
import com.wizard.repos.ViaggioDTO;
import com.wizard.services.RecensioneService;

import jakarta.servlet.http.HttpSession;

@RestController
public class PaginaViaggioController {
	
	@Autowired
	private ViaggioDAO viaggioDAO;
	
	@Autowired
	private UtenteDAO utenteDAO;
	
	@Autowired
	private RecensioneService recensioneService;
	
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
        viaggioDTO.setCreatoreId(viaggio.getCreatoreId());
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
        viaggioDTO.setCreatoreId(viaggio.getCreatoreId());
        
        Optional<Utente> creatore = utenteDAO.findById(viaggio.getCreatoreId());
        if (creatore.isPresent()) {
            Utente creatoreInfo = creatore.get();
            viaggioDTO.setNomeCreatore(creatoreInfo.getNome());
            viaggioDTO.setCognomeCreatore(creatoreInfo.getCognome());
            if (creatoreInfo.getImmagine() != null) {
                viaggioDTO.setImmagineProfiloCreatore(creatoreInfo.getImmagine().getImg());
            }
        }

        // Immagine copertina
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

        // Mappiamo i tag del viaggio, verificando se la lista non è nulla o vuota
        List<TagDTO> tagDTOs = (viaggio.getViaggioTags() != null && !viaggio.getViaggioTags().isEmpty()) 
            ? viaggio.getViaggioTags().stream()
                .map(viaggioTag -> new TagDTO(viaggioTag.getTag().getTagId(), viaggioTag.getTag().getTipoTag()))
                .collect(Collectors.toList())
            : new ArrayList<>();  // Ritorna una lista vuota se nessun tag è presente
        viaggioDTO.setTagDTOs(tagDTOs);
        
     // Controlliamo e logghiamo la lista di immagini
        if (viaggio.getImmaginiViaggio() != null && !viaggio.getImmaginiViaggio().isEmpty()) {
            System.out.println("Trovate " + viaggio.getImmaginiViaggio().size() + " immagini per il viaggio con ID: " + viaggio.getViaggioId());
            for (int i = 0; i < viaggio.getImmaginiViaggio().size(); i++) {
                byte[] imgData = viaggio.getImmaginiViaggio().get(i).getImmagine().getImg();
                System.out.println("Immagine " + (i+1) + ": lunghezza dati = " + (imgData != null ? imgData.length : 0) + " bytes");
            }
        } else {
            System.out.println("Nessuna immagine trovata per il viaggio con ID: " + viaggio.getViaggioId());
        }

        // Controlliamo e logghiamo la lista di tag
        if (viaggio.getViaggioTags() == null || viaggio.getViaggioTags().isEmpty()) {
            System.out.println("Nessun tag trovato per il viaggio con ID: " + viaggio.getViaggioId());
        } else {
            System.out.println("Trovati " + viaggio.getViaggioTags().size() + " tag per il viaggio con ID: " + viaggio.getViaggioId());
        }
        
        // Mappiamo i partecipanti del viaggio in PartecipantiViaggioDTO
        List<PartecipantiViaggioDTO> partecipantiDTOs = viaggio.getPartecipanti().stream()
            .map(partecipante -> {
                PartecipantiViaggioDTO partecipanteDTO = new PartecipantiViaggioDTO();
                partecipanteDTO.setNome(partecipante.getUtente().getNome());
                partecipanteDTO.setCognome(partecipante.getUtente().getCognome());
                // Aggiungi altri campi necessari per il partecipante
                return partecipanteDTO;
            })
            .collect(Collectors.toList());
        viaggioDTO.setPartecipanti(partecipantiDTOs);

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
	
}
