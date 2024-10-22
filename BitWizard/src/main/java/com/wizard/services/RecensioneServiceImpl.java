package com.wizard.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wizard.entities.ChiaveRecensione;
import com.wizard.entities.Recensione;
import com.wizard.entities.Utente;
import com.wizard.entities.Viaggio;
import com.wizard.repos.RecensioneDAO;
import com.wizard.repos.RecensioneDTO;
import com.wizard.repos.UtenteDAO;
import com.wizard.repos.ViaggioDAO;

@Service
public class RecensioneServiceImpl implements RecensioneService{
	
    @Autowired
    private RecensioneDAO recensioniRepository;

    @Autowired
    private ViaggioDAO viaggioRepository;

    @Autowired
    private UtenteDAO utenteRepository;
    
    @Autowired
    private RecensioneDAO recensioneDAO;

    public Recensione salvaRecensione(Long viaggioId, Long utenteId, String testo, int rating, Date data) {
    	
        Optional<Viaggio> viaggio = viaggioRepository.findById(viaggioId);
        Optional<Utente> utente = utenteRepository.findById(utenteId);

        if (viaggio.isPresent() && utente.isPresent()) {
            Recensione recensione = new Recensione();
            ChiaveRecensione chiave = new ChiaveRecensione();
            chiave.setViaggioId(viaggioId);
            chiave.setUtenteId(utenteId);

            recensione.setId(chiave);
            recensione.setViaggio(viaggio.get());
            recensione.setUtente(utente.get());
            recensione.setTesto(testo);
            recensione.setRating(rating);
            recensione.setData(data);

            return recensioniRepository.save(recensione);
        } else {
            throw new RuntimeException("Viaggio o Utente non trovato");
        }
    }
    
    @Override
    public List<Recensione> trovaRecensioniPerUtente(Long utenteId) {
        return recensioniRepository.findByIdUtenteId(utenteId);
    }
    
    @Override
    public List<Recensione> trovaRecensioniPerViaggio(Long viaggioId) {
	    return recensioniRepository.findByIdViaggioId(viaggioId);
    }
    
    @Override
    public List<Recensione> trovaRecensioniDeiViaggiCreatiDaUtente(Long creatoreId) {
        // Troviamo tutti i viaggi creati dall'utente
        List<Viaggio> viaggiCreati = viaggioRepository.findByCreatoreId(creatoreId);
        
        // Ora raccogliamo tutte le recensioni per quei viaggi
        List<Recensione> recensioni = new ArrayList<>();
        
        for (Viaggio viaggio : viaggiCreati) {
            List<Recensione> recensioniPerViaggio = recensioniRepository.findByIdViaggioId(viaggio.getViaggioId());
            recensioni.addAll(recensioniPerViaggio);
        }
        
        return recensioni;
    }

    @Override
    public List<Recensione> trovaRecensioni() {
        return recensioneDAO.findAll();
    }
    
	private RecensioneDTO createDTOFromRecensione(Recensione recensione) {
		RecensioneDTO recensioneDTO = new RecensioneDTO();
	    
	    // Imposta l'ID del viaggio associato
	    if (recensione.getViaggio() != null) {
	    	recensioneDTO.setViaggioId(recensione.getViaggio().getViaggioId());
	    }

	    // Imposta l'ID dell'utente associato
	    if (recensione.getUtente() != null) {
	    	recensioneDTO.setUtenteId(recensione.getUtente().getUtenteId());
	    }

	    // Imposta il testo della recensione
	    recensioneDTO.setTesto(recensione.getTesto());

	    // Imposta la data della recenzione
	    recensioneDTO.setData(recensione.getData());
	    
	    recensioneDTO.setRating(recensione.getRating());

	    return recensioneDTO;
	}

	@Override
	public List<RecensioneDTO> caricaRecensioneViaggio(Long viaggioId) {
		
		List<Recensione> recensioni = recensioniRepository.findByIdViaggioId(viaggioId);
    	
    	List<RecensioneDTO> recensioniDTO = new ArrayList<>();
	    
	    // Converte ogni Messaggio in MessaggioDTO
	    for (Recensione recensione : recensioni) {
	    	RecensioneDTO dto = createDTOFromRecensione(recensione);
	        recensioniDTO.add(dto);  // Aggiunge il DTO alla lista
	    }
	    
		return recensioniDTO;
	}

}
