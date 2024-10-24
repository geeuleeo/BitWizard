package com.wizard.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.wizard.DTO.AmicoDTO;
import com.wizard.entities.Amicizia;
import com.wizard.entities.Utente;
import com.wizard.repos.ViaggioDTO;

@Service
public interface AmiciziaService {
	
	public Amicizia creaAmicizia(Long utente1Id, Long utente2Id);
    public List<Amicizia> getAmicizie(Long utenteId);
    public List<ViaggioDTO> getViaggiDegliAmici(Long utenteId);
	public void rifiutaRichiesta(Long utenteId, Long utenteId2);
	public void accettaRichiesta(Long utenteId, Long utenteId2);
	public AmicoDTO toDTO(Utente amico);

}
