package com.wizard.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wizard.entities.Ruolo;

@Repository
public interface RuoloDAO extends JpaRepository<Ruolo, Integer> {
	
	List<Ruolo> findByDescrizione(String descrizione);

}