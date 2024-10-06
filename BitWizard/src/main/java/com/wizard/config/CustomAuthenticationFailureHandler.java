package com.wizard.config;

import java.io.IOException;

import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			org.springframework.security.core.AuthenticationException exception) throws IOException, ServletException {

		// Aggiungi un attributo alla sessione per indicare il fallimento
        request.getSession().setAttribute("loginError", "Credenziali non valide. Per favore riprova.");

        // Reindirizza alla pagina di login
        response.sendRedirect("/login");
		
	}

}