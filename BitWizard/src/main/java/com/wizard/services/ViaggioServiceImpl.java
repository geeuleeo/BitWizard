package com.wizard.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wizard.entities.Immagine;
import com.wizard.entities.Stato;
import com.wizard.entities.Tag;
import com.wizard.entities.Utente;
import com.wizard.entities.UtenteTag;
import com.wizard.entities.Viaggio;
import com.wizard.entities.ViaggioImmagini;
import com.wizard.entities.ViaggioTag;
import com.wizard.repos.ImmagineDAO;
import com.wizard.repos.StatoDAO;
import com.wizard.repos.TagDAO;
import com.wizard.repos.ViaggioDAO;
import com.wizard.repos.ViaggioDTO;
import com.wizard.repos.ViaggioImmaginiDAO;
import com.wizard.repos.ViaggioTagDAO;

import jakarta.servlet.http.HttpSession;

@Service
public class ViaggioServiceImpl implements ViaggioService {
	
	@Autowired
	private ViaggioDAO dao;
	
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
		
	    /*
	    		(Utente) session.getAttribute("utenteCreatore");
	    if (creatore == null) {
	        throw new IllegalStateException("Creatore non trovato nella sessione.");
	    }
	     */
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

	    // Imposta il creatore del viaggio
	    viaggio.setCreatoreId(creatoreId);
	    
	    System.out.println("Creatore ID impostato: " + creatoreId);
	    
	    if (viaggioDTO.getStatoId() == null) {
	        // Recupera lo stato "In attesa" dal database usando lo statoDAO
	        Stato statoInAttesa = statoDAO.findByTipoStato("In attesa");
	        viaggio.setStato(statoInAttesa);
	        throw new IllegalStateException("Nessuno stato trovato");
	    } else {
	        // Se lo stato viene passato nel DTO, recuperalo dal database usando l'ID
	        Optional<Stato> statoOptional = statoDAO.findByStatoId(viaggioDTO.getStatoId());
	        if (statoOptional.isPresent()) {
	            viaggio.setStato(statoOptional.get());
	        } else {
	            throw new IllegalArgumentException("Stato con ID " + viaggioDTO.getStatoId() + " non trovato.");
	        }
	    }

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
        
        List<Integer> tagIds = viaggioDTO.getTagIds();
        if (tagIds != null && !tagIds.isEmpty()) {
            for (Integer tagId : tagIds) {
                Optional<Tag> tag = tagDAO.findById(tagId);
                if (tag.isPresent()) {
                    ViaggioTag viaggioTag = new ViaggioTag();
					viaggioTag.setViaggio(viaggioSalvato); // Associa l'utente
                    viaggioTag.setTag(tag.get()); // Associa il tag
                    // Salva l'associazione nella tabella utente_tag
                    viaggioTagDAO.save(viaggioTag);
                } else {
                    System.out.println("Tag con ID " + tagId + " non trovato.");
                }
            }
        }


        // Associa le immagini al viaggio
        List<Integer> immagineIds = viaggioDTO.getImmagineIds();
        if (immagineIds != null && !immagineIds.isEmpty()) {
            for (Integer immagineId : immagineIds) {
                Optional<Immagine> imgOptional = immagineDAO.findById(immagineId);
                if (imgOptional.isPresent()) {
                    Immagine img = imgOptional.get();
                    ViaggioImmagini viaggioImg = new ViaggioImmagini();
                    viaggioImg.setViaggio(viaggioSalvato);
                    viaggioImg.setImmagine(img);
                    viaggioImgDAO.save(viaggioImg);
                } else {
                    System.out.println("Immagine con ID " + immagineId + " non trovata.");
                }
            }
        }
        
        return viaggioSalvato;
	}
}
