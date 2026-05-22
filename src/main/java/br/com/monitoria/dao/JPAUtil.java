package br.com.monitoria.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    private static final EntityManagerFactory factory =
            Persistence.createEntityManagerFactory("monitoriaPU");

    public static EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    public static void fechar() {
        factory.close();
    }
}