package com.wizard.services;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wizard.DTO.TagDTO;
import com.wizard.entities.ChiavePartecipantiViaggio;
import com.wizard.entities.PartecipantiViaggio;
import com.wizard.entities.Stato;
import com.wizard.entities.Tag;
import com.wizard.entities.Utente;
import com.wizard.entities.Viaggio;
import com.wizard.entities.ViaggioImmagini;
import com.wizard.entities.ViaggioTag;
import com.wizard.repos.ImmagineDAO;
import com.wizard.repos.PartecipantiViaggioDAO;
import com.wizard.repos.StatoDAO;
import com.wizard.repos.TagDAO;
import com.wizard.repos.UtenteDAO;
import com.wizard.repos.ViaggioDAO;
import com.wizard.repos.ViaggioDTO;
import com.wizard.repos.ViaggioImmaginiDAO;
import com.wizard.repos.ViaggioTagDAO;

import jakarta.transaction.Transactional;

@Service
public class ViaggioServiceImpl implements ViaggioService {
	
	@Autowired
	private ViaggioDAO dao;
	
	@Autowired
	private UtenteDAO utenteDAO;
	
	@Autowired
	private PartecipantiViaggioDAO partecipantiViaggioDAO;
	
	@Autowired
    private ViaggioTagDAO viaggioTagDAO;
	
	@Autowired
	private TagDAO tagDAO;
	
	@Autowired
	private ViaggioImmaginiDAO viaggioImgDAO;
	
	@Autowired
	private ImmagineDAO immagineDAO;
	
	@Autowired
	private StatoDAO statoDAO;
	
	@Transactional
	public Viaggio salvaViaggio(Viaggio viaggio, List<TagDTO> tagDTOs) {
	    
	    System.out.println("Inizio creazione viaggio...");

	    // Imposta lo stato del viaggio (puoi usare uno stato predefinito)
	    Stato stato = statoDAO.findById(1)
	        .orElseThrow(() -> new RuntimeException("Stato non trovato"));
	    viaggio.setStato(stato);
	    
	    if (viaggio.getImmagineCopertina() != null) {
            immagineDAO.save(viaggio.getImmagineCopertina());
        }

	    // Salva il viaggio
	    Viaggio viaggioSalvato;
	    try {
	        viaggioSalvato = dao.save(viaggio);
	        System.out.println("Viaggio salvato con ID: " + viaggioSalvato.getViaggioId());
	    } catch (Exception e) {
	        System.out.println("Errore durante il salvataggio del viaggio: " + e.getMessage());
	        throw e;
	    }

	    // Gestisci i tag
	    if (tagDTOs != null && !tagDTOs.isEmpty()) {
	        for (TagDTO tagDTO : tagDTOs) {
	            Tag tag = tagDAO.findById(tagDTO.getTagId())
	                .orElseGet(() -> {
	                    Tag newTag = new Tag();
	                    newTag.setTipoTag(tagDTO.getTipoTag());
	                    return tagDAO.save(newTag);
	                });

	            ViaggioTag viaggioTag = new ViaggioTag();
	            viaggioTag.setViaggio(viaggioSalvato);
	            viaggioTag.setTag(tag);
	            viaggioTagDAO.save(viaggioTag);
	        }
	    }

	    Optional<Utente> creatoreOptional = utenteDAO.findById(viaggio.getCreatoreId());
	    if (creatoreOptional.isPresent()) {
	        Utente creatore = creatoreOptional.get();

	        // Crea o recupera il partecipante
	        PartecipantiViaggio partecipante = addPartecipanteViaggio(creatore, viaggioSalvato);

	        // Aggiorna la lista dei partecipanti nel viaggio
	        Set<PartecipantiViaggio> partecipanti = new HashSet<>(viaggioSalvato.getPartecipanti());
	        partecipanti.add(partecipante);
	        viaggioSalvato.setPartecipanti(partecipanti);
	        
	        // Aggiorna il viaggio
	        Viaggio risultatoFinale = dao.save(viaggioSalvato);

	        return risultatoFinale;
	    } else {
	        throw new IllegalArgumentException("Creatore con ID " + viaggio.getCreatoreId() + " non trovato.");
	    }
	}
	
	@Override
	public PartecipantiViaggio addPartecipanteViaggio(Utente partecipante, Viaggio viaggio) {

	    // Verifica se il partecipante esiste nel database
	    Optional<Utente> partecipanteOptional = utenteDAO.findById(partecipante.getUtenteId());
	    
	    if (partecipanteOptional.isPresent()) {
	        Utente utente = partecipanteOptional.get();
	        
	        // Crea una nuova chiave composta per il partecipante
	        ChiavePartecipantiViaggio chiavePartecipantiViaggio = new ChiavePartecipantiViaggio();
	        chiavePartecipantiViaggio.setViaggioId(viaggio.getViaggioId());
	        chiavePartecipantiViaggio.setUtenteId(utente.getUtenteId());

	        // Crea un record per la tabella PartecipantiViaggio
	        PartecipantiViaggio partecipanteViaggio = new PartecipantiViaggio();
	        partecipanteViaggio.setId(chiavePartecipantiViaggio);  // Imposta la chiave composta
	        partecipanteViaggio.setViaggio(viaggio);  // Associa il viaggio
	        partecipanteViaggio.setUtente(utente);  // Associa l'utente
	        partecipanteViaggio.setDataIscrizione(new Date());  // Imposta la data di iscrizione

	        // Recupera lo stato di partecipazione predefinito (es. "In attesa")
	        Stato statoPredefinito = statoDAO.findById(1)  // ID 1 rappresenta lo stato predefinito
	            .orElseThrow(() -> new IllegalArgumentException("Stato di partecipazione predefinito non trovato"));

	        // Imposta lo stato di partecipazione
	        partecipanteViaggio.setStatoPartecipazione(statoPredefinito);

	        // Salva il partecipante nella tabella partecipanti_viaggio
	        partecipantiViaggioDAO.save(partecipanteViaggio);

	        // Aggiungi il partecipante alla lista dei partecipanti del viaggio (se necessario)
	        viaggio.addPartecipante(partecipanteViaggio);

	        return partecipanteViaggio;  // Restituisci il partecipante aggiunto
	    } else {
	        throw new IllegalArgumentException("Utente con ID " + partecipante.getUtenteId() + " non trovato.");
	    }
	}
	
    public List<ViaggioDTO> findViaggiByUtenteId(Long utenteId) {
        List<Viaggio> viaggi = dao.findViaggiByPartecipanteId(utenteId);

        return viaggi.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    private ViaggioDTO convertToDTO(Viaggio viaggio) {
        ViaggioDTO dto = new ViaggioDTO();
        dto.setViaggioId(viaggio.getViaggioId());
        dto.setNome(viaggio.getNome());
        dto.setLuogoPartenza(viaggio.getLuogoPartenza());
        dto.setLuogoArrivo(viaggio.getLuogoArrivo());
        dto.setDataPartenza(viaggio.getDataPartenza());
        dto.setDataRitorno(viaggio.getDataRitorno());
        dto.setPrezzo(viaggio.getPrezzo());

        return dto;
    }
	
	private void associaTagEViaggio(List<Long> tagIds, Viaggio viaggio) {
	    if (tagIds != null && !tagIds.isEmpty()) {
	        tagDAO.findAllById(tagIds).forEach(tag -> {
	            ViaggioTag viaggioTag = new ViaggioTag();
	            viaggioTag.setViaggio(viaggio);
	            viaggioTag.setTag(tag);
	            viaggioTagDAO.save(viaggioTag);
	        });
	    }
	}
	
	// ?
	private void associaImmaginiEViaggio(List<Integer> immagineIds, Viaggio viaggio) {
	    if (immagineIds != null && !immagineIds.isEmpty()) {
	        immagineDAO.findAllById(immagineIds).forEach(immagine -> {
	            ViaggioImmagini viaggioImg = new ViaggioImmagini();
	            viaggioImg.setViaggio(viaggio);
	            viaggioImg.setImmagine(immagine);
	            viaggioImgDAO.save(viaggioImg);
	        });
	    }
	}

	@Override
	public List<Viaggio> getViaggiByTag(Integer tagId) {
	    // Recupera tutte le associazioni Viaggio-Tag per il tag specificato
	    List<ViaggioTag> viaggiTag = viaggioTagDAO.findByTagTagId(tagId);
	    
	    // Estrai gli ID dei viaggi da queste associazioni
	    List<Long> viaggiId = viaggiTag.stream()
	                                   .map(vt -> vt.getViaggio().getViaggioId())
	                                   .collect(Collectors.toList());
	    
	    // Usa gli ID per recuperare tutti i Viaggi
	    List<Viaggio> viaggi = dao.findAllById(viaggiId);
	    
	    return viaggi;
	}

	@Override
	public List<Viaggio> getViaggiByEta(Integer min, Integer max) {
	    List<Viaggio> viaggi = dao.findByEtaMinGreaterThanEqualAndEtaMaxLessThanEqual(min, max);
	    
	    return viaggi;
	}

	@Override
	public List<Viaggio> getViaggiByDestinazione(String destinazione) {
	    List<Viaggio> viaggi = dao.findByLuogoArrivoContainingIgnoreCase(destinazione);
	    
	    return viaggi;
	}

	@Override
	public List<Viaggio> getViaggiByPartenza(String partenza) {
		List<Viaggio> viaggi = dao.findByLuogoPartenzaContainingIgnoreCase(partenza);

	return viaggi;

	}

	@Override
	public List<Viaggio> getViaggiByPrezzo(Integer min, Integer max) {
		List<Viaggio> viaggi = dao.findByPrezzoBetween(min,max);

		return viaggi;

	}

}
