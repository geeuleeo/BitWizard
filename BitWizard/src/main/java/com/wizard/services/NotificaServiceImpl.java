package com.wizard.services;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wizard.entities.Notifica;
import com.wizard.entities.PartecipantiViaggio;
import com.wizard.entities.Utente;
import com.wizard.entities.Viaggio;
import com.wizard.repos.NotificaDAO;
import com.wizard.repos.UtenteDAO;

@Service
public class NotificaServiceImpl implements NotificaService {
	
    @Autowired
    private NotificaDAO notificaDAO;
    
    @Autowired
    UtenteDAO utenteDAO;
    
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void creaNotificaBenvenuto(Utente utente) {

        Notifica notifica = new Notifica();
        notifica.setUtenteId(utente.getUtenteId());
        notifica.setTesto("Benvenuto " + utente.getNome() + "! Siamo lieti di averti tra noi. " +
                          "Esplora il nostro sito e scopri tutte le fantastiche opportunità di viaggio disponibili. " +
                          "Buon viaggio!");
        notifica.setData(new Date());

        notificaDAO.save(notifica);
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void creaNotifichePerCreazioneViaggio(Viaggio viaggio) {
    	
    	Utente utente = utenteDAO.findById(viaggio.getCreatoreId())
                .orElseThrow(() -> {
                    System.out.println("Utente con ID " + viaggio.getCreatoreId() + " non trovato.");
                    return new IllegalArgumentException("Utente non trovato");
                });;

        Notifica notifica = new Notifica();
        notifica.setUtenteId(utente.getUtenteId());
        notifica.setTesto("Complimenti " + utente.getNome() + "! Hai creato con successo il viaggio '" + viaggio.getNome() + 
                          "' con partenza da " + viaggio.getLuogoPartenza() + " il " + viaggio.getDataPartenza() + 
                          ". Il tuo viaggio è ora disponibile per le iscrizioni!");
        notifica.setData(new Date());

        notificaDAO.save(notifica);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void creaNotifichePerIscrizioneViaggio(Viaggio viaggio, Utente utente) {

        Notifica notifica = new Notifica();
        notifica.setUtenteId(utente.getUtenteId());
        notifica.setTesto("Ciao " + utente.getNome() + "! Ti sei iscritto con successo al viaggio '" + viaggio.getNome() + 
                          "' con partenza da " + viaggio.getLuogoPartenza() + " il " + viaggio.getDataPartenza() + 
                          ". Preparati per una fantastica avventura!");
        notifica.setData(new Date());
        
        System.out.println("Utente ID: " + utente.getUtenteId());
        System.out.println("Testo notifica: " + notifica.getTesto());
        System.out.println("Data notifica: " + notifica.getData());

        try {
            notificaDAO.save(notifica);
            System.out.println("Notifica creata per l'utente con ID " + utente.getUtenteId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante la creazione della notifica", e);
        }
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void creaNotifichePerAnnullamentoIscrizioneViaggio(Viaggio viaggio, Utente utente) {

        Notifica notifica = new Notifica();
        notifica.setUtenteId(utente.getUtenteId());
        notifica.setTesto("Ciao " + utente.getNome() + ", hai annullato la tua iscrizione al viaggio '" + viaggio.getNome() + 
                          "' con partenza da " + viaggio.getLuogoPartenza() + " il " + viaggio.getDataPartenza() + 
                          ". Speriamo di rivederti presto in un altro viaggio!");
        notifica.setData(new Date());

        notificaDAO.save(notifica);
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void creaNotifichePerPartecipanti(Viaggio viaggio) {
    	
        Set<PartecipantiViaggio> partecipanti = viaggio.getPartecipanti();

        if (partecipanti != null && !partecipanti.isEmpty()) {
            for (PartecipantiViaggio partecipante : partecipanti) {
                Utente utente = partecipante.getUtente();

                Notifica notifica = new Notifica();
                notifica.setUtenteId(utente.getUtenteId());
                notifica.setTesto("Il viaggio '" + viaggio.getNome() + "' è stato modificato.");
                notifica.setData(new Date());

                notificaDAO.save(notifica);
            }
        }
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void creaNotifichePerPartecipantiAnnullaViaggio(Viaggio viaggio) {
    	
        Set<PartecipantiViaggio> partecipanti = viaggio.getPartecipanti();

        if (partecipanti != null && !partecipanti.isEmpty()) {
            for (PartecipantiViaggio partecipante : partecipanti) {
                Utente utente = partecipante.getUtente();

                Notifica notifica = new Notifica();
                notifica.setUtenteId(utente.getUtenteId());
                notifica.setTesto("Il viaggio '" + viaggio.getNome() + "' è stato ANNULLATO.");
                notifica.setData(new Date());

                notificaDAO.save(notifica);
            }
        }
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void creaNotifichePerRifiutoAmicizia(Long riceveRichiestaId, Long inviaRichiestaId) {

        System.out.println(inviaRichiestaId + " id inviaRichiesta");
        System.out.println(riceveRichiestaId + " id riceveRichiesta");
        
        // Carlo sta inviando la richiesta, quindi trova Carlo (inviaRichiestaId)
        Utente utenteRichiedente = utenteDAO.findById(inviaRichiestaId)
                .orElseThrow(() -> {
                    System.out.println("Utente con ID " + inviaRichiestaId + " non trovato.");
                    return new IllegalArgumentException("Utente non trovato");
                });
                
        System.out.println("Utente che invia la richiesta: " + utenteRichiedente.getNome());

        // Mauro sta ricevendo la richiesta, quindi trova Mauro (riceveRichiestaId)
        Utente utenteRicevente = utenteDAO.findById(riceveRichiestaId)
                .orElseThrow(() -> {
                    System.out.println("Utente con ID " + riceveRichiestaId + " non trovato.");
                    return new IllegalArgumentException("Utente non trovato");
                });
                
        System.out.println("Utente che riceve la richiesta: " + utenteRicevente.getNome());

        // Creazione della notifica
        Notifica notifica = new Notifica();
        
        // La notifica va inviata al ricevente, cioè Mauro (utenteRicevente)
        notifica.setUtenteId(utenteRicevente.getUtenteId());
        
        notifica.setTesto("Ciao " + utenteRicevente.getNome() + ", " + utenteRichiedente.getNome() + "  ha rifiutato la tua richiesta di amicizia.");
        
        notifica.setData(new Date());

        notificaDAO.save(notifica);
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void creaNotifichePerAccettazioneAmicizia(Long riceveRichiestaId, Long inviaRichiestaId) {

        System.out.println(inviaRichiestaId + " id inviaRichiesta");
        System.out.println(riceveRichiestaId + " id riceveRichiesta");
        
        // Carlo sta inviando la richiesta, quindi trova Carlo (inviaRichiestaId)
        Utente utenteRichiedente = utenteDAO.findById(inviaRichiestaId)
                .orElseThrow(() -> {
                    System.out.println("Utente con ID " + inviaRichiestaId + " non trovato.");
                    return new IllegalArgumentException("Utente non trovato");
                });
                
        System.out.println("Utente che invia la richiesta: " + utenteRichiedente.getNome());

        // Mauro sta ricevendo la richiesta, quindi trova Mauro (riceveRichiestaId)
        Utente utenteRicevente = utenteDAO.findById(riceveRichiestaId)
                .orElseThrow(() -> {
                    System.out.println("Utente con ID " + riceveRichiestaId + " non trovato.");
                    return new IllegalArgumentException("Utente non trovato");
                });
                
        System.out.println("Utente che riceve la richiesta: " + utenteRicevente.getNome());

        // Creazione della notifica
        Notifica notifica = new Notifica();
        
        // La notifica va inviata al ricevente, cioè Mauro (utenteRicevente)
        notifica.setUtenteId(utenteRicevente.getUtenteId());
        
        notifica.setTesto("Ciao " + utenteRicevente.getNome() + ", " + utenteRichiedente.getNome() + "  ha accettato la tua richiesta di amicizia!");
        
        notifica.setData(new Date());

        notificaDAO.save(notifica);
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void creaNotifichePerRichiestaAmicizia(Long riceveRichiestaId, Long inviaRichiestaId) {
        
        // id inviaRichiesta = 49 (Carlo)
        // id riceveRichiesta = 32 (Mauro)
        
        System.out.println(inviaRichiestaId + " id inviaRichiesta");
        System.out.println(riceveRichiestaId + " id riceveRichiesta");
        
        // Carlo sta inviando la richiesta, quindi trova Carlo (inviaRichiestaId)
        Utente utenteRichiedente = utenteDAO.findById(inviaRichiestaId)
                .orElseThrow(() -> {
                    System.out.println("Utente con ID " + inviaRichiestaId + " non trovato.");
                    return new IllegalArgumentException("Utente non trovato");
                });
                
        System.out.println("Utente che invia la richiesta: " + utenteRichiedente.getNome());

        // Mauro sta ricevendo la richiesta, quindi trova Mauro (riceveRichiestaId)
        Utente utenteRicevente = utenteDAO.findById(riceveRichiestaId)
                .orElseThrow(() -> {
                    System.out.println("Utente con ID " + riceveRichiestaId + " non trovato.");
                    return new IllegalArgumentException("Utente non trovato");
                });
                
        System.out.println("Utente che riceve la richiesta: " + utenteRicevente.getNome());

        // Creazione della notifica
        Notifica notifica = new Notifica();
        
        // La notifica va inviata al ricevente, cioè Mauro (utenteRicevente)
        notifica.setUtenteId(utenteRicevente.getUtenteId());
        
        notifica.setTesto("Ciao " + utenteRicevente.getNome() + ", hai ricevuto una richiesta d'amicizia da " 
                + utenteRichiedente.getNome() + ". "
                + "<button class=\"gestisci-amicizia-btn btn btn-sm btn-success me-2\" onclick=\"gestisciRichiestaAmicizia(" + utenteRichiedente.getUtenteId() + ", true, this)\">Accetta</button>"
                + "<button class=\"gestisci-amicizia-btn btn btn-sm btn-danger\" onclick=\"gestisciRichiestaAmicizia(" + utenteRichiedente.getUtenteId() + ", false, this)\">Rifiuta</button>");
        
        notifica.setData(new Date());
        
        notifica.setData(new Date());

        notificaDAO.save(notifica);
    }

}