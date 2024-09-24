package com.wizard.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wizard.entities.Stato;

@Repository
public interface StatoDAO extends JpaRepository<Stato, Integer> {
	
	Stato findByTipoStato(String tipoStato);

	Optional<Stato> findByStatoId(Long statoId);

}