package com.wizard.services;

import com.wizard.customs.CustomDettagliAgenzia;
import com.wizard.entities.Agenzia;
import com.wizard.repos.AgenziaDAO;
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
    @Autowired
    private AgenziaDAO agenziaRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utente utente = utenteRepository.findByEmail(email).orElse(null);

        if (utente == null) {
            Agenzia agenzia = agenziaRepository.findAgenziaByPartitaIva(email).orElseThrow(() -> new UsernameNotFoundException(email));
            return new CustomDettagliAgenzia(agenzia);
        }



        return new CustomDettagliUtente(utente);
    }

}