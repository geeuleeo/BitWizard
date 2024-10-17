package com.wizard.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wizard.entities.Tag;
import com.wizard.entities.Utente;
import com.wizard.entities.UtenteTag;

public interface UtenteTagDAO extends JpaRepository<UtenteTag, Integer>{
	
	public UtenteTag findByUtenteAndTag(Utente utente, Tag tag);


}
