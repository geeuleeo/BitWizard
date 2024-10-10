package com.wizard.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wizard.entities.ChiavePartecipantiViaggio;
import com.wizard.entities.Immagine;
import com.wizard.entities.PartecipantiViaggio;
import com.wizard.entities.Stato;
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
	
	public Viaggio salvaViaggio(ViaggioDTO viaggioDTO) {
		
		System.out.println("Inizio creazione viaggio...");
		
		Long creatoreId = viaggioDTO.getCreatoreId();
		
	    Viaggio viaggio = new Viaggio();
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
	    viaggio.setDeleted(false);
	    viaggio.setCreatoIl(new Date());	    
	    
	    if (Optional.ofNullable(viaggioDTO.getImmagineCopertina()).isPresent() && viaggioDTO.getImmagineCopertina().length > 0) {
	        try {
	            byte[] imgBytes = viaggioDTO.getImmagineCopertina();
	            
	            // Aggiungi qui un controllo del formato immagine, se necessario
	            
	            Immagine immagine = new Immagine();
	            immagine.setImg(imgBytes); // Salva i dati dell'immagine
	            immagineDAO.save(immagine);
	            
	            viaggio.setImmagineCopertina(immagine); // Associa l'immagine al viaggio
	        } catch (IllegalArgumentException e) {
	            throw new IllegalArgumentException("Formato immagine non valido: " + e.getMessage(), e);
	        }
	    }

	    // Imposta il creatore del viaggio
	    viaggio.setCreatoreId(creatoreId);
	    System.out.println("Creatore ID impostato: " + creatoreId);

	    System.out.println("Tentativo di salvataggio del viaggio...");
	    
	    Viaggio viaggioSalvato = new Viaggio();
	    
	    try {
	        viaggioSalvato = dao.save(viaggio);
	        System.out.println("Viaggio salvato con ID: " + viaggioSalvato.getViaggioId());
	    } catch (Exception e) {
	        System.out.println("Errore durante il salvataggio del viaggio: " + e.getMessage());
	        e.printStackTrace();
	        throw e;
	    }
        
        List<Long> tagIds = viaggioDTO.getTagIds();
        
        associaTagEViaggio(tagIds, viaggioSalvato);
        
        Optional<Utente> creatoreOptional = utenteDAO.findById(viaggioDTO.getCreatoreId());

        // Verifica se il creatore è presente
        if (creatoreOptional.isPresent()) {
            Utente creatore = creatoreOptional.get();
            
            // Crea una nuova chiave composta per il partecipante
            ChiavePartecipantiViaggio chiavePartecipantiViaggio = new ChiavePartecipantiViaggio();
            chiavePartecipantiViaggio.setViaggioId(viaggioSalvato.getViaggioId());
            chiavePartecipantiViaggio.setUtenteId(creatore.getUtenteId());

            // Crea un record per la tabella PartecipantiViaggio
            PartecipantiViaggio partecipante = new PartecipantiViaggio();
            partecipante.setId(chiavePartecipantiViaggio);  // Imposta la chiave composta
            partecipante.setViaggio(viaggioSalvato);  // Associa il viaggio
            partecipante.setUtente(creatore);  // Associa l'utente (creatore)
            partecipante.setDataIscrizione(new Date());  // Imposta la data di partecipazione

            // Recupera lo stato di partecipazione predefinito (es. "In attesa")
            Stato statoPredefinito = statoDAO.findById(1)  // Sostituisci con lo stato predefinito corretto
                .orElseThrow(() -> new IllegalArgumentException("Stato di partecipazione predefinito non trovato"));

            // Imposta lo stato di partecipazione
            partecipante.setStatoPartecipazione(statoPredefinito);

            // Salva il partecipante nel database
            partecipantiViaggioDAO.save(partecipante);

            // Aggiungi il partecipante alla lista dei partecipanti del viaggio
            viaggioSalvato.addPartecipante(partecipante);
        } else {
            throw new IllegalArgumentException("Creatore con ID " + viaggioDTO.getCreatoreId() + " non trovato.");
        }
        
        return viaggioSalvato;
	}
	
	@Override
	public Utente addPartecipanteViaggio(Utente partecipante, Viaggio viaggio) {

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

	        return utente;  // Restituisci il partecipante aggiunto
	    } else {
	        throw new IllegalArgumentException("Utente con ID " + partecipante.getUtenteId() + " non trovato.");
	    }
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
