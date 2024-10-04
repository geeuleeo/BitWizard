package com.wizard.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wizard.entities.Immagine;
import com.wizard.services.ImmagineService;

@RestController
@RequestMapping("/api/immagini")
public class ImmagineController {

    @Autowired
    private ImmagineService immagineService;

    // API per salvare un'immagine
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImmagine(@RequestParam("file") MultipartFile file) {
        try {
            Immagine immagineSalvata = immagineService.salvaImmagine(file);
            return new ResponseEntity<>(immagineSalvata, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>("Errore durante il caricamento dell'immagine", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{idImg}")
    public ResponseEntity<?> getImmagine(@PathVariable int idImg) {
        Immagine immagine = immagineService.getImmagineById(idImg);
        return new ResponseEntity<>(immagine, HttpStatus.OK);
    }

}
