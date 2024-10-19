package com.wizard;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import jakarta.persistence.EntityManager;

@SpringBootApplication
public class BitWizardApplication {
	
	@Autowired
    private EntityManager entityManager;

	public static void main(String[] args) {
		SpringApplication.run(BitWizardApplication.class, args);
	}
	
    @Bean
    public CommandLineRunner initializeFilters() {
        return args -> {
            // Abilita il filtro globale per i viaggi non cancellati
            Session session = entityManager.unwrap(Session.class);
            session.enableFilter("deletedFilter").setParameter("isDeleted", false);
            System.out.println("Filtro 'deletedFilter' abilitato all'avvio dell'applicazione.");
        };
    }

}
