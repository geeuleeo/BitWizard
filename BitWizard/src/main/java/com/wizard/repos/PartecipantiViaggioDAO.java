package com.wizard.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wizard.entities.ChiavePartecipantiViaggio;
import com.wizard.entities.PartecipantiViaggio;
import com.wizard.entities.Viaggio;

@Repository
public interface PartecipantiViaggioDAO extends JpaRepository<PartecipantiViaggio, ChiavePartecipantiViaggio> {
	
    List<PartecipantiViaggio> findByViaggio(Viaggio viaggio);
    
}