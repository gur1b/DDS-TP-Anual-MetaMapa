package servicioEstadisticas.model.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servicioEstadisticas.model.entities.Categoria;
import servicioEstadisticas.model.entities.Hecho;
import servicioEstadisticas.model.entities.SolicitudSpam;
import utils.DBUtils;
import utils.GeocodingUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RepositoryServicioEstadisticas {

    private static final Logger log = LoggerFactory.getLogger(RepositoryServicioEstadisticas.class);

    private static volatile RepositoryServicioEstadisticas instance;
    public static RepositoryServicioEstadisticas getInstance() {
        if (instance == null) {
            synchronized (RepositoryServicioEstadisticas.class) {
                if (instance == null) {
                    instance = new RepositoryServicioEstadisticas();
                }
            }
        }
        return instance;
    }

    public static List<Map<String, Object>> provinciaConMasHechos() {
        EntityManager em = DBUtils.getEntityManager();
        try {
            String jpql = "SELECT c.latitud, c.longitud " +
                    "FROM Hecho h " +
                    "INNER JOIN h.coordenadas c";

            List<Object[]> coordenadas = em.createQuery(jpql, Object[].class)
                    .getResultList();

            Map<String, Long> provinciaCount = new HashMap<>();

            for (Object[] coord : coordenadas) {
                double lat = (Double) coord[0];
                double lon = (Double) coord[1];

                String provincia = utils.GeocodingUtils.obtenerProvincia(lat, lon);

                if (provincia == null) {
                    log.warn("[DEBUG] Hecho con coordenadas ({},{}) NO mapeado a ninguna provincia", lat, lon);
                }
                if (provincia != null) {
                    provinciaCount.merge(provincia, 1L, Long::sum);
                }
            }

            // Acá, después del for:

            return provinciaCount.entrySet().stream()
                    .map(entry -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("provincia", entry.getKey());
                        map.put("cantidad", entry.getValue());
                        return map;
                    })
                    .sorted((m1, m2) -> Long.compare(
                            (Long) m2.get("cantidad"),
                            (Long) m1.get("cantidad")
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }

    public static List<Map<String, Object>> categoriaMasReportada() {
        EntityManager em = DBUtils.getEntityManager();
        try {
            String jpql = "SELECT c.nombre, COUNT(h) as cantidad " +
                    "FROM Hecho h " +
                    "JOIN h.categoria c "+
                    "GROUP BY c.nombre " +
                    "ORDER BY COUNT(h) DESC";

            List<Object[]> results = em.createQuery(jpql, Object[].class)
                    .getResultList();

            return results.stream()
                    .map(result -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("categoria", result[0]);
                        map.put("cantidad", result[1]);
                        return map;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }

    public static Map<String, Object> cantidadSolicitudesEliminacion() {
        EntityManager em = DBUtils.getEntityManager();
        try {
            // Consulta para obtener la cantidad de solicitudes aceptadas
            String jpqlAceptadas = "SELECT COUNT(s) FROM SolicitudSpam s WHERE s.aceptada = true";
            Long cantidadAceptadas = em.createQuery(jpqlAceptadas, Long.class)
                    .getSingleResult();

            // Consulta para obtener el total de solicitudes
            String jpqlTotal = "SELECT COUNT(s) FROM SolicitudSpam s";
            Long cantidadTotal = em.createQuery(jpqlTotal, Long.class)
                    .getSingleResult();

            Map<String, Object> resultado = new HashMap<>();
            resultado.put("solicitudes spam", cantidadAceptadas);
            resultado.put("total de solicitudes", cantidadTotal);

            return resultado;
        } catch (Exception e) {
            Map<String, Object> errorResultado = new HashMap<>();
            errorResultado.put("solicitudes spam", 0);
            errorResultado.put("total de solicitudes", 0);
            return errorResultado;
        } finally {
            em.close();
        }
    }

    public static List<Map<String, Object>> horarioxCategoria(String categoriaNombre) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            String jpqlCategoria = "SELECT c FROM Categoria c WHERE c.nombre = :nombreCategoria";
            List<Categoria> categorias = em.createQuery(jpqlCategoria, Categoria.class)
                    .setParameter("nombreCategoria", categoriaNombre)
                    .getResultList();
            if (categorias.isEmpty()) return Collections.emptyList();
            Categoria categoria = categorias.get(0); // usa la primera

            String jpql = "SELECT HOUR(h.hora_suceso) as hora, COUNT(h) as cantidad " +
                    "FROM Hecho h " +
                    "WHERE h.categoria = :categoria " +
                    "GROUP BY HOUR(h.hora_suceso) " +
                    "ORDER BY COUNT(h) DESC";

            List<Object[]> resultados = em.createQuery(jpql, Object[].class)
                    .setParameter("categoria", categoria)
                    .getResultList();

            return resultados.stream()
                    .map(resultado -> {
                        Map<String, Object> map = new HashMap<>();
                        Integer hora = (Integer) resultado[0];
                        String horaFormateada = String.format("%02d:00", hora);
                        map.put("hora", horaFormateada);
                        map.put("cantidad", resultado[1]);
                        return map;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }

    public static List<Map<String, Object>> provinciaConMasHechosEnCategoria(String nombreCategoria) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            String jpqlCategoria = "SELECT c FROM Categoria c WHERE c.nombre = :nombreCategoria";
            List<Categoria> categorias = em.createQuery(jpqlCategoria, Categoria.class)
                    .setParameter("nombreCategoria", nombreCategoria)
                    .getResultList();
            if (categorias.isEmpty()) return Collections.emptyList();
            Categoria categoria = categorias.get(0); // usa la primera

            String jpql = "SELECT c.latitud, c.longitud " +
                    "FROM Hecho h " +
                    "JOIN h.coordenadas c " +
                    "WHERE h.categoria = :categoria ";

            List<Object[]> coordenadas = em.createQuery(jpql, Object[].class)
                    .setParameter("categoria", categoria)
                    .getResultList();

            Map<String, Long> provinciaCount = new HashMap<>();
            for (Object[] coord : coordenadas) {
                double lat = (Double) coord[0];
                double lon = (Double) coord[1];
                String provincia = GeocodingUtils.obtenerProvincia(lat, lon);

                if (provincia != null) {
                    provinciaCount.merge(provincia, 1L, Long::sum);
                }
            }

            return provinciaCount.entrySet().stream()
                    .map(entry -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("provincia", entry.getKey());
                        map.put("cantidad", entry.getValue());
                        return map;
                    })
                    .sorted((m1, m2) -> Long.compare((Long) m2.get("cantidad"), (Long) m1.get("cantidad")))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }

}