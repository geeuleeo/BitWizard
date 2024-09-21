package com.wizard.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wizard.entities.Livello;

public interface LivelloDAO extends JpaRepository<Livello, Integer> {
	
	List<Livello> findByDescrizione(String descrizione);

}