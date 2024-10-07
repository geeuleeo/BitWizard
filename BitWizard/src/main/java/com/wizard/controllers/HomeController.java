package com.wizard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wizard.customs.CustomDettagliUtente;
import com.wizard.entities.Utente;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
    // Metodo per servire la pagina di login
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {

        try {
            // Crea un token di autenticazione
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);

            // Autentica l'utente
            Authentication authentication = authenticationManager.authenticate(authToken);

            // Imposta l'utente autenticato nel contesto di sicurezza
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Recupera i dettagli dell'utente (cast a CustomDettagliUtente)
            CustomDettagliUtente customUserDetails = (CustomDettagliUtente) authentication.getPrincipal();

            // Ottieni l'istanza di Utente dall'oggetto CustomDettagliUtente
            Utente utente = customUserDetails.getUtente(); // Supponendo che CustomDettagliUtente abbia un metodo getUtente()

            // Memorizza l'utente nella sessione
            session.setAttribute("utenteLoggato", utente);

            // Reindirizza alla pagina home
            return "redirect:/home";

        } catch (AuthenticationException e) {
            // Autenticazione fallita
            redirectAttributes.addFlashAttribute("loginError", "Credenziali non valide. Per favore riprova.");
            return "redirect:/login";
        }
    }


    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        Utente utente = (Utente) session.getAttribute("utenteLoggato");
        if (utente != null) {
            model.addAttribute("nomeUtente", utente.getNome());
            model.addAttribute("messaggioBenvenuto", "Benvenuto, " + utente.getNome() + "!");
        }
        return "home";
    }
    
    @GetMapping("/CreaViaggio")
    public String showCreaViaggioPage() {
        return "CreaViaggio";
    }
    
}