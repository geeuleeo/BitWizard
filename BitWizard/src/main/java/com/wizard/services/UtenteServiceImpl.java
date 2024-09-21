package com.wizard.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.wizard.entities.Utente;
import com.wizard.repos.UtenteDAO;

public class UtenteServiceImpl implements UtenteService {
	
	@Autowired
    private UtenteDAO dao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Utente salvaUtente(String nome, String cognome, String email, String password) {

        Utente u = new Utente();
        u.setNome(nome);
        u.setCognome(cognome);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(password));

        return dao.save(u);
    }

	@Override
	public Set<String> getUtenti() {
		return new TreeSet<String>(dao.findAll().stream().map(d -> d.getEmail()).sorted().toList());
	}

	@Override
	public Utente loggaUtente(String email, String password) {

	    Utente u = dao.findByEmail(email);
	    if (u != null && passwordEncoder.matches(password, u.getPassword()) && u.getEmail().matches(email)) {
	        return u;
	    }

	    return null;
	}

    @Override
    public Optional<Utente> getUtente(Long id) {
        return dao.findById(id);
    }

    @Override
    public List<Utente> getAllUtenti() {
        return dao.findAll();
    }

	public Utente eliminaUtente(String email, String password) {
		
		Utente u = dao.findByEmail(email);
		
		if (u != null && passwordEncoder.matches(password, u.getPassword()) && u.getEmail().matches(email)) {
			u.setDeleted(true);
			dao.save(u);
			return null;
	    }
		
		return u;
	}

	@Override
	public Utente authenticate(String email, String password) {

	    Utente u = dao.findByEmail(email);
	    if (u != null && passwordEncoder.matches(password, u.getPassword()) && u.getEmail().matches(email)) {
	        return u;
	    }

	    return null;
	}

}