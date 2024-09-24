package com.wizard.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wizard.entities.Tag;

@Repository
public interface TagDAO extends JpaRepository<Tag, Integer> {
	
	List<Tag> findByTipoTag(String tipoTag);
	
	Optional<Tag> findById(Integer id);

}