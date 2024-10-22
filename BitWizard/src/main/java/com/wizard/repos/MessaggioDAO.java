package com.wizard.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wizard.entities.ChiaveMessaggio;
import com.wizard.entities.Messaggio;
import com.wizard.entities.Viaggio;

@Repository
public interface MessaggioDAO extends JpaRepository<Messaggio, ChiaveMessaggio> {
	
	List<Messaggio> findByViaggio(Viaggio viaggio);
	
	@Query("SELECT MAX(m.chiavemessaggio.messaggioId) FROM Messaggio m WHERE m.chiavemessaggio.viaggioId = :viaggioId AND m.chiavemessaggio.utenteId = :utenteId")
	Long findUltimoMessaggioId(@Param("viaggioId") Long viaggioId, @Param("utenteId") Long utenteId);

}
