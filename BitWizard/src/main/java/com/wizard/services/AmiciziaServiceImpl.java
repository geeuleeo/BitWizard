package com.wizard.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.wizard.DTO.AmicoDTO;
import com.wizard.entities.Amicizia;
import com.wizard.entities.Amicizia.StatoAmicizia;
import com.wizard.entities.ChiaveAmicizia;
import com.wizard.entities.Notifica;
import com.wizard.entities.Utente;
import com.wizard.entities.Viaggio;
import com.wizard.repos.AmiciziaDAO;
import com.wizard.repos.NotificaDAO;
import com.wizard.repos.UtenteDAO;
import com.wizard.repos.ViaggioDAO;
import com.wizard.repos.ViaggioDTO;

@Service
public class AmiciziaServiceImpl implements AmiciziaService {
	
    @Autowired
    AmiciziaDAO amiciziaDAO;
    
    @Autowired
    UtenteDAO utenteDAO;
    
    @Autowired
    ViaggioDAO viaggioDAO;
    
    @Autowired
    NotificaService notificaService;
    
    @Autowired
    NotificaDAO notificaDAO;
    
    @Override
    public Amicizia creaAmicizia(Long utenteInvianteId, Long utenteRiceventeId) {

        ChiaveAmicizia chiaveAmicizia = new ChiaveAmicizia();
        chiaveAmicizia.setUtenteInviante(utenteInvianteId);
        chiaveAmicizia.setUtenteRicevente(utenteRiceventeId);
        Date data = new Date();
        
        Utente utenteInviante = utenteDAO.findByUtenteId(utenteInvianteId)
        	.orElseThrow(() -> new IllegalArgumentException("Utente inviante non trovato"));
        
        Utente utenteRicevente = utenteDAO.findByUtenteId(utenteRiceventeId)
            	.orElseThrow(() -> new IllegalArgumentException("Utente ricevente non trovato"));

        Amicizia amicizia = new Amicizia(chiaveAmicizia,utenteInviante,utenteRicevente,data, null);

        return amiciziaDAO.save(amicizia);
    }
    
    public void inviaRichiestaAmicizia(Long utenteInvianteId, Long utenteRiceventeId) {
    	
    	ChiaveAmicizia chiaveAmicizia = new ChiaveAmicizia(utenteInvianteId, utenteRiceventeId);
    	
    	Utente utenteInviante = utenteDAO.findByUtenteId(utenteInvianteId)
            	.orElseThrow(() -> new IllegalArgumentException("Utente inviante non trovato"));
            
            Utente utenteRicevente = utenteDAO.findByUtenteId(utenteRiceventeId)
                	.orElseThrow(() -> new IllegalArgumentException("Utente ricevente non trovato"));
    	
        Amicizia amicizia = new Amicizia();
        amicizia.setUtenteInviante(utenteInviante);
        amicizia.setUtenteRicevente(utenteRicevente);
        amicizia.setDataAmicizia(new Date());
        amicizia.setStato(StatoAmicizia.IN_ATTESA);
        amicizia.setChiave(chiaveAmicizia);        
        
        amiciziaDAO.save(amicizia);
        
        try {
            notificaService.creaNotifichePerRichiestaAmicizia(utenteRiceventeId, utenteInvianteId);
        } catch (Exception e) {
            System.err.println("Errore durante la creazione delle notifiche per l'amicizia a: " + amicizia.getUtenteRicevente().getNome());
            e.printStackTrace();
        }
    }

    public void accettaRichiesta(Long utenteLoggatoId, Long utenteId, Long notificaId) {
    	
    	 Optional<Amicizia> amiciziaOptional = amiciziaDAO.findAmiciziaByUtenti(utenteLoggatoId, utenteId);
        		
    	 Amicizia amicizia = amiciziaOptional
    	            .orElseThrow(() -> new IllegalArgumentException("Richiesta d'amicizia non trovata"));
        
        amicizia.setStato(StatoAmicizia.ACCETTATO);
        
        amiciziaDAO.save(amicizia);
        
        Optional<Notifica> notificaOpt = notificaDAO.findById(notificaId);
	    if (!notificaOpt.isPresent()) {
	    	System.err.println("Notifica non trovata");
	    }
	    notificaDAO.delete(notificaOpt.get());
    }

    public void rifiutaRichiesta(Long utenteLoggatoId, Long utenteId, Long notificaId) {
    	
    	System.out.println("Utente loggato ID: " + utenteLoggatoId);
    	System.out.println("Utente ID: " + utenteId);
    	
        Optional<Amicizia> amiciziaOptional = amiciziaDAO.findAmiciziaByUtenti(utenteLoggatoId, utenteId);

        Amicizia amicizia = amiciziaOptional
            .orElseThrow(() -> new IllegalArgumentException("Richiesta d'amicizia non trovata"));

        amiciziaDAO.delete(amicizia);
        
        Optional<Notifica> notificaOpt = notificaDAO.findById(notificaId);
	    if (!notificaOpt.isPresent()) {
	    	System.err.println("Notifica non trovata");
	    }
	    notificaDAO.delete(notificaOpt.get());
    }

    @Override
    public List<Amicizia> getAmicizie(Long utenteId) {
    	
        return amiciziaDAO.findAmicizieByUtenteId(utenteId);
    }

    @Override
    public List<ViaggioDTO> getViaggiDegliAmici(Long utenteId) {

        List<Amicizia> amici = amiciziaDAO.findAmicizieByUtenteId(utenteId);

        List<Long> listaIds = new ArrayList<>();

        for (Amicizia amico : amici) {

            listaIds.add( amico.getChiave().getUtenteInviante());
            listaIds.add( amico.getChiave().getUtenteRicevente());

        }
        List<Long> listaIdAmici = new ArrayList<>();
        for (Long id : listaIds) {

            if (!id.equals(utenteId)) {
                    listaIdAmici.add(id);
            }

        }
        List<ViaggioDTO> viaggi = new ArrayList<>();


        for (Long id : listaIdAmici) {
            List<Viaggio> viaggiUtenti = viaggioDAO.findViaggiByPartecipanteId(id);
            if (!viaggiUtenti.isEmpty()) {

                for (Viaggio v : viaggiUtenti) {
                    viaggi.add(convertToDTO(v));
                }

            }

        }
        return viaggi;
    }
    
    public AmicoDTO toDTO(Utente amico) {
    	
        AmicoDTO amicoDTO = new AmicoDTO();

        amicoDTO.setNome(amico.getNome());
        amicoDTO.setCognome(amico.getCognome());
        amicoDTO.setUtenteId(amico.getUtenteId());
        
        if (amico.getImmagine() != null) {
            amicoDTO.setImmagine(amico.getImmagine().getImg());
        }

        return amicoDTO;
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
}
