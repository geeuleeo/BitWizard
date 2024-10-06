package com.wizard.config;

import java.io.IOException;

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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Ottieni l'email dall'oggetto Authentication
        String email = authentication.getName();

        // Recupera l'utente dal database
        Utente utente = utenteRepository.findByEmail(email).orElse(null);

        // Memorizza l'utente nella HttpSession
        HttpSession session = request.getSession();
        session.setAttribute("utenteLoggato", utente);

        // Reindirizza alla pagina home
        response.sendRedirect("/home");
    }

}
