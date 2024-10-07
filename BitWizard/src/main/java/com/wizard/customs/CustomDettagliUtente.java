package com.wizard.customs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.wizard.entities.Utente;

public class CustomDettagliUtente implements UserDetails{
	
	 private Utente utente;

	    public CustomDettagliUtente(Utente utente) {
	        this.utente = utente;
	    }

	    @Override
	    public Collection<? extends GrantedAuthority> getAuthorities() {
	        // Mappa il ruolo dell'utente ai GrantedAuthority
	        List<GrantedAuthority> authorities = new ArrayList<>();
	        authorities.add(new SimpleGrantedAuthority(utente.getRuolo().getDescrizione()));
	        return authorities;
	    }

	    @Override
	    public String getPassword() {
	        return utente.getPassword();
	    }

	    @Override
	    public String getUsername() {
	        return utente.getEmail();
	    }
	     
	    @Override
	    public boolean isEnabled() {
	        return !utente.isDeleted();
	    }

		public Utente getUtente() {
			return this.utente;
		}

}