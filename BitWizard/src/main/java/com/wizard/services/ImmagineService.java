package com.wizard.services;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.wizard.entities.Immagine;

@Service
public interface ImmagineService {

	public Immagine salvaImmagine(MultipartFile file) throws IOException;

	public Immagine getImmagineById(int idImg);
	
	public byte[] findImageById(int id);

}
