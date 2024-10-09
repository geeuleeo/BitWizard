package com.wizard.services;

import com.wizard.entities.Agenzia;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AgenziaService {


    List<Agenzia> getAgenzie();


}
