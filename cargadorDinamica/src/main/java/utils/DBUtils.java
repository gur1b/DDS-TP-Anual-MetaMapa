package utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class DBUtils {

    private static final EntityManagerFactory factory;

    static {
        Map<String, String> overrides = new HashMap<>();

        // 1️⃣ Prioridad: variables de entorno (Docker)
        putIfEnv(overrides, "hibernate.connection.url", "SPRING_DATASOURCE_URL");
        putIfEnv(overrides, "hibernate.connection.username", "SPRING_DATASOURCE_USERNAME");
        putIfEnv(overrides, "hibernate.connection.password", "SPRING_DATASOURCE_PASSWORD");

        // 2️⃣ Opcional: variables más genéricas
        putIfEnv(overrides, "hibernate.connection.url", "DB_URL");
        putIfEnv(overrides, "hibernate.connection.username", "DB_USER");
        putIfEnv(overrides, "hibernate.connection.password", "DB_PASSWORD");

        // 3️⃣ Crear EMF con overrides
        factory = overrides.isEmpty()
                ? Persistence.createEntityManagerFactory("cargador_dinamica")
                : Persistence.createEntityManagerFactory("cargador_dinamica", overrides);
    }

    private static void putIfEnv(Map<String, String> map, String jpaKey, String envKey) {
        String value = System.getenv(envKey);
        if (value != null && !value.isBlank()) {
            map.put(jpaKey, value.trim());
        }
    }

    public static EntityManager getEntityManager() {
        EntityManager em = factory.createEntityManager();
        return em;
    }

    public static void comenzarTransaccion(EntityManager em) {
        EntityTransaction tx = em.getTransaction();
        if (!tx.isActive()) {
            tx.begin();
        }
    }

    public static void commit(EntityManager em) {
        EntityTransaction tx = em.getTransaction();
        if (tx.isActive()) {
            tx.commit();
        }
    }

    public static void rollback(EntityManager em) {
        EntityTransaction tx = em.getTransaction();
        if (tx.isActive()) {
            tx.rollback();
        }
    }

}