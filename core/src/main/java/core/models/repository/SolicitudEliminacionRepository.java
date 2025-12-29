package core.models.repository;

import core.models.entities.colecciones.Coleccion;
import core.models.entities.hecho.Hecho;
import core.models.entities.solicitud.SolicitudDeEliminacion;
import utils.DBUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class SolicitudEliminacionRepository extends JpaRepositoryBase<SolicitudDeEliminacion, Integer> {
    private static volatile SolicitudEliminacionRepository instance;

    private SolicitudEliminacionRepository() {
        super(SolicitudDeEliminacion.class, DBUtils::getEntityManager, SolicitudDeEliminacion::getId);
    }

    public static SolicitudEliminacionRepository getInstance() {
        if (instance == null) {
            synchronized (SolicitudEliminacionRepository.class) {
                if (instance == null) {
                    instance = new SolicitudEliminacionRepository();
                }
            }
        }
        return instance;
    }
    public List<SolicitudDeEliminacion> obtenerTodasConHechoYContribuyente() {
        EntityManager em = DBUtils.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<SolicitudDeEliminacion> cq = cb.createQuery(SolicitudDeEliminacion.class);
            Root<SolicitudDeEliminacion> root = cq.from(SolicitudDeEliminacion.class);

            // fetch del hecho
            Fetch<SolicitudDeEliminacion, Hecho> hechoFetch = root.fetch("hecho", JoinType.LEFT);
            // fetch del contribuyente del hecho
            hechoFetch.fetch("contribuyente", JoinType.LEFT);

            cq.select(root).distinct(true);
            cq.orderBy(cb.desc(root.get("id")));

            TypedQuery<SolicitudDeEliminacion> q = em.createQuery(cq);
            return q.getResultList();
        } finally {
            em.close();
        }
    }



}
