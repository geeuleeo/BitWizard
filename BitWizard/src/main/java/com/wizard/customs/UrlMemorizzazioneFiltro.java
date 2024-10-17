package com.wizard.customs;

import java.io.IOException;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UrlMemorizzazioneFiltro implements Filter {

    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Controlla se l'utente non è autenticato e la richiesta non è per la pagina di login
        if (httpRequest.getUserPrincipal() == null && !httpRequest.getRequestURI().contains("/login")) {
            // Memorizza l'URL originale nella sessione
            httpRequest.getSession().setAttribute("urlPrecedente", httpRequest.getRequestURI());
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
