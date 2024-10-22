package com.wizard.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wizard.DTO.NotificaDTO;
import com.wizard.entities.Notifica;
import com.wizard.entities.Utente;
import com.wizard.repos.NotificaDAO;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@RestController
public class NotificaController {
	
	@Autowired
	private NotificaDAO notificaDAO;
	
	@PostMapping("/notifica/crea")
	public ResponseEntity<?> creaNotifica(@RequestBody NotificaDTO notificaDTO) {
		
		Notifica notifica = createNotificaFromDTO(notificaDTO);
		
		notificaDAO.save(notifica);
		
		return new ResponseEntity<>(notifica, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/notifica/cancella")
	@Transactional
	public ResponseEntity<?> cancellaNotifica(@RequestParam Long notificaId) {
	    Optional<Notifica> notificaOpt = notificaDAO.findById(notificaId);
	    if (!notificaOpt.isPresent()) {
	        return new ResponseEntity<>("Notifica non trovata", HttpStatus.NOT_FOUND);
	    }
	    notificaDAO.delete(notificaOpt.get());
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/notifica/cerca")
	public List<Notifica> getNotifiche(HttpSession session) {
		
		Utente utente = (Utente) session.getAttribute("utenteLoggato");
		
		List<Notifica> notifiche = notificaDAO.findNotificaByUtenteId(utente.getUtenteId());
		
		return notifiche;
	}
	
    private Notifica createNotificaFromDTO(NotificaDTO notificaDTO) {
        Notifica notifica = new Notifica();
        notifica.setTesto(notificaDTO.getTesto());
        notifica.setUtenteId(notificaDTO.getUtenteId());
        notifica.setData(new Date());
        
        return notifica;
    }
    
    private List<NotificaDTO> createDTOFromNotifica(List<Notifica> notifica){
    	List<NotificaDTO> notificaDTO = new ArrayList<NotificaDTO>();
    	
    	for (NotificaDTO notificaDTO2 : notificaDTO) {
			notificaDTO2.setNotificaId(notifica.get);
		}
    	
    	return notificaDTO;
    }
	
}
