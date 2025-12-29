
package core.models.repository;

import core.api.DTO.ColeccionConTodoDTO;
import core.api.DTO.ColeccionDTO;
import core.api.DTO.FuenteDTO;
import core.api.DTO.HechoResumenDTO;
import core.api.DTO.criterio.CriterioDTO;
import core.models.entities.colecciones.*;
import core.models.entities.colecciones.criterios.Criterio;
import core.models.entities.fuentes.Fuente;
import core.models.entities.hecho.Etiqueta;
import core.models.entities.hecho.Hecho;
import org.hibernate.Hibernate;
import utils.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import java.util.*;

public class ColeccionesRepository extends JpaRepositoryBase<Coleccion, Integer> {

    private static volatile ColeccionesRepository instance;
    private static final Logger logger = LoggerFactory.getLogger(ColeccionesRepository.class);
    private ColeccionesRepository() {
        super(Coleccion.class, DBUtils::getEntityManager, Coleccion::getId);
    }

    public static ColeccionesRepository getInstance() {
        if (instance == null) {
            synchronized (ColeccionesRepository.class) {
                if (instance == null) instance = new ColeccionesRepository();
            }
        }
        return instance;
    }

    public Coleccion getColeccion(Integer idColeccion) {
        return findById(idColeccion);
    }

    public List<Criterio> getCriterios(Integer idColeccion) {
        Coleccion coleccion = findById(idColeccion);
        /*
         EntityManager em = emSupplier.get();
        try {
            String ql = "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e";
            return em.createQuery(ql, Long.class).getSingleResult();
        } finally {
            em.close();
        }
         */

        //SELECT criterios FROM coleccion c WHERE  idColeccion = c.id_coleccion

        return coleccion.getCriterioDePertenencia();
    }

    public void agregarHechosAColeccion(int idColeccion, List<Integer> idsHechos) {
        if (idsHechos == null || idsHechos.isEmpty()) return;

        DBUtils.withTxVoid(em -> {
            Coleccion coleccion = em.find(Coleccion.class, idColeccion, LockModeType.PESSIMISTIC_WRITE);
            if (coleccion == null) throw new IllegalArgumentException("No existe la Coleccion con id=" + idColeccion);

            if (coleccion.getHechos() == null) coleccion.setHechos(new ArrayList<>());

            Set<Integer> existentes = new HashSet<>(
                    em.createQuery("select h.id from coleccion c join c.hechos h where c.id = :idCol", Integer.class)
                            .setParameter("idCol", idColeccion)
                            .getResultList()
            );

            for (Integer idHecho : idsHechos) {
                if (idHecho == null || existentes.contains(idHecho)) continue;
                Hecho ref = em.getReference(Hecho.class, idHecho);
                coleccion.getHechos().add(ref);
                existentes.add(idHecho);
            }
        });
    }


    //VER SI SE TRAE LOS VISIBLES O QUE CARAJEANOS!!! (CONSULTAR EQUIPO DINAMITA)
    //EN QUE QUEDO LO DE LA TABLA ESA DE VISIBLES O NO. PORQUE SI NO HAY QUE CAMBIAR.

    public Optional<Coleccion> findByIdFetchHechosYContribuyente(Integer idColeccion) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            DBUtils.comenzarTransaccion(em);

            List<Coleccion> res = em.createQuery("""
                    SELECT DISTINCT c
                    FROM coleccion c
                    LEFT JOIN FETCH c.hechos h
                    LEFT JOIN FETCH h.contribuyente
                    WHERE c.id = :id
                """, Coleccion.class)
                    .setParameter("id", idColeccion)
                    .getResultList();

            Coleccion c = res.isEmpty() ? null : res.get(0);

            if (c != null) {
                // Fuerzo init de lo que después se usa fuera del EM
                c.getHechos().forEach(h -> {
                    Hibernate.initialize(h.getEtiquetas());
                    Hibernate.initialize(h.getMultimedia());
                });
            }

            DBUtils.commit(em);
            return Optional.ofNullable(c);

        } catch (Exception e) {
            DBUtils.rollback(em);
            throw e;
        } finally {
            try { em.close(); } catch (Exception ignore) {}
        }
    }


    public Optional<Coleccion> findByIdFetchHechosVisiblesYContribuyente(Integer idColeccion) {
        return DBUtils.withTx(em -> {
            List<Coleccion> res = em.createQuery("""
        SELECT DISTINCT c
        FROM coleccion c
        LEFT JOIN FETCH c.hechosVisibles hv
        LEFT JOIN FETCH hv.contribuyente
        WHERE c.id = :id
      """, Coleccion.class)
                    .setParameter("id", idColeccion)
                    .getResultList();

            Coleccion c = res.isEmpty() ? null : res.get(0);
            if (c != null) {
                c.getHechosVisibles().forEach(hv -> {
                    Hibernate.initialize(hv.getEtiquetas());
                    Hibernate.initialize(hv.getMultimedia());
                });
                if (c.getAlgoritmoConsenso() != null) c.getAlgoritmoConsenso().toString();
            }
            return Optional.ofNullable(c);
        });
    }


    public Optional<Coleccion> findByIdFetchFuentes(Integer idColeccion) {
        return DBUtils.withTx(em -> {
            List<Coleccion> res = em.createQuery("""
        SELECT DISTINCT c
        FROM coleccion c
        LEFT JOIN FETCH c.fuentes f
        WHERE c.id = :id
      """, Coleccion.class)
                    .setParameter("id", idColeccion)
                    .getResultList();
            return res.stream().findFirst();
        });
    }


    public List<ColeccionDTO> listarColeccionesDTOConCantidadHechos() {
        EntityManager em = DBUtils.getEntityManager();
        try {
            // BASE
            List<Object[]> bases = em.createQuery("""
            SELECT c.id,
                   c.titulo,
                   c.descripcionColeccion,
                   c.identificadorHandle,
                   c.modoDeNavegacion,
                   c.algoritmoConsenso
            FROM coleccion c
            ORDER BY c.id DESC
        """, Object[].class).getResultList();

            // CANTIDAD DE HECHOS
            List<Object[]> rows = em.createQuery("""
            SELECT c.id, COUNT(h)
            FROM coleccion c
            LEFT JOIN c.hechos h
            GROUP BY c.id
        """, Object[].class).getResultList();

            Map<Integer, Long> conteos = new HashMap<>();
            for (Object[] r : rows) {
                conteos.put((Integer) r[0], (Long) r[1]);
            }

            // CANTIDAD DE HECHOS VISIBLES
            List<Object[]> rowsVisibles = em.createQuery("""
                SELECT c.id, COUNT(hv)
                FROM coleccion c
                LEFT JOIN c.hechosVisibles hv
                GROUP BY c.id
            """, Object[].class).getResultList();

            Map<Integer, Long> conteosVisibles = new HashMap<>();
            for (Object[] r : rowsVisibles) {
                conteosVisibles.put((Integer) r[0], (Long) r[1]);
            }
            // FUENTES DE CADA COLECCIÓN → List<Integer> con IDs
            List<Object[]> rowsFuentes = em.createQuery("""
                SELECT c.id, f.id
                FROM coleccion c
                JOIN c.fuentes f
                ORDER BY c.id
            """, Object[].class).getResultList();

            Map<Integer, List<Integer>> fuentesPorColeccion = new HashMap<>();

            for (Object[] r : rowsFuentes) {
                Integer idColeccion = (Integer) r[0];
                Integer idFuente = (Integer) r[1];

                fuentesPorColeccion
                        .computeIfAbsent(idColeccion, k -> new ArrayList<>())
                        .add(idFuente);
            }

            // CRITERIOS DE CADA COLECCIÓN
            List<Object[]> rowsCriterios = em.createQuery("""
                SELECT c.id, crit
                FROM coleccion c
                JOIN c.criterioDePertenencia crit
                ORDER BY c.id
            """, Object[].class).getResultList();

            Map<Integer, List<core.api.DTO.criterio.CriterioDTO>> criteriosPorColeccion = new HashMap<>();

            for (Object[] r : rowsCriterios) {
                Integer idColeccion = (Integer) r[0];
                Criterio crit = (Criterio) r[1];

                criteriosPorColeccion
                        .computeIfAbsent(idColeccion, k -> new ArrayList<>())
                        .add(core.api.DTO.criterio.CriterioDTO.from(crit));
            }

            // ARMAR DTOS
            List<ColeccionDTO> dtos = new ArrayList<>(bases.size());
            for (Object[] b : bases) {
                Integer id = (Integer) b[0];
                String titulo = (String) b[1];
                String descripcion = (String) b[2];
                String handle = (String) b[3];

                // tipos reales que vienen del JPQL
                ModoDeNavegacion modo = (ModoDeNavegacion) b[4];      // puede ser null
                AlgoritmoConsenso algoritmoObj = (AlgoritmoConsenso) b[5];     // puede ser null

                // pasar a String para el DTO
                String modoStr = (modo != null) ? modo.name() : null;

                String algoritmoStr = null;
                if (algoritmoObj != null) {
                    if (algoritmoObj instanceof StrategyAbsoluta) {
                        algoritmoStr = "ABSOLUTO";
                    } else if (algoritmoObj instanceof StrategyMayoriaSimple) {
                        algoritmoStr = "MAYORIA_SIMPLE";
                    } else if (algoritmoObj instanceof StrategyMultiplesMenciones) {
                        algoritmoStr = "MULTIPLES_MENCIONES";
                    } else {
                        algoritmoStr = algoritmoObj.getClass().getSimpleName();
                    }
                }

                ColeccionDTO dto = new ColeccionDTO();
                dto.setId(id);
                dto.setTitulo(titulo);
                dto.setDescripcionColeccion(descripcion);
                dto.setIdentificadorHandle(handle);
                dto.setModoDeNavegacion(modoStr);
                dto.setAlgoritmoConsenso(algoritmoStr);

                // cantidad de hechos totales
                dto.setCantidadHechos(
                        Math.toIntExact(conteos.getOrDefault(id, 0L))
                );

                // cantidad de hechos visibles
                dto.setCantidadHechosVisibles(
                        Math.toIntExact(conteosVisibles.getOrDefault(id, 0L))
                );

                // ids de las fuentes
                dto.setFuentes(
                        fuentesPorColeccion.getOrDefault(id, new ArrayList<>())
                );

                // criterios
                dto.setCriterioDePertenencia(
                        criteriosPorColeccion.getOrDefault(id, new ArrayList<>())
                );

                dtos.add(dto);
            }
            return dtos;

        } finally {
            try {
                em.close();
            } catch (Exception ignore) {
            }
        }
    }

    public ColeccionDTO obtenerColeccionDTOConCantidadHechos(Integer idColeccion) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            // =========================
            //  BASE: datos principales
            // =========================
            Object[] base = em.createQuery("""
            SELECT c.id,
                   c.titulo,
                   c.descripcionColeccion,
                   c.identificadorHandle,
                   c.modoDeNavegacion,
                   c.algoritmoConsenso
            FROM coleccion c
            WHERE c.id = :idColeccion
        """, Object[].class)
                    .setParameter("idColeccion", idColeccion)
                    .getSingleResult();

            Integer id = (Integer) base[0];
            String titulo = (String) base[1];
            String descripcion = (String) base[2];
            String handle = (String) base[3];
            ModoDeNavegacion modo = (ModoDeNavegacion) base[4];  // puede ser null
            AlgoritmoConsenso algoritmoObj = (AlgoritmoConsenso) base[5]; // puede ser null

            // =========================
            //  CANTIDAD DE HECHOS
            // =========================
            Long cantHechos = em.createQuery("""
            SELECT COUNT(h)
            FROM coleccion c
            LEFT JOIN c.hechos h
            WHERE c.id = :idColeccion
        """, Long.class)
                    .setParameter("idColeccion", idColeccion)
                    .getSingleResult();

            // =========================
            //  CANTIDAD DE HECHOS VISIBLES
            // =========================
            Long cantHechosVisibles = em.createQuery("""
            SELECT COUNT(hv)
            FROM coleccion c
            LEFT JOIN c.hechosVisibles hv
            WHERE c.id = :idColeccion
        """, Long.class)
                    .setParameter("idColeccion", idColeccion)
                    .getSingleResult();

            // =========================
            //  FUENTES (lista de IDs)
            // =========================
            List<Integer> fuentesIds = em.createQuery("""
            SELECT f.id
            FROM coleccion c
            JOIN c.fuentes f
            WHERE c.id = :idColeccion
            ORDER BY f.id
        """, Integer.class)
                    .setParameter("idColeccion", idColeccion)
                    .getResultList();

            // =========================
            //  CRITERIOS
            // =========================
            List<Object[]> rowsCriterios = em.createQuery("""
            SELECT c.id, crit
            FROM coleccion c
            JOIN c.criterioDePertenencia crit
            WHERE c.id = :idColeccion
            ORDER BY c.id
        """, Object[].class)
                    .setParameter("idColeccion", idColeccion)
                    .getResultList();

            List<core.api.DTO.criterio.CriterioDTO> criteriosDTO = new ArrayList<>();
            for (Object[] r : rowsCriterios) {
                Criterio crit = (Criterio) r[1];
                criteriosDTO.add(core.api.DTO.criterio.CriterioDTO.from(crit));
            }

            // =========================
            //  MAPEO A DTO
            // =========================
            String modoStr = (modo != null) ? modo.name() : null;

            String algoritmoStr = null;
            if (algoritmoObj != null) {
                if (algoritmoObj instanceof StrategyAbsoluta) {
                    algoritmoStr = "ABSOLUTO";
                } else if (algoritmoObj instanceof StrategyMayoriaSimple) {
                    algoritmoStr = "MAYORIA_SIMPLE";
                } else if (algoritmoObj instanceof StrategyMultiplesMenciones) {
                    algoritmoStr = "MULTIPLES_MENCIONES";
                } else {
                    algoritmoStr = algoritmoObj.getClass().getSimpleName();
                }
            }

            ColeccionDTO dto = new ColeccionDTO();
            dto.setId(id);
            dto.setTitulo(titulo);
            dto.setDescripcionColeccion(descripcion);
            dto.setIdentificadorHandle(handle);
            dto.setModoDeNavegacion(modoStr);
            dto.setAlgoritmoConsenso(algoritmoStr);

            dto.setCantidadHechos(Math.toIntExact(cantHechos != null ? cantHechos : 0L));
            dto.setCantidadHechosVisibles(Math.toIntExact(cantHechosVisibles != null ? cantHechosVisibles : 0L));

            dto.setFuentes(fuentesIds != null ? fuentesIds : new ArrayList<>());
            dto.setCriterioDePertenencia(criteriosDTO);

            return dto;

        } catch (NoResultException e) {
            // si querés, podés devolver null o tirar una excepción custom
            return null;
        } finally {
            try {
                em.close();
            } catch (Exception ignore) {
            }
        }
    }


    public List<Hecho> getHechosConUbicacion(Integer idColeccion) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            return em.createQuery(
                            "select h " +
                                    "from coleccion c " +
                                    " join c.hechos h " +
                                    " left join fetch h.ubicacion " +
                                    "where c.id = :id", Hecho.class)
                    .setParameter("id", idColeccion)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Reemplaza completamente las fuentes de una colección por las seleccionadas (solo las tildadas quedan asociadas).
     */
    public void actualizarFuentesDeColeccion(int idColeccion, List<Integer> idsFuentes) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            DBUtils.comenzarTransaccion(em);
            Coleccion coleccion = em.find(Coleccion.class, idColeccion, LockModeType.PESSIMISTIC_WRITE);
            if (coleccion == null) {
                throw new IllegalArgumentException("No existe la Coleccion con id=" + idColeccion);
            }

            // Inicializar la lista si hace falta
            if (coleccion.getFuentes() == null) {
                coleccion.setFuentes(new ArrayList<>());
            }

            // Limpiar fuentes actuales
            coleccion.getFuentes().clear();

            // Agregar nuevas fuentes (solo las seleccionadas)
            if (idsFuentes != null) {
                for (Integer idFuente : idsFuentes) {
                    if (idFuente == null) continue;
                    Fuente ref = em.getReference(Fuente.class, idFuente);
                    coleccion.getFuentes().add(ref);
                }
            }

            DBUtils.commit(em);
        } catch (RuntimeException ex) {
            DBUtils.rollback(em);
            throw ex;
        } finally {
            try {
                em.close();
            } catch (Exception ignore) {
            }
        }
    }

    public List<Hecho> obtenerHechosVisiblesDeColeccion(Integer idColeccion) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            Coleccion coleccion = em.find(Coleccion.class, idColeccion);
            if (coleccion == null) {
                return List.of();
            }

            // Fuerzo la inicialización dentro de la sesión
            List<Hecho> visibles = coleccion.getHechosVisibles();
            visibles.size(); // toca la colección para inicializarla

            // Devuelvo una lista "normal", desconectada de Hibernate
            return new ArrayList<>(visibles);
        } finally {
            try {
                em.close();
            } catch (Exception ignore) {
            }
        }
    }

    @Override
    public void delete(Coleccion entity) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            DBUtils.comenzarTransaccion(em);

            // 1. Re-attach la entidad si es necesario para poder acceder a sus colecciones
            Coleccion managed = em.contains(entity) ? entity : em.merge(entity);

            // 2. Guardamos los criterios asociados ANTES de borrar la colección
            // (Hacemos una copia de la lista para no tener problemas de concurrencia)
            List<Criterio> criteriosAsociados = new ArrayList<>(managed.getCriterioDePertenencia());

            // 3. Borramos la colección
            // Esto eliminará la colección y las filas de unión en 'coleccion_criterio'
            em.remove(managed);

            // Hacemos flush para que la BD actualice la tabla intermedia inmediatamente
            em.flush();

            // 4. Verificamos "huérfanos": ¿Quedó algún criterio suelto?
            for (Criterio c : criteriosAsociados) {
                // Contamos cuántas colecciones siguen usando este criterio específico
                Long count = em.createQuery(
                                "SELECT COUNT(c) FROM coleccion c JOIN c.criterioDePertenencia cr WHERE cr.id = :id",
                                Long.class)
                        .setParameter("id", c.getId())
                        .getSingleResult();

                // Si nadie más lo usa (count == 0), lo borramos
                if (count == 0) {
                    Criterio criterioABorrar = em.find(Criterio.class, c.getId());
                    if (criterioABorrar != null) {
                        em.remove(criterioABorrar);
                    }
                }
            }

            DBUtils.commit(em);
        } catch (RuntimeException ex) {
            DBUtils.rollback(em);
            throw ex;
        } finally {
            try {
                em.close();
            } catch (Exception ignore) {
            }
        }
    }

    public void eliminarFuenteDeTodasLasColecciones(Integer idFuente) {
        if (idFuente == null) return;
        EntityManager em = DBUtils.getEntityManager();
        try {
            DBUtils.comenzarTransaccion(em);
            int filas = em.createNativeQuery(
                            "DELETE FROM coleccion_fuente WHERE id_fuente = ?1"
                    )
                    .setParameter(1, idFuente)
                    .executeUpdate();
            DBUtils.commit(em);
        } catch (RuntimeException ex) {
            DBUtils.rollback(em);
            throw ex;
        } finally {
            try { em.close(); } catch (Exception ignore) {}
        }
    }

    public Coleccion findByIdConCriteriosYFuentes(Integer id) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            // 1) Traigo la coleccion + fuentes
            Coleccion c = em.createQuery("""
            select distinct c from coleccion c
            left join fetch c.fuentes
            where c.id = :id
        """, Coleccion.class)
                    .setParameter("id", id)
                    .getSingleResult();

            // 2) Inicializo criterios en una segunda query (mismo EM)
            em.createQuery("""
            select c from coleccion c
            left join fetch c.criterioDePertenencia
            where c.id = :id
        """, Coleccion.class)
                    .setParameter("id", id)
                    .getSingleResult();

            // ahora c ya tiene ambas cargadas (porque sigue siendo managed en este EM)
            c.getCriterioDePertenencia().size();
            c.getFuentes().size();

            return c;
        } finally {
            em.close();
        }
    }


    // Solo criterios, sin fuentes
    public Coleccion findByIdConCriterios(Integer id) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            return em.createQuery("""
            select distinct c from coleccion c
            left join fetch c.criterioDePertenencia
            where c.id = :id
        """, Coleccion.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public List<Integer> obtenerIdsFuentesDeColeccion(Integer idColeccion) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            return em.createQuery("""
            select f.id from coleccion c
            join c.fuentes f
            where c.id = :id
        """, Integer.class)
                    .setParameter("id", idColeccion)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Coleccion> listarColeccionesParaGraphQL() {
        EntityManager em = DBUtils.getEntityManager();
        try {
            List<Coleccion> colecciones = em
                    .createQuery("select c from coleccion c", Coleccion.class)
                    .getResultList();

            colecciones.forEach(c -> {
                // Forzamos inicialización de fuentes
                if (c.getFuentes() != null) {
                    c.getFuentes().forEach(f -> {
                        f.getNombre();          // toca el proxy
                        f.getId();  // por las dudas
                        f.getTipoFuente();      // si lo usás en el schema
                        f.getLink();
                    });
                }

                // Inicializar criterioDePertenencia (por ahora solo size())
                if (c.getCriterioDePertenencia() != null) {
                    c.getCriterioDePertenencia().size();
                }

                // Inicializar hechos (lista completa)
                if (c.getHechos() != null) {
                    c.getHechos().forEach(this::inicializarHechoParaGraphQL);
                }

                // Inicializar hechosVisibles (la que te está explotando ahora)
                if (c.getHechosVisibles() != null) {
                    c.getHechosVisibles().forEach(this::inicializarHechoParaGraphQL);
                }

                // Campo simple, por si acaso
                c.getModoDeNavegacion();
            });

            return colecciones;
        } finally {
            em.close();
        }
    }

    private void inicializarHechoParaGraphQL(Hecho h) {
        if (h == null) return;

        h.getTitulo();
        h.getDescripcion();
        h.getEstado();

        if (h.getCategoria() != null) {
            h.getCategoria().getNombre();
        }
        if (h.getUbicacion() != null) {
            h.getUbicacion().getLatitud();
            h.getUbicacion().getLongitud();
        }
        if (h.getContribuyente() != null) {
            h.getContribuyente().getNombre();
            h.getContribuyente().getMail();
        }

        h.getEtiquetas().size();    // inicializa colección
        h.getMultimedia().size();   // idem

        h.getFechaSuceso();
        h.getFechaCarga();
        h.getHoraSuceso();
        h.getUltimaFechaModificacion();
        h.getCodigoDeFuente();
        h.getHash();
        h.getIdFuente();
        h.getLinkFuente();
    }

    public Coleccion buscarColeccionParaGraphQL(Integer id) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            Coleccion c = em.find(Coleccion.class, id);
            if (c == null) return null;

            // Mismo patrón de inicialización:
            if (c.getFuentes() != null) {
                c.getFuentes().forEach(f -> {
                    f.getNombre();
                    f.getId();
                    f.getTipoFuente();
                    f.getLink();
                });
            }

            if (c.getCriterioDePertenencia() != null) {
                c.getCriterioDePertenencia().size();
            }

            if (c.getHechos() != null) {
                c.getHechos().forEach(this::inicializarHechoParaGraphQL);
            }

            if (c.getHechosVisibles() != null) {
                c.getHechosVisibles().forEach(this::inicializarHechoParaGraphQL);
            }

            c.getModoDeNavegacion();
            c.getDescripcionColeccion();
            c.getTitulo();

            return c;
        } finally {
            em.close();
        }
    }

    public int desvincularHechosDeColeccionPorIdsFuente(int idColeccion, List<Integer> idsFuentesRemovidas) {
        if (idsFuentesRemovidas == null || idsFuentesRemovidas.isEmpty()) return 0;

        EntityManager em = DBUtils.getEntityManager();
        try {
            DBUtils.comenzarTransaccion(em);

            // desvincula coleccion_hecho
            int filas = em.createNativeQuery("""
            DELETE ch
            FROM coleccion_hecho ch
            JOIN hecho h ON h.id_hecho = ch.id_hecho
            WHERE ch.id_coleccion = :idColeccion
              AND h.id_fuente IN (:idsFuentes)
        """)
                    .setParameter("idColeccion", idColeccion)
                    .setParameter("idsFuentes", idsFuentesRemovidas)
                    .executeUpdate();

            // hechos_visibles
            em.createNativeQuery("""
            DELETE hv
            FROM hechos_visibles hv
            JOIN hecho h ON h.id_hecho = hv.id_hecho
            WHERE hv.id_coleccion = :idColeccion
              AND h.id_fuente IN (:idsFuentes)
        """)
                    .setParameter("idColeccion", idColeccion)
                    .setParameter("idsFuentes", idsFuentesRemovidas)
                    .executeUpdate();

            DBUtils.commit(em);
            return filas;

        } catch (RuntimeException ex) {
            DBUtils.rollback(em);
            throw ex;
        } finally {
            try { em.close(); } catch (Exception ignore) {}
        }
    }

    public int desvincularHechosDeColeccion(int idColeccion, List<Integer> idsHechos) {
        if (idsHechos == null || idsHechos.isEmpty()) return 0;

        EntityManager em = DBUtils.getEntityManager();
        try {
            DBUtils.comenzarTransaccion(em);

            int filas = em.createNativeQuery("""
            DELETE FROM coleccion_hecho
            WHERE id_coleccion = ?1
              AND id_hecho IN (?2)
        """)
                    .setParameter(1, idColeccion)
                    .setParameter(2, idsHechos)
                    .executeUpdate();

            em.createNativeQuery("""
            DELETE FROM hechos_visibles
            WHERE id_coleccion = ?1
              AND id_hecho IN (?2)
        """)
                    .setParameter(1, idColeccion)
                    .setParameter(2, idsHechos)
                    .executeUpdate();

            DBUtils.commit(em);
            return filas;

        } catch (RuntimeException ex) {
            DBUtils.rollback(em);
            throw ex;
        } finally {
            try { em.close(); } catch (Exception ignore) {}
        }
    }

    public List<Integer> obtenerTodosLosIds() {
        EntityManager em = DBUtils.getEntityManager();
        try {
            return em.createQuery("SELECT c.id FROM coleccion c", Integer.class).getResultList();
        } finally {
            em.close();
        }
    }

    // 2. Método transaccional que encapsula toda la lógica de base de datos
    public void recalcularHechosVisibles(Integer idColeccion) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            DBUtils.comenzarTransaccion(em);

            // A. Query optimizada con JOIN FETCH para traer todo en un viaje y evitar LazyInitException
            Coleccion coleccion = em.createQuery(
                            "SELECT DISTINCT c FROM coleccion c LEFT JOIN FETCH c.hechos WHERE c.id = :id",
                            Coleccion.class)
                    .setParameter("id", idColeccion)
                    .getSingleResult();

            // B. Ejecutar lógica de dominio (El algoritmo de consenso)
            coleccion.actualizarColeccionVisible();

            // C. Guardar cambios (Hibernate detectará los cambios en 'hechosVisibles')
            em.merge(coleccion);

            DBUtils.commit(em);
        } catch (Exception e) {
            DBUtils.rollback(em);
            logger.error("Error recalculando colección ID {}", idColeccion, e);
        } finally {
            try { em.close(); } catch (Exception ignore) {}
        }
    }
}

