package com.wizard.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.wizard.entities.Immagine;
import com.wizard.entities.Livello;
import com.wizard.entities.Ruolo;
import com.wizard.entities.Tag;
import com.wizard.entities.Utente;
import com.wizard.repos.ImmagineDAO;
import com.wizard.repos.UtenteDAO;

@Service
public class UtenteServiceImpl implements UtenteService {
	
	@Autowired
    private UtenteDAO dao;
	
	@Autowired
	private ImmagineDAO immagineDAO;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
	@Override
	public Utente salvaUtente(String nome, String cognome, String numeroTelefono, String email, String password,
			Date dataNascita, String descrizione, Immagine imgProfilo, List<Tag> tag, Ruolo ruolo) {
		
		Utente u = new Utente();
		
        u.setNome(nome);
        u.setCognome(cognome);
        u.setEmail(email);
        u.setNumeroTelefono(numeroTelefono);
        u.setDataNascita(dataNascita);
        u.setDescrizione(descrizione);
        u.setDeleted(false);
        u.setCreatoIl(new Date());
        u.setTag(tag);
        u.setRuolo(ruolo);
        
        u.setPassword(passwordEncoder.encode(password));
        
        // 1. Salva l'immagine nel database
        Immagine immagineSalvata = immagineDAO.save(imgProfilo);
        System.out.println("Immagine salvata con ID: " + immagineSalvata.getIdImg());

        // 2. Ora assegna l'immagine salvata all'utente
        u.setImmagineProfilo(immagineSalvata);

        // 3. Salva l'utente nel database con l'immagine associata
        Utente utenteSalvato = dao.save(u);
        System.out.println("Utente salvato con ID: " + utenteSalvato.getUtenteId());
        
		return utenteSalvato;
	}
	
	@Override
    public boolean existByEmail(String email) {
        return dao.findByEmail(email).isPresent();
    }

    @Override
    public List<Utente> getAllUtenti() {
        return dao.findAll();
    }
	
}