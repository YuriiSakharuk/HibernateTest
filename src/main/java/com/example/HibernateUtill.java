package com.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.function.Consumer;
import java.util.function.Function;

public class HibernateUtill {

    private static EntityManagerFactory emf;

    public static void init(String unitName) {
        emf = Persistence.createEntityManagerFactory(unitName);
    }

    public static void close(){
        emf.close();
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    public static void doWithinPersistanceContext(Consumer<EntityManager> operation) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            operation.accept(em);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    public static <T> T doAndReturnWithinPersistanceContext(Function<EntityManager, T> function){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            T result = function.apply(em);
            em.getTransaction().commit();
            return result;
        } catch (Exception e){
            em.getTransaction().rollback();
            e.printStackTrace();
            return null;
        }
        finally {
            em.close();
        }
    }
}
