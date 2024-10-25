package com.wizard.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wizard.entities.Amicizia;
import com.wizard.entities.ChiaveAmicizia;

public interface AmiciziaDAO extends JpaRepository<Amicizia, ChiaveAmicizia> {

	@Query("SELECT a FROM Amicizia a WHERE a.utenteInviante.utenteId = :utenteId OR a.utenteRicevente.utenteId = :utenteId")
	List<Amicizia> findAmicizieByUtenteId(@Param("utenteId") Long utenteId);
	
	@Query("SELECT a FROM Amicizia a WHERE (a.utenteInviante.utenteId = :utenteInvianteId AND a.utenteRicevente.utenteId = :utenteRiceventeId) OR (a.utenteInviante.utenteId = :utenteRiceventeId AND a.utenteRicevente.utenteId = :utenteInvianteId)")
	Optional<Amicizia> findAmiciziaByUtenti(@Param("utenteInvianteId") Long utenteInvianteId, @Param("utenteRiceventeId") Long utenteRiceventeId);

}
