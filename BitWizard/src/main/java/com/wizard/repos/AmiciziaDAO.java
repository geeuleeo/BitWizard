package com.wizard.repos;

import com.wizard.entities.Amicizia;
import com.wizard.entities.ChiaveAmicizia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AmiciziaDAO extends JpaRepository<Amicizia, ChiaveAmicizia> {

    @Query("SELECT a FROM Amicizia a WHERE a.utente_id1 = :utenteId OR a.utente_id2 = :utenteId")
    List<Amicizia> findAmicizieByUtenteId(@Param("utenteId") Long utenteId);


}
