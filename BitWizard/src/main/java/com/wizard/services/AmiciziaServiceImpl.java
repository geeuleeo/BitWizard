package com.wizard.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wizard.DTO.AmicoDTO;
import com.wizard.entities.Amicizia;
import com.wizard.entities.Amicizia.StatoAmicizia;
import com.wizard.entities.ChiaveAmicizia;
import com.wizard.entities.Utente;
import com.wizard.entities.Viaggio;
import com.wizard.repos.AmiciziaDAO;
import com.wizard.repos.UtenteDAO;
import com.wizard.repos.ViaggioDAO;
import com.wizard.repos.ViaggioDTO;

@Service
public class AmiciziaServiceImpl implements AmiciziaService {
	
    @Autowired
    AmiciziaDAO amiciziaDAO;
    
    @Autowired
    UtenteDAO utenteDAO;
    
    @Autowired
    ViaggioDAO viaggioDAO;
    
    @Override
    public Amicizia creaAmicizia(Long utente1Id, Long utente2Id) {

        ChiaveAmicizia chiaveAmicizia = new ChiaveAmicizia();
        chiaveAmicizia.setUtenteId1(utente1Id);
        chiaveAmicizia.setUtenteId2(utente2Id);
        Date data = new Date();

        Amicizia amicizia = new Amicizia(chiaveAmicizia,utente1Id,utente2Id,data, null);


        return amiciziaDAO.save(amicizia);

    }
    
    public void inviaRichiestaAmicizia(Long idUtente1, Long idUtente2) {
        Amicizia amicizia = new Amicizia();
        amicizia.setUtente_id1(idUtente1);
        amicizia.setUtente_id2(idUtente2);
        amicizia.setDataAmicizia(new Date());
        amicizia.setStato(StatoAmicizia.IN_ATTESA);
        amiciziaDAO.save(amicizia);
    }

    public void accettaRichiesta(Long riceveRichiestaId, Long inviaRichiestaId) {
        Amicizia amicizia = amiciziaDAO.findById(new ChiaveAmicizia(riceveRichiestaId, inviaRichiestaId))
            .orElseThrow(() -> new IllegalArgumentException("Amicizia non trovata"));
        amicizia.setStato(StatoAmicizia.ACCETTATO);
        amiciziaDAO.save(amicizia);
    }

    public void rifiutaRichiesta(Long riceveRichiestaId, Long inviaRichiestaId) {
        Amicizia amicizia = amiciziaDAO.findById(new ChiaveAmicizia(riceveRichiestaId, inviaRichiestaId))
            .orElseThrow(() -> new IllegalArgumentException("Amicizia non trovata"));
        amicizia.setStato(StatoAmicizia.RIFIUTATO);
        amiciziaDAO.save(amicizia);
    }

    @Override
    public List<Amicizia> getAmicizie(Long utenteId) {


        return amiciziaDAO.findAmicizieByUtenteId(utenteId);
    }

    @Override
    public List<ViaggioDTO> getViaggiDegliAmici(Long utenteId) {

        List<Amicizia> amici = amiciziaDAO.findAmicizieByUtenteId(utenteId);

        List<Long> listaIds = new ArrayList<>();

        for (Amicizia amico : amici) {

            listaIds.add( amico.getChiave().getUtenteId1());
            listaIds.add( amico.getChiave().getUtenteId2());

        }
        List<Long> listaIdAmici = new ArrayList<>();
        for (Long id : listaIds) {

            if (!id.equals(utenteId)) {
                    listaIdAmici.add(id);
            }

        }
        List<ViaggioDTO> viaggi = new ArrayList<>();


        for (Long id : listaIdAmici) {
            List<Viaggio> viaggiUtenti = viaggioDAO.findViaggiByPartecipanteId(id);
            if (!viaggiUtenti.isEmpty()) {

                for (Viaggio v : viaggiUtenti) {
                    viaggi.add(convertToDTO(v));
                }

            }

        }
        return viaggi;
    }
    
    public AmicoDTO toDTO(Utente amico) {
    	
        AmicoDTO amicoDTO = new AmicoDTO();

        amicoDTO.setNome(amico.getNome());
        amicoDTO.setCognome(amico.getCognome());
        
        if (amico.getImmagine() != null) {
            amicoDTO.setImmagine(amico.getImmagine().getImg());
        }

        return amicoDTO;
    }

    private ViaggioDTO convertToDTO(Viaggio viaggio) {
        ViaggioDTO dto = new ViaggioDTO();
        dto.setViaggioId(viaggio.getViaggioId());
        dto.setNome(viaggio.getNome());
        dto.setLuogoPartenza(viaggio.getLuogoPartenza());
        dto.setLuogoArrivo(viaggio.getLuogoArrivo());
        dto.setDataPartenza(viaggio.getDataPartenza());
        dto.setDataRitorno(viaggio.getDataRitorno());
        dto.setPrezzo(viaggio.getPrezzo());

        return dto;
    }
}
