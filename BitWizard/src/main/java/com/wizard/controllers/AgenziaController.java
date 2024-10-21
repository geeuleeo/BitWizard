package com.wizard.controllers;


import com.wizard.DTO.AgenziaRegistrazioneDTO;
import com.wizard.entities.Agenzia;
import com.wizard.repos.AgenziaDAO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/agenzia")
public class AgenziaController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AgenziaDAO agenziaDAO;




    @PostMapping("/signup")
    public ResponseEntity<?>  signUp (@Valid@RequestBody AgenziaRegistrazioneDTO agenziaRegistrazioneDTO){

       if (agenziaDAO.findAgenziaByPartitaIva(agenziaRegistrazioneDTO.getPartitaIVA()).isPresent()){
           return
           ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Partita IVA gia presente nel database ");
       }

        Agenzia agenzia = new Agenzia();
       agenzia.setPartitaIva(agenziaRegistrazioneDTO.getPartitaIVA());
       agenzia.setNome(agenziaRegistrazioneDTO.getNome());
       agenzia.setPassword(passwordEncoder.encode(agenziaRegistrazioneDTO.getPassword()));
       agenzia.setDescrizione(agenziaRegistrazioneDTO.getDescrizione());


       agenziaDAO.save(agenzia);

       return ResponseEntity.status(HttpStatus.CREATED).body("Registrazione avvenuta con successo ");

    }

}
