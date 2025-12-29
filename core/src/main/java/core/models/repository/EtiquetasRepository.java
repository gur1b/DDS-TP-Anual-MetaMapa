package core.models.repository;

import core.models.entities.hecho.Etiqueta;
import utils.DBUtils;

import javax.persistence.EntityManager;

public class EtiquetasRepository extends JpaRepositoryBase<Etiqueta, Integer> {

    private static volatile EtiquetasRepository instance;

    private EtiquetasRepository() {
        super(Etiqueta.class, DBUtils::getEntityManager, Etiqueta::getId);
    }

    public static EtiquetasRepository getInstance() {
        if (instance == null) {
            synchronized (EtiquetasRepository.class) {
                if (instance == null) instance = new EtiquetasRepository();
            }
        }
        return instance;
    }

    public Etiqueta getEtiqueta(Integer idEtiqueta){
        return findById(idEtiqueta);
    }

    /** Busca por nombre (case-insensitive, trim). Si no existe, crea y devuelve. */
    public Etiqueta buscarOCrearPorNombre(String nombre) {
        if (nombre == null) return null;

        String normalized = nombre.trim();
        if (normalized.isBlank()) return null;

        EntityManager em = DBUtils.getEntityManager();
        try {
            // 1) buscar existente
            Etiqueta existente = em.createQuery(
                            "select e from etiqueta e where lower(trim(e.nombre)) = :n",
                            Etiqueta.class
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

            Etiqueta nueva = new Etiqueta();
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