package com.wizard.customs;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;

@Aspect
@Component
public class FilterAspect {

    @Autowired
    private EntityManager entityManager;

    @Before("execution(* com.wizard.services.ViaggioService.*(..))")
    public void abilitaFiltroViaggiServiceNonCancellati() {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("deletedFilter").setParameter("isDeleted", false);
        System.out.println("Filtro 'deletedFilter' abilitato tramite Aspect.");
    }
    
    @Before("execution(* com.wizard.services.ViaggioServiceImpl.*(..))")
    public void abilitaFiltroViaggiNonCancellati() {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("deletedFilter").setParameter("isDeleted", false);
        System.out.println("Filtro 'deletedFilter' abilitato tramite Aspect.");
    }
}
