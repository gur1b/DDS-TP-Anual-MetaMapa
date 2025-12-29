package core.models.repository;

import core.models.entities.colecciones.Coleccion;
import core.models.entities.hecho.Categoria;
import utils.DBUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class CategoriaRepository extends JpaRepositoryBase<Categoria, Integer>{

        private static volatile CategoriaRepository instance;

        private CategoriaRepository() {
        super(Categoria.class, DBUtils::getEntityManager, Categoria::getId);
         }

        public static CategoriaRepository getInstance() {
            if (instance == null) {
                synchronized (CategoriaRepository.class) {
                    if (instance == null) {
                        instance = new CategoriaRepository();
                    }
                }
            }
            return instance;
        }


    public Categoria buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) return null;

        EntityManager em = DBUtils.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM categoria c WHERE LOWER(TRIM(c.nombre)) = LOWER(:nombre)",
                            Categoria.class)
                    .setParameter("nombre", nombre.trim())
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }


    public boolean existe(String nombre) {
        return buscarPorNombre(nombre) != null;
    }

    public Categoria buscarOCrearPorNombre(String nombre) {
        if (nombre == null) return null;

        String normalized = nombre.trim();
        if (normalized.isBlank()) return null;

        EntityManager em = DBUtils.getEntityManager();
        try {
            // 1) buscar existente
            Categoria existente = em.createQuery(
                            "select c from categoria c where lower(trim(c.nombre)) = :n",
                            Categoria.class
                    )
                    .setParameter("n", normalized.toLowerCase())
                    .setMaxResults(1)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (existente != null) return existente;

            // 2) crear
            DBUtils.comenzarTransaccion(em);

            Categoria nueva = new Categoria();
            nueva.setNombre(normalized);
            em.persist(nueva);
            em.flush();

            DBUtils.commit(em);
            return nueva;

        } catch (RuntimeException ex) {
            DBUtils.rollback(em);
            throw ex;
        } finally {
            try { em.close(); } catch (Exception ignore) {}
        }
    }


    }


