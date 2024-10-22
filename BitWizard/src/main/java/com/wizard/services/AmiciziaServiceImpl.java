package com.wizard.services;

import com.wizard.entities.Amicizia;
import com.wizard.entities.ChiaveAmicizia;
import com.wizard.entities.Utente;
import com.wizard.entities.Viaggio;
import com.wizard.repos.AmiciziaDAO;
import com.wizard.repos.UtenteDAO;
import com.wizard.repos.ViaggioDAO;
import com.wizard.repos.ViaggioDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

        Amicizia amicizia = new Amicizia(chiaveAmicizia,utente1Id,utente2Id,data);


        return amiciziaDAO.save(amicizia);

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
