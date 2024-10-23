package com.wizard.controllers;

import com.wizard.customs.CustomDettagliAgenzia;
import com.wizard.entities.Agenzia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.wizard.repos.UtenteTagDAO;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
    @Autowired
    private UtenteTagDAO utenteTagDAO;

      
    // Metodo per servire la pagina di login
    @GetMapping("/login")
    public String showLoginPage() {
        return "Login/login";
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
            
         // Recupera l'URL precedente dalla sessione
          //  String urlPrecedente = (String) session.getAttribute("urlPrecedente");

            // Reindirizza all'URL precedente o alla home se l'URL non è disponibile
            // return "redirect:" + (urlPrecedente != null ? urlPrecedente : "/home");
            
            return "redirect:/";

        } catch (AuthenticationException e) {
            // Autenticazione fallita
            redirectAttributes.addFlashAttribute("loginError", "Credenziali non valide. Per favore riprova.");
            return "redirect:/login";
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        // Invalida la sessione
        session.invalidate();

        // Rimuovi l'autenticazione dal contesto di sicurezza
        SecurityContextHolder.clearContext();

        // Restituisce una risposta di conferma con lo status HTTP 200
        return ResponseEntity.ok("Logout effettuato con successo.");
    }

    @GetMapping("")
    public String home(HttpSession session, Model model) {
    	
        Utente utente = (Utente) session.getAttribute("utenteLoggato");

        if (utente != null) {
            // model.addAttribute("tagsUtente",utente.getUtenteTags().stream().map(UtenteTag::getTag).findAny().get().getTagId().intValue());
            model.addAttribute("nomeUtente", utente.getNome());
           // model.addAttribute("idUtente", utente.getUtenteId());
            model.addAttribute("messaggioBenvenuto", "Benvenuto, " + utente.getNome() + "!");
        }
        return "home";
    }
    
    @GetMapping("/CreaViaggio")
    public String showCreaViaggioPage() {
        return "CreaViaggio";
    }
    
    @GetMapping("/registrazione")
    public String showSignupPage() {
        return "registrazione";
    }

    @GetMapping("/registrazioneAgenzia")
    public String showAziendaSignupPage() {
        return "registrazioneAgenzia";
    }
    
    @GetMapping("/paginaPersonale")
    public String getPaginaPersonaleUtente(HttpSession session) {
    	Utente utente = (Utente) session.getAttribute("utenteLoggato");
        if (utente == null) {
            return "redirect:/login";
        }
        return "paginaPersonaleUtente";
    }
    
	@GetMapping("/paginaViaggio/viaggio")
	public String getPaginaViaggio(@RequestParam Long viaggioId) {    
	    return "Viaggio";
	}

	@GetMapping("lista")
	public String getAllViaggi(//HttpSession session
			)
			{
	//Utente utente = (Utente) session.getAttribute("utenteLoggato");
	//if (utente == null) {
	//return "redirect:/login";

	//}
	return "listaViaggi";
	}
	
	@GetMapping ("/ricerca")
    public String showRicercaUtente() { return "Filters";}

    @GetMapping("loginAgenzia")
    public String showLoginAgenziaPage(){

        return "loginAgenzia";
    }

   @PostMapping("/loginAgenzia")
   public String loginAgenzia(@RequestParam String partitaIva,
                       @RequestParam String password,
                       HttpSession session,
                       RedirectAttributes redirectAttributes) {
       try {
           // Crea un token di autenticazione
           UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(partitaIva, password);

          // Autentica l'agenzia
           Authentication authentication = authenticationManager.authenticate(authToken);

           // Imposta l'agenzia autenticata nel contesto di sicurezza
           SecurityContextHolder.getContext().setAuthentication(authentication);

           // Recupera i dettagli dell'agenzia (cast a CustomDettagliAgenzia)
           CustomDettagliAgenzia customDettagliAgenzia = (CustomDettagliAgenzia) authentication.getPrincipal();

           // Ottieni l'istanza di Agenzia dall'oggetto CustomDettagliAgenzia
           Agenzia agenzia = customDettagliAgenzia.getAgenzia(); // Supponendo che CustomDettagliAgenzia abbia un metodo getAgenzia()

           // Memorizza l'agenzia nella sessione
           session.setAttribute("agenziaLoggata", agenzia);


           return "redirect:/";

       } catch (AuthenticationException e) {

            e.printStackTrace();
           return "redirect:/loginAzienda";
       }
   }





}