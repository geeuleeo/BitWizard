package com.wizard.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wizard.entities.Tag;
import com.wizard.services.TagServiceImpl;

@RestController
@RequestMapping("/api/tags")
public class TagController {
	
	@Autowired
	private TagServiceImpl tagService;
	
	@GetMapping("/cerca")
	public ResponseEntity<List<Tag>> getAllTags() {
	    List<Tag> tags = tagService.findAll();
	    return new ResponseEntity<>(tags, HttpStatus.OK);
	}

}
