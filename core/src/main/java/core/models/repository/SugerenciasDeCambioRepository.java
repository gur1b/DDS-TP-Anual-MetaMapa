package core.models.repository;

import core.models.entities.hecho.Hecho;
import core.models.entities.hecho.SugerenciaDeCambio;
import utils.DBUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class SugerenciasDeCambioRepository extends JpaRepositoryBase<SugerenciaDeCambio, Integer> {

    private static volatile SugerenciasDeCambioRepository instance;

    private SugerenciasDeCambioRepository() {
        super(SugerenciaDeCambio.class, DBUtils::getEntityManager, SugerenciaDeCambio::getId);
    }

    public static SugerenciasDeCambioRepository getInstance() {
        if (instance == null) {
            synchronized (SugerenciasDeCambioRepository.class) {
                if (instance == null) instance = new SugerenciasDeCambioRepository();
            }
        }
        return instance;
    }

    public SugerenciaDeCambio getSugerenciaDeCambio(Integer idSugerenciaDeCambio){
        return findById(idSugerenciaDeCambio);
    }

    public List<SugerenciaDeCambio> obtenerTodasConTodoInicializado() {
        EntityManager em = DBUtils.getEntityManager();
        try {
                // 1) Traemos sugerencias + (hecho, contribuyente, categorias) + etiquetas de la sugerencia
                List<SugerenciaDeCambio> sugerencias = em.createQuery("""
                    SELECT DISTINCT s
                    FROM sugerencia_de_cambio s
                    LEFT JOIN FETCH s.hecho h
                    LEFT JOIN FETCH h.contribuyente
                    LEFT JOIN FETCH s.categoria
                    LEFT JOIN FETCH h.categoria
                    LEFT JOIN FETCH s.etiquetas
                    WHERE s.aprobada IS NULL
                    ORDER BY s.id DESC
                """, SugerenciaDeCambio.class).getResultList();

                // 2) Inicializamos etiquetas del hecho (sin traer de nuevo todo)
                //    Sacamos ids de hechos presentes
                List<Integer> idsHechos = sugerencias.stream()
                        .map(SugerenciaDeCambio::getHecho)
                        .filter(java.util.Objects::nonNull)
                        .map(Hecho::getId)
                        .distinct()
                        .toList();

                if (!idsHechos.isEmpty()) {
                    em.createQuery("""
                        SELECT DISTINCT h
                        FROM hecho h
                        LEFT JOIN FETCH h.etiquetas
                        WHERE h.id IN :ids
                    """, Hecho.class)
                            .setParameter("ids", idsHechos)
                            .getResultList();
                    // Nota: no guardamos el resultado porque Hibernate lo deja en el persistence context
                    // y ya quedan inicializadas las etiquetas de esos hechos.
                }

                return sugerencias;
        } finally {
            em.close();
        }
    }

    public SugerenciaDeCambio getByIdConEtiquetas(Integer id) {
        if (id == null) return null;

        EntityManager em = DBUtils.getEntityManager();
        try {
            return em.createQuery("""
            select distinct s
            from sugerencia_de_cambio s
            left join fetch s.etiquetas
            where s.id = :id
        """, SugerenciaDeCambio.class)
                    .setParameter("id", id)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }



}
