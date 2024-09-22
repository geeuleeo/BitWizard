package com.wizard.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wizard.entities.Utente;

@Repository
public interface UtenteDAO extends JpaRepository<Utente, Long> {
	
	Optional<Utente> findByEmail(String email);

}