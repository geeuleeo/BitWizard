package com.wizard.repos;

import com.wizard.entities.Agenzia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgenziaDAO extends JpaRepository<Agenzia, Long> {

        Optional<Agenzia> findAgenziaByPartitaIva(String partitaIva);



}
