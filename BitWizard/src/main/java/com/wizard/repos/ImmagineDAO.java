package com.wizard.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wizard.entities.Immagine;

@Repository
public interface ImmagineDAO extends JpaRepository<Immagine, Integer> {
	
	List<Immagine> findByImg(String img);

}