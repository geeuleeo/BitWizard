package com.wizard.services;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.wizard.entities.*;
import com.wizard.repos.*;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wizard.DTO.TagDTO;

import jakarta.persistence.EntityManager;
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
	
	@Autowired
    private EntityManager entityManager;
    @Autowired
    private AgenziaDAO agenziaDAO;

	public void abilitaFiltroViaggiNonCancellati() {
        // Ottieni la sessione Hibernate e abilita il filtro
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("deletedFilter").setParameter("isDeleted", false);
    }
	
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
			if (viaggioSalvato.getAgenziaId()!=null) {
				System.out.println("agenzia id : " + viaggioSalvato.getAgenziaId());
			}else{
				System.out.println("creatore id :" + viaggioSalvato.getCreatoreId());
			}

	    } catch (Exception e) {
	        System.out.println("Errore durante il salvataggio del viaggio: " + e.getMessage());
	        throw e;
	    }

	    // Gestisci i tag
	    if (tagDTOs != null && !tagDTOs.isEmpty()) {
			System.out.println("Siamo nel controllo tag");
	        for (TagDTO tagDTO : tagDTOs) {
				System.out.println("Siamo nel controllo tag 2");
	            Tag tag = tagDAO.findById(tagDTO.getTagId())
	                .orElseGet(() -> {
						System.out.println("Siamo nel controllo tag 3");
	                    Tag newTag = new Tag();
	                    newTag.setTipoTag(tagDTO.getTipoTag());
	                    return tagDAO.save(newTag);
	                });
				System.out.println("Siamo nel controllo tag 4");
	            ViaggioTag viaggioTag = new ViaggioTag();
	            viaggioTag.setViaggio(viaggioSalvato);
	            viaggioTag.setTag(tag);
	            viaggioTagDAO.save(viaggioTag);
				System.out.println("Siamo nel controllo tag 5");
	        }
	    }
	    
	    Optional<Utente> creatoreOptional = utenteDAO.findById(viaggio.getCreatoreId());
		Optional<Agenzia> agenziaOptional = agenziaDAO.findById(viaggioSalvato.getAgenziaId());
	    if (creatoreOptional.isPresent()) {
			System.out.println("siamo in creatoreOptional");
	    	if (viaggio.getPartecipanti() == null || viaggio.getPartecipanti().isEmpty()) {
	    		Utente creatore = creatoreOptional.get();

	    		// Crea o recupera il partecipante
	    		PartecipantiViaggio partecipante = addPartecipanteViaggio(creatore, viaggioSalvato);

	    		// Aggiorna la lista dei partecipanti nel viaggio
	    		Set<PartecipantiViaggio> partecipanti = new HashSet<>(viaggioSalvato.getPartecipanti());
	    		partecipanti.add(partecipante);
	    		viaggioSalvato.setPartecipanti(partecipanti);
	    	}
	        // Aggiorna il viaggio
	        Viaggio risultatoFinale = dao.save(viaggioSalvato);

	        return risultatoFinale;
	    }else if (agenziaOptional.isPresent()) {
			Set<PartecipantiViaggio> partecipanti = new HashSet<>(viaggioSalvato.getPartecipanti());
			viaggioSalvato.setPartecipanti(partecipanti);
			System.out.println("siamo in agenzia.ispresent");
			Viaggio risultatoFinale = dao.save(viaggioSalvato);
			return risultatoFinale;
		}
		else {
	        throw new IllegalArgumentException("Creatore con ID " + viaggio.getCreatoreId() + " non trovato.");
	    }
	}

	public Viaggio salvaViaggioAgenzia(Viaggio viaggio, List<TagDTO> tagDTOs) {

		Stato stato = statoDAO.findById(1)
				.orElseThrow(() -> new RuntimeException("Stato non trovato"));
		viaggio.setStato(stato);

		if (viaggio.getImmagineCopertina() != null) {
			immagineDAO.save(viaggio.getImmagineCopertina());
		}

		Viaggio viaggioSalvato;
		try {
			viaggioSalvato = dao.save(viaggio);
			System.out.println("Viaggio salvato con ID: " + viaggioSalvato.getViaggioId());
			if (viaggioSalvato.getAgenziaId()!=null) {
				System.out.println("agenzia id : " + viaggioSalvato.getAgenziaId());
			}else{
				System.out.println("creatore id :" + viaggioSalvato.getCreatoreId());
			}

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
		return viaggioSalvato;
	}



	@Transactional
	public Viaggio aggiornaViaggioTags(Long viaggioId, List<TagDTO> nuoviTagDTOs) {
		
	    Viaggio viaggio = dao.findById(viaggioId)
	        .orElseThrow(() -> new IllegalArgumentException("Viaggio non trovato"));

	    Set<ViaggioTag> viaggioTagsEsistenti = viaggio.getViaggioTags();

	    // Identify tags to remove
	    Set<ViaggioTag> tagsToRemove = viaggioTagsEsistenti.stream()
	        .filter(vt -> nuoviTagDTOs.stream().noneMatch(tagDTO -> tagDTO.getTagId().equals(vt.getTag().getTagId())))
	        .collect(Collectors.toSet());

	    viaggioTagsEsistenti.removeAll(tagsToRemove);

	    // Identify tags to add
	    Set<Long> existingTagIds = viaggioTagsEsistenti.stream()
	        .map(vt -> vt.getTag().getTagId())
	        .collect(Collectors.toSet());

	    for (TagDTO tagDTO : nuoviTagDTOs) {
	        if (!existingTagIds.contains(tagDTO.getTagId())) {
	            Tag tag = tagDAO.findById(tagDTO.getTagId())
	                .orElseThrow(() -> new IllegalArgumentException("Tag non trovato"));
	            ViaggioTag nuovoViaggioTag = new ViaggioTag(viaggio, tag);
	            viaggioTagsEsistenti.add(nuovoViaggioTag);
	        }
	    }

	    // No need to set the collection again
	    return dao.save(viaggio);
	}
	
	@Transactional
	public Viaggio salvaEaggiornaViaggio(Viaggio viaggio, List<TagDTO> tagDTOs) {
	    // Salva il viaggio (senza tag)
	    Viaggio viaggioSalvato = salvaViaggio(viaggio, null);
	    
	    // Aggiorna i tag del viaggio
	    aggiornaViaggioTags(viaggioSalvato.getViaggioId(), tagDTOs);
	    
	    return viaggioSalvato;
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

	@Override
	public List<ViaggioDTO> findViaggiByCreatore(Long creatoreId) {
		List<Viaggio> viaggi = dao.findByCreatoreId(creatoreId);

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
	public List<ViaggioDTO> getViaggiByTag(Integer tagId) {
	    // Recupera tutte le associazioni Viaggio-Tag per il tag specificato
	    List<ViaggioTag> viaggiTag = viaggioTagDAO.findByTagTagId(tagId);
	    
	    // Estrai gli ID dei viaggi da queste associazioni
	    List<Long> viaggiId = viaggiTag.stream()
	                                   .map(vt -> vt.getViaggio().getViaggioId())
	                                   .collect(Collectors.toList());
	    
	    // Usa gli ID per recuperare tutti i Viaggi
	    List<Viaggio> viaggi = dao.findAllById(viaggiId);
	    
	    // Converti ciascun Viaggio in ViaggioDTO
	    List<ViaggioDTO> viaggiDTO = viaggi.stream()
	                                       .map(v -> convertToDTO(v))
	                                       .collect(Collectors.toList());
	    
	    return viaggiDTO;
	}

	@Override
	public List<ViaggioDTO> getViaggiByEta(Integer min, Integer max) {
	    List<Viaggio> viaggi = dao.findByEtaMinGreaterThanEqualAndEtaMaxLessThanEqual(min, max);
	    
	 // Converti ciascun Viaggio in ViaggioDTO
	    List<ViaggioDTO> viaggiDTO = viaggi.stream()
	                                       .map(v -> convertToDTO(v))
	                                       .collect(Collectors.toList());
	    
	    return viaggiDTO;
	}

	@Override
	public List<ViaggioDTO> getViaggiByDestinazione(String destinazione) {
	    List<Viaggio> viaggi = dao.findByLuogoArrivoContainingIgnoreCase(destinazione);
	    
	 // Converti ciascun Viaggio in ViaggioDTO
	    List<ViaggioDTO> viaggiDTO = viaggi.stream()
	                                       .map(v -> convertToDTO(v))
	                                       .collect(Collectors.toList());
	    
	    return viaggiDTO;
	}

	@Override
	public List<ViaggioDTO> getViaggiByPartenza(String partenza) {
		List<Viaggio> viaggi = dao.findByLuogoPartenzaContainingIgnoreCase(partenza);
		
		// Converti ciascun Viaggio in ViaggioDTO
	    List<ViaggioDTO> viaggiDTO = viaggi.stream()
	                                       .map(v -> convertToDTO(v))
	                                       .collect(Collectors.toList());

	return viaggiDTO;

	}

	@Override
	public List<ViaggioDTO> getViaggiByPrezzo(Integer min, Integer max) {
		List<Viaggio> viaggi = dao.findByPrezzoBetween(min,max);
		
		// Converti ciascun Viaggio in ViaggioDTO
	    List<ViaggioDTO> viaggiDTO = viaggi.stream()
	                                       .map(v -> convertToDTO(v))
	                                       .collect(Collectors.toList());

		return viaggiDTO;

	}

	@Override
	public List<ViaggioDTO> getAllViaggi() {
		List<Viaggio> viaggi = dao.findAll();
		
		return viaggi.stream()
	            .map(this::convertToDTO)
	            .collect(Collectors.toList());
	}

}
