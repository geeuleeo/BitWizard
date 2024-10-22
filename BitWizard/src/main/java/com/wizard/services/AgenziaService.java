package com.wizard.services;

import com.wizard.entities.Agenzia;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AgenziaService {


    List<Agenzia> getAgenzie();
    Optional<Agenzia> getAgenziaByIVA(String IVA);


}
