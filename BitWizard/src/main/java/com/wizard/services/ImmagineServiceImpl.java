package com.wizard.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.wizard.entities.Immagine;
import com.wizard.repos.ImmagineDAO;

@Service
public class ImmagineServiceImpl implements ImmagineService{
	
    @Autowired
    private ImmagineDAO immagineRepository;

    // Metodo per salvare un'immagine nel database
    public Immagine salvaImmagine(MultipartFile file) throws IOException {
        Immagine immagine = new Immagine();
        immagine.setImg(file.getBytes()); // Salva il contenuto binario del file

        return immagineRepository.save(immagine); // Salva l'immagine nel database
    }

    // Metodo per ottenere un'immagine dal database
    public Immagine getImmagineById(int idImg) {
        return immagineRepository.findById(idImg)
                .orElseThrow(() -> new RuntimeException("Immagine non trovata con ID: " + idImg));
    }
    
    public byte[] findImageById(int id) {
        return immagineRepository.findByIdImg(id)
                .orElseThrow(() -> new RuntimeException("Immagine non trovata")).getImg();
    }

}
