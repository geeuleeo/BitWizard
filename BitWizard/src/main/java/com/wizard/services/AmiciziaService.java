package com.wizard.services;

import com.wizard.entities.Amicizia;
import com.wizard.entities.Utente;
import com.wizard.entities.Viaggio;
import com.wizard.repos.ViaggioDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AmiciziaService {
    public Amicizia creaAmicizia(Long utente1Id, Long utente2Id);

    public List<Amicizia> getAmicizie(Long utenteId);
    public List<ViaggioDTO> getViaggiDegliAmici(Long utenteId);

}
