package com.wizard.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wizard.customs.CustomDettagliUtente;
import com.wizard.entities.Utente;
import com.wizard.repos.UtenteDAO;

@Service
public class CustomDettagliUtenteService implements UserDetailsService{
	
    @Autowired
    private UtenteDAO utenteRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utente utente = utenteRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con email: " + email));

        return new CustomDettagliUtente(utente);
    }

}