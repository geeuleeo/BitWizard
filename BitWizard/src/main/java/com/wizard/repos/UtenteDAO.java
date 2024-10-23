package com.wizard.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wizard.entities.Utente;
import com.wizard.entities.UtenteTag;

@Repository
public interface UtenteDAO extends JpaRepository<Utente, Long> {
	
	Optional<Utente> findByEmail(String email);

	void save(UtenteTag utenteTag);

	Optional<Utente> findByUtenteId(Long utenteId);

	boolean existsByEmail(String email);

}