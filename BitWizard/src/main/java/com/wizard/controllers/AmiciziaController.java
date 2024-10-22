package com.wizard.controllers;


import com.wizard.entities.Amicizia;
import com.wizard.entities.Utente;
import com.wizard.repos.ViaggioDTO;
import com.wizard.services.AmiciziaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("api/amicizia")
public class AmiciziaController {
    @Autowired
    AmiciziaService amiciziaService;

    @PostMapping("crea/{utenteId1}/{utenteId2}")
    public ResponseEntity<Amicizia> creaAmicizia(@PathVariable Long utenteId1,@PathVariable Long utenteId2) {

       Amicizia amicizia =  amiciziaService.creaAmicizia(utenteId1,utenteId2);

       if (amicizia == null) {
           return ResponseEntity.notFound().build();
       }

        return ResponseEntity.ok(amicizia);
    }

    @GetMapping("trovaAmici")
    public ResponseEntity<List<Amicizia>> trovaAmici(HttpSession session) {

        Utente utente = (Utente) session.getAttribute("utenteLoggato");

        List<Amicizia> amici = amiciziaService.getAmicizie(utente.getUtenteId());

        if(amici == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(amici);
    }
    @GetMapping("trovaViaggiConAmici")
    public ResponseEntity<List<ViaggioDTO>> trovaViaggiConAmici(HttpSession session) {

            Utente utente = (Utente) session.getAttribute("utenteLoggato");

            List<ViaggioDTO> viaggiAmici = amiciziaService.getViaggiDegliAmici(utente.getUtenteId());
            if(viaggiAmici == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(viaggiAmici);

    }


}
