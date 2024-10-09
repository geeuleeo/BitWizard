package com.wizard.services;

import com.wizard.entities.Agenzia;
import com.wizard.repos.AgenziaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgenziaServiceImpl implements AgenziaService {
    @Autowired
    AgenziaDAO agenziaDAO;


    @Override
    public List<Agenzia> getAgenzie() {
         return agenziaDAO.findAll();
    }
}
