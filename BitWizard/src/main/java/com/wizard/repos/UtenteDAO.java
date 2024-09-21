package com.wizard.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wizard.entities.Utente;

@Repository
public interface UtenteDAO extends JpaRepository<Utente, Long> {
	
	Utente findByEmail(String email);

}