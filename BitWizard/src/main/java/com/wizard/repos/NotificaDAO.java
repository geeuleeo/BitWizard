package com.wizard.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wizard.entities.Notifica;

@Repository
public interface NotificaDAO extends JpaRepository<Notifica, Long>{
	
	public List<Notifica> findNotificaByUtenteId(Long utenteId);

}
