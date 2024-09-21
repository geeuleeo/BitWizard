package com.wizard.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wizard.entities.Stato;

@Repository
public interface StatoDAO extends JpaRepository<Stato, Integer> {
	
	List<Stato> findByTipoStato(String tipoStato);

}