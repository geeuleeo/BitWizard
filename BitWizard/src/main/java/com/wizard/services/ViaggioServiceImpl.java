package com.wizard.services;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wizard.DTO.TagDTO;
import com.wizard.DTO.ViaggioCreazioneDTO;
import com.wizard.entities.Agenzia;
import com.wizard.entities.ChiavePartecipantiViaggio;
import com.wizard.entities.PartecipantiViaggio;
import com.wizard.entities.Stato;
import com.wizard.entities.Tag;
import com.wizard.entities.Utente;
import com.wizard.entities.Viaggio;
import com.wizard.entities.ViaggioImmagini;
import com.wizard.entities.ViaggioTag;
import com.wizard.repos.AgenziaDAO;
import com.wizard.repos.ImmagineDAO;
import com.wizard.repos.PartecipantiViaggioDAO;
import com.wizard.repos.StatoDAO;
import com.wizard.repos.TagDAO;
import com.wizard.repos.UtenteDAO;
import com.wizard.repos.ViaggioDAO;
import com.wizard.repos.ViaggioDTO;
import com.wizard.repos.ViaggioImmaginiDAO;
import com.wizard.repos.ViaggioTagDAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
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
    
    @Autowired
    private NotificaService notificaService;

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
				try {
				    Tag tag = tagDAO.findById(tagDTO.getTagId()).orElseGet(() -> {
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
	        } catch (Exception e) {
				System.out.println("Errore durante la gestione del tag: " + tagDTO.getTagId());
			    throw new RuntimeException("Errore durante il salvataggio del tag", e);
			}
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
	    
	        try {
	            notificaService.creaNotifichePerCreazioneViaggio(viaggio);
	        } catch (Exception e) {
	            System.err.println("Errore durante la creazione delle notifiche per il viaggio con id " + risultatoFinale.getViaggioId());
	        }

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
	    public ViaggioDTO modificaViaggio(Long viaggioId, ViaggioCreazioneDTO viaggioDTO, List<TagDTO> tagDTOs) {
	        
	        Viaggio viaggio = dao.findById(viaggioId)
	            .orElseThrow(() -> new EntityNotFoundException("Viaggio non trovato con id " + viaggioId));

	        
	        viaggio.setNome(viaggioDTO.getNome());
	        viaggio.setLuogoPartenza(viaggioDTO.getLuogoPartenza());
	        viaggio.setLuogoArrivo(viaggioDTO.getLuogoArrivo());
	        viaggio.setDataPartenza(viaggioDTO.getDataPartenza());
	        viaggio.setDataRitorno(viaggioDTO.getDataRitorno());
	        viaggio.setDataScadenza(viaggioDTO.getDataScadenza());
	        viaggio.setNumPartMin(viaggioDTO.getNumPartMin());
	        viaggio.setNumPartMax(viaggioDTO.getNumPartMax());
	        viaggio.setPrezzo(viaggioDTO.getPrezzo());
	        viaggio.setEtaMin(viaggioDTO.getEtaMin());
	        viaggio.setEtaMax(viaggioDTO.getEtaMax());

	        aggiornaViaggioTags(viaggio, tagDTOs);

	        dao.save(viaggio);
	        
	        try {
	            notificaService.creaNotifichePerPartecipanti(viaggio);
	        } catch (Exception e) {
	            System.err.println("Errore durante la creazione delle notifiche per il viaggio con id " + viaggioId);
	        }

	        return toDTO(viaggio);
	    }

	    private void aggiornaViaggioTags(Viaggio viaggio, List<TagDTO> nuoviTagDTOs) {
	        Set<ViaggioTag> viaggioTagsEsistenti = viaggio.getViaggioTags();

	        // Identify tags to remove
	        Set<ViaggioTag> tagsToRemove = viaggioTagsEsistenti.stream()
	            .filter(vt -> nuoviTagDTOs.stream()
	                .noneMatch(tagDTO -> tagDTO.getTagId().equals(vt.getTag().getTagId())))
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
	    }
	    
	    @Override
		public ViaggioDTO toDTO(Viaggio viaggio) {
	        ViaggioDTO viaggioDTO = new ViaggioDTO();

	        viaggioDTO.setViaggioId(viaggio.getViaggioId());
	        viaggioDTO.setNome(viaggio.getNome());
	        viaggioDTO.setLuogoPartenza(viaggio.getLuogoPartenza());
	        viaggioDTO.setLuogoArrivo(viaggio.getLuogoArrivo());
	        viaggioDTO.setDataPartenza(viaggio.getDataPartenza());
	        viaggioDTO.setDataRitorno(viaggio.getDataRitorno());
	        viaggioDTO.setDataScadenza(viaggio.getDataScadenza());
	        viaggioDTO.setNumPartMin(viaggio.getNumPartMin());
	        viaggioDTO.setNumPartMax(viaggio.getNumPartMax());
	        viaggioDTO.setPrezzo(viaggio.getPrezzo());
	        viaggioDTO.setEtaMin(viaggio.getEtaMin());
	        viaggioDTO.setEtaMax(viaggio.getEtaMax());
	        // Map other fields as necessary

	        // Map tags
	        List<TagDTO> tagDTOs = viaggio.getViaggioTags().stream()
	            .map(viaggioTag -> new TagDTO(
	                viaggioTag.getTag().getTagId(),
	                viaggioTag.getTag().getTipoTag()
	            ))
	            .collect(Collectors.toList());
	        viaggioDTO.setTagDTOs(tagDTOs);

	        return viaggioDTO;
	    }

	@Transactional
	public PartecipantiViaggio addPartecipanteViaggio(Utente partecipante, Viaggio viaggio) {

		// Verifica se il partecipante esiste già per questo viaggio
		ChiavePartecipantiViaggio chiavePartecipantiViaggio = new ChiavePartecipantiViaggio();
		chiavePartecipantiViaggio.setViaggioId(viaggio.getViaggioId());
		chiavePartecipantiViaggio.setUtenteId(partecipante.getUtenteId());

		// Cerca l'esistenza del record nella tabella partecipanti_viaggio
		Optional<PartecipantiViaggio> partecipanteEsistente = partecipantiViaggioDAO.findById(chiavePartecipantiViaggio);

		if (partecipanteEsistente.isPresent()) {
			// Se esiste già, ritorna il partecipante esistente
			return partecipanteEsistente.get();
		} else {
			// Crea un nuovo record per la tabella partecipanti_viaggio
			PartecipantiViaggio partecipanteViaggio = new PartecipantiViaggio();
			partecipanteViaggio.setId(chiavePartecipantiViaggio);  // Imposta la chiave composta
			partecipanteViaggio.setViaggio(viaggio);  // Associa il viaggio
			partecipanteViaggio.setUtente(partecipante);  // Associa l'utente
			partecipanteViaggio.setDataIscrizione(new Date());  // Imposta la data di iscrizione

			// Recupera lo stato di partecipazione predefinito
			Stato statoPredefinito = statoDAO.findById(1)
					.orElseThrow(() -> new IllegalArgumentException("Stato di partecipazione predefinito non trovato"));
			partecipanteViaggio.setStatoPartecipazione(statoPredefinito);

			entityManager.merge(partecipante);


			// Aggiungi il partecipante alla lista dei partecipanti del viaggio
			viaggio.addPartecipante(partecipanteViaggio);

			try {
				notificaService.creaNotifichePerIscrizioneViaggio(viaggio, partecipante);
			} catch (Exception e) {
				System.err.println("Errore durante la creazione delle notifiche per il viaggio con id " + viaggio.getViaggioId());
				e.printStackTrace();
			}

			return partecipanteViaggio;
		}
	}
	
	@Override
	public void removePartecipanteViaggio(Long utenteId, Long viaggioId) {
		
	    int deleted = partecipantiViaggioDAO.deleteByViaggioIdAndUtenteId(viaggioId, utenteId);
	    
	    if (deleted == 0) {
	        throw new IllegalArgumentException("Partecipante con utenteId " + utenteId + " e viaggioId " + viaggioId + " non trovato.");
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
	
    public List<Viaggio> trovaViaggiPerUtente(Long utenteId) {
        List<PartecipantiViaggio> partecipazioni = partecipantiViaggioDAO.findByUtente_UtenteId(utenteId);
        
        // Estrai tutti i viaggi dalle partecipazioni
        return partecipazioni.stream()
                             .map(PartecipantiViaggio::getViaggio)
                             .collect(Collectors.toList());
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
