package com.wizard.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wizard.entities.Viaggio;

import jakarta.transaction.Transactional;

@Repository
public interface ViaggioDAO extends JpaRepository<Viaggio, Long> {
	
	List<Viaggio> findByNome(String nome);

	List<Viaggio> findByEtaMinGreaterThanEqualAndEtaMaxLessThanEqual(Integer min, Integer max);
	
	List<Viaggio> findByLuogoArrivoContainingIgnoreCase(String destinazione);

	List<Viaggio> findByLuogoPartenzaContainingIgnoreCase(String partenza);

	List<Viaggio> findByCreatoreId(Long creatoreId);

	List<Viaggio> findByPrezzoBetween(Integer min, Integer max);
	
	@Transactional
	@Query("SELECT v FROM Viaggio v JOIN v.partecipanti p WHERE p.utente.id = :utenteId")
	List<Viaggio> findViaggiByPartecipanteId(Long utenteId);

    List<Viaggio> findByAgenziaId(Long agenziaId);
}