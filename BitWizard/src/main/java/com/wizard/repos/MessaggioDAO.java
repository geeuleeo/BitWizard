package com.wizard.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wizard.DTO.MessaggioDTO;
import com.wizard.entities.ChiaveMessaggio;
import com.wizard.entities.Messaggio;
import com.wizard.entities.Viaggio;

public interface MessaggioDAO extends JpaRepository<Messaggio, ChiaveMessaggio> {
	
	List<MessaggioDTO> findByViaggio(Viaggio viaggio);

}
