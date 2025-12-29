package core.models.repository;

import core.models.entities.colecciones.criterios.*;
import core.models.entities.hecho.Categoria;
import core.models.entities.hecho.Coordenadas;
import utils.DBUtils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;

public class CriteriosRepository extends JpaRepositoryBase<Criterio, Integer> {

    private static volatile CriteriosRepository instance;

    private CriteriosRepository() {
        super(Criterio.class, DBUtils::getEntityManager, Criterio::getId); // ajustá getId() si tu Criterio usa otro tipo
    }

    public static CriteriosRepository getInstance() {
        if (instance == null) {
            synchronized (CriteriosRepository.class) {
                if (instance == null) instance = new CriteriosRepository();
            }
        }
        return instance;
    }

    public Criterio getCriterio(Integer id) {
        return findById(id);
    }

    // =====================================================
    //  DESCRIPCION  (palabraClave)
    // =====================================================
    public CriterioDescripcion buscarDescripcion(String palabra) {
        if (palabra == null || palabra.isBlank()) return null;

        EntityManager em = DBUtils.getEntityManager();
        try {
            return (CriterioDescripcion) em.createQuery(
                            "SELECT c FROM CriterioDescripcion c " +
                                    "WHERE LOWER(TRIM(c.palabraClave)) = LOWER(:palabra)",
                            CriterioDescripcion.class)
                    .setParameter("palabra", palabra.trim())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }

    // =====================================================
    //  NOMBRE  (palabraClave)
    // =====================================================
    public CriterioNombre buscarNombre(String palabra) {
        if (palabra == null || palabra.isBlank()) return null;

        EntityManager em = DBUtils.getEntityManager();
        try {
            return (CriterioNombre) em.createQuery(
                            "SELECT c FROM CriterioNombre c " +
                                    "WHERE LOWER(TRIM(c.palabraClave)) = LOWER(:palabra)",
                            CriterioNombre.class)
                    .setParameter("palabra", palabra.trim())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }

    // =====================================================
    //  CATEGORIA  (Categoria asociada)
    // =====================================================
    public CriterioCategoria buscarCategoria(Categoria categoria) {
        if (categoria == null) return null;

        EntityManager em = DBUtils.getEntityManager();
        try {
            return (CriterioCategoria) em.createQuery(
                            "SELECT c FROM CriterioCategoria c " +
                                    "WHERE c.categoria = :categoria",
                            CriterioCategoria.class)
                    .setParameter("categoria", categoria)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }

    // =====================================================
    //  UBICACION  (Coordenadas asociadas)
    // =====================================================
    public CriterioUbicacion buscarUbicacion(Coordenadas coordenadas) {
        if (coordenadas == null) return null;

        EntityManager em = DBUtils.getEntityManager();
        try {
            return (CriterioUbicacion) em.createQuery(
                            "SELECT c FROM CriterioUbicacion c " +
                                    "WHERE c.coordenadas = :coords",
                            CriterioUbicacion.class)
                    .setParameter("coords", coordenadas)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }

    // =====================================================
    //  FECHA SUCESO  (fechaInicio / fechaFin)
    // =====================================================
    public CriterioFechaSuceso buscarFechaSuceso(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) return null;

        EntityManager em = DBUtils.getEntityManager();
        try {
            return (CriterioFechaSuceso) em.createQuery(
                            "SELECT c FROM CriterioFechaSuceso c " +
                                    "WHERE c.fechaInicio = :desde AND c.fechaFin = :hasta",
                            CriterioFechaSuceso.class)
                    .setParameter("desde", desde)
                    .setParameter("hasta", hasta)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }

    // =====================================================
    //  FECHA CARGA  (fechaInicio / fechaFin)
    // =====================================================
    public CriterioFechaCarga buscarFechaCarga(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) return null;

        EntityManager em = DBUtils.getEntityManager();
        try {
            return (CriterioFechaCarga) em.createQuery(
                            "SELECT c FROM CriterioFechaCarga c " +
                                    "WHERE c.fechaInicio = :desde AND c.fechaFin = :hasta",
                            CriterioFechaCarga.class)
                    .setParameter("desde", desde)
                    .setParameter("hasta", hasta)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }

    // =====================================================
    //  FECHA MODIFICACION  (fechaInicio / fechaFin)
    // =====================================================
    public CriterioFechaModificacion buscarFechaModificacion(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) return null;

        EntityManager em = DBUtils.getEntityManager();
        try {
            return (CriterioFechaModificacion) em.createQuery(
                            "SELECT c FROM CriterioFechaModificacion c " +
                                    "WHERE c.fechaInicio = :desde AND c.fechaFin = :hasta",
                            CriterioFechaModificacion.class)
                    .setParameter("desde", desde)
                    .setParameter("hasta", hasta)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }

    // =====================================================
    //  HORA SUCESO  (horaInicio / horaFin)
    // =====================================================
    public CriterioHoraSuceso buscarHoraSuceso(LocalTime desde, LocalTime hasta) {
        if (desde == null || hasta == null) return null;

        EntityManager em = DBUtils.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM CriterioHoraSuceso c " +
                                    "WHERE c.horaInicio = :desde AND c.horaFin = :hasta",
                            CriterioHoraSuceso.class)
                    .setParameter("desde", desde)
                    .setParameter("hasta", hasta)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }
}