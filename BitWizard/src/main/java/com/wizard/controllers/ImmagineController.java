package com.wizard.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wizard.entities.Immagine;
import com.wizard.entities.Utente;
import com.wizard.services.ImmagineService;
import com.wizard.services.UtenteService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/immagini")
public class ImmagineController {

    @Autowired
    private ImmagineService immagineService;
    
    @Autowired
    private UtenteService utenteService;

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
    
    @GetMapping("/utente/immagine")
    public ResponseEntity<byte[]> getImmagineProfilo(HttpSession session) {
    	Utente utente = (Utente) session.getAttribute("utenteLoggato");

        byte[] immagine = utente.getImmagine().getImg();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        
        return new ResponseEntity<>(immagine, headers, HttpStatus.OK);
    }
    
    @GetMapping("/viaggio/immagine")
    public ResponseEntity<byte[]> getImmagineCopertina(int id) {

        byte[] immagine = immagineService.findImageById(id);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        
        return new ResponseEntity<>(immagine, headers, HttpStatus.OK);
    }

}