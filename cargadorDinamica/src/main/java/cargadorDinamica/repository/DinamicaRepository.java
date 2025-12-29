package cargadorDinamica.repository;

import cargadorDinamica.model.HechoAIntegrarDTO;
import utils.DBUtils;

import javax.persistence.EntityManager;
import java.util.*;

public class DinamicaRepository extends JpaRepositoryBase<HechoAIntegrarDTO, String> {
    private static volatile DinamicaRepository instance;

    private DinamicaRepository() {
        super(HechoAIntegrarDTO.class, DBUtils::getEntityManager, HechoAIntegrarDTO::getHash);
    }


    public static DinamicaRepository getInstance() {
        if (instance == null) {
            synchronized (DinamicaRepository.class) {
                if (instance == null) {
                    instance = new DinamicaRepository();
                }
            }
        }
        return instance;
    }

    public List<HechoAIntegrarDTO> getHechosNoProcesados() {
        EntityManager em = DBUtils.getEntityManager();
        List<HechoAIntegrarDTO> hechos = new ArrayList<>();

        try {
            // Traer los hechos no procesados (null o false)
            hechos = em.createQuery(
                            "SELECT h FROM HechoAIntegrarDTO h WHERE h.fueExtraido IS NULL OR h.fueExtraido = false",
                            HechoAIntegrarDTO.class)
                    .getResultList();

            if (hechos.isEmpty()) {
                return hechos; // nada que hacer
            }

            // Inicializar colecciones LAZY mientras la sesión sigue abierta
            for (HechoAIntegrarDTO h : hechos) {
                if (h.getEtiquetas() != null) {
                    h.getEtiquetas().size();   // fuerza el load
                }
                if (h.getMultimedia() != null) {
                    h.getMultimedia().size();  // fuerza el load
                }
            }

            // Marcar como procesados
            em.getTransaction().begin();
            for (HechoAIntegrarDTO h : hechos) {
                h.setFueExtraido(true);
                em.merge(h);
            }
            em.getTransaction().commit();

            return hechos;
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    @Override
    public HechoAIntegrarDTO save(HechoAIntegrarDTO hecho) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            DBUtils.comenzarTransaccion(em);

            if (hecho.getEtiquetas() == null) {
                hecho.setEtiquetas(List.of());
            }
            if (hecho.getMultimedia() == null) {
                hecho.setMultimedia(List.of());
            }

            // si ya existe el hash, actualizamos en vez de romper
            HechoAIntegrarDTO existente = em.find(HechoAIntegrarDTO.class, hecho.getHash());
            if (existente == null) {
                em.persist(hecho);
            } else {
                // merge para actualizar etiquetas/multimedia también
                em.merge(hecho);
            }

            DBUtils.commit(em);
            return hecho;
        } catch (RuntimeException e) {
            DBUtils.rollback(em);
            throw e;
        } finally {
            try { em.close(); } catch (Exception ignore) {}
        }
    }
}
