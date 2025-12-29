package utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DBUtils {
    private static final EntityManagerFactory factory;

    static {
        Map<String, Object> props = new HashMap<>();

        String url = System.getenv("JDBC_URL");
        String user = System.getenv("JDBC_USER");
        String pass = System.getenv("JDBC_PASS");

        if (url != null)  props.put("hibernate.connection.url", url);
        if (user != null) props.put("hibernate.connection.username", user);
        if (pass != null) props.put("hibernate.connection.password", pass);

        factory = Persistence.createEntityManagerFactory("core", props);
    }

    public static EntityManager getEntityManager() {
        EntityManager em = factory.createEntityManager();
        return em;
    }

    public static <T> T withTx(Function<EntityManager, T> fn) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T res = fn.apply(em);
            tx.commit();
            return res;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            try { em.close(); } catch (Exception ignore) {}
        }
    }

    public static void withTxVoid(java.util.function.Consumer<EntityManager> fn) {
        withTx(em -> { fn.accept(em); return null; });
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
