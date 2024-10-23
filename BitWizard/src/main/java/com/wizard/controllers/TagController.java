package com.wizard.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wizard.entities.Tag;
import com.wizard.entities.Utente;
import com.wizard.entities.UtenteTag;
import com.wizard.services.TagServiceImpl;
import com.wizard.services.UtenteServiceImpl;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/tags")
public class TagController {
	
	@Autowired
	private TagServiceImpl tagService;
	@Autowired
	private UtenteServiceImpl utenteService;
	
	@GetMapping("/cerca")
	public ResponseEntity<List<Tag>> getAllTags() {
	    List<Tag> tags = tagService.findAll();
	    return new ResponseEntity<>(tags, HttpStatus.OK);
	}

	@GetMapping("/get")
	public ResponseEntity<?> getTagByUtenteId(HttpSession session) {

		try
		{
			Utente utente = (Utente) session.getAttribute("utenteLoggato");
			if (utente == null) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

			Set<UtenteTag> uTags = utente.getUtenteTags();
			List<Tag> tags = uTags.stream().map(UtenteTag::getTag).toList();
			if (tags.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("tag non trovate ");
			}
			return ResponseEntity.ok(tags);

		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage() + " " + e.getCause());
		}


	}

}
