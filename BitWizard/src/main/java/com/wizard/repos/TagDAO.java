package com.wizard.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wizard.entities.Tag;

@Repository
public interface TagDAO extends JpaRepository<Tag, Long> {
	
	List<Tag> findByTipoTag(String tipoTag);
	
	Optional<Tag> findById(Long id);

	//List<Tag> findAllById(List<Long> tagIds);
	
	/*
	@Query("SELECT t FROM Tag t WHERE t.tagId IN :ids")
    List<Tag> findTagsByTagIds(@Param("ids") List<Long> ids);
    */

}