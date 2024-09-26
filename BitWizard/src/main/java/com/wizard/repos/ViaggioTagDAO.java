package com.wizard.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wizard.entities.ViaggioTag;

public interface ViaggioTagDAO extends JpaRepository<ViaggioTag, Integer>{

	List<ViaggioTag> findByTagTagId(Integer tagId);
	
}
