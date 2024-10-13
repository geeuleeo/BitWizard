package com.wizard.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wizard.DTO.TagDTO;
import com.wizard.entities.Tag;
import com.wizard.entities.Utente;

import jakarta.servlet.http.HttpSession;

@Service
public interface UtenteService {

    List<Utente> getAllUtenti();

	Utente salvaUtente(Utente utente, List<TagDTO> tagDTOs);

	boolean existByEmail(String email);

	Utente getUtente(HttpSession session);

	UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

}