package com.wizard.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wizard.entities.ChiavePartecipantiViaggio;
import com.wizard.entities.PartecipantiViaggio;
import com.wizard.entities.Viaggio;

@Repository
public interface PartecipantiViaggioDAO extends JpaRepository<PartecipantiViaggio, ChiavePartecipantiViaggio> {
	
    List<PartecipantiViaggio> findByViaggio(Viaggio viaggio);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM PartecipantiViaggio pv WHERE pv.id.viaggioId = :viaggioId AND pv.id.utenteId = :utenteId")
    int deleteByViaggioIdAndUtenteId(@Param("viaggioId") Long viaggioId, @Param("utenteId") Long utenteId);
    
}