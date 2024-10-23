package com.wizard.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wizard.entities.Viaggio;
import com.wizard.entities.ViaggioTag;

import jakarta.transaction.Transactional;

public interface ViaggioTagDAO extends JpaRepository<ViaggioTag, Integer>{

	List<ViaggioTag> findByTagTagId(Integer tagId);
	
	@Modifying
    @Transactional
    @Query("DELETE FROM ViaggioTag vt WHERE vt.viaggio = :viaggio")
    void deleteByViaggio(@Param("viaggio") Viaggio viaggio);
	
}
