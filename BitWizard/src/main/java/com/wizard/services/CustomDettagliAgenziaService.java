package com.wizard.services;

import com.wizard.customs.CustomDettagliAgenzia;
import com.wizard.entities.Agenzia;
import com.wizard.repos.AgenziaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomDettagliAgenziaService implements UserDetailsService {

    @Autowired
    private AgenziaDAO agenziaDAO;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Agenzia agenzia = agenziaDAO.findAgenziaByPartitaIva(username).orElse(null);
        if (agenzia == null) {
            throw new UsernameNotFoundException("Agenzia non trovata con partita IVA : " + username);
        }


        return new CustomDettagliAgenzia(agenzia);
    }
}
