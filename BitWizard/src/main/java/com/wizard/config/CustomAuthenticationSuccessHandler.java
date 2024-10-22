package com.wizard.config;

import java.io.IOException;
import java.util.Optional;

import com.wizard.entities.Agenzia;
import com.wizard.repos.AgenziaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.wizard.entities.Utente;
import com.wizard.repos.UtenteDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler{
	
    @Autowired
    private UtenteDAO utenteRepository;
    @Autowired
    private AgenziaDAO agenziaRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String email = authentication.getName();
        Utente utente = utenteRepository.findByEmail(email).orElse(null);
        System.out.println(authentication);
        if (utente != null) {
            HttpSession session = request.getSession();
            session.setAttribute("utenteLoggato", utente);
        } else {
            Optional<Agenzia> agenzia = agenziaRepository.findAgenziaByPartitaIva(email);
            if (agenzia.isPresent()) {
                Agenzia agenzia2 = agenzia.get();
                HttpSession session = request.getSession();
                session.setAttribute("agenziaLoggata", agenzia2);
            }
            }
        response.sendRedirect("/registrazione");
    }

}
