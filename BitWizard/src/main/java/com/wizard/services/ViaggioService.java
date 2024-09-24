package com.wizard.services;

import com.wizard.entities.Viaggio;
import com.wizard.repos.ViaggioDTO;

public interface ViaggioService {
	// HttpSession session
	Viaggio salvaViaggio(ViaggioDTO viaggioDTO);
	
}