package core.api.handlers.hechos;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.models.entities.hecho.Etiqueta;
import core.models.entities.hecho.Hecho;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.DBUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.*;
import java.util.stream.Collectors;
import core.models.entities.hecho.Coordenadas;
import core.models.entities.hecho.Categoria;
import core.models.repository.CategoriaRepository;

public class PatchHechoHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(PatchHechoHandler.class);
    private static final ObjectMapper RELAXED_JSON = new ObjectMapper()
            .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
            .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

    @Override
    public void handle(Context ctx) {
        String hash = ctx.pathParam("hash"); // Ruta: /core/api/hechos/{hash}
        log.info("Patch hecho hash={}", hash);

        if (hash == null || hash.isBlank()) {
            log.warn("Hash requerido faltante");
            ctx.status(400).result("Hash requerido");
            return;
        }

        String raw = ctx.body();
        Map<String, Object> body;
        try {
            body = (raw == null || raw.isBlank())
                    ? Collections.emptyMap()
                    : RELAXED_JSON.readValue(raw, new TypeReference<Map<String, Object>>() {});
        } catch (Exception ex) {
            log.warn("Body inválido en patch hecho hash={}", hash);
            ctx.status(400).result("Body inválido. Enviar JSON con nombre/descripcion/etiquetas.");
            return;
        }

        String nombre = body.get("nombre") != null ? String.valueOf(body.get("nombre")).trim() : null;
        String descripcion = body.get("descripcion") != null ? String.valueOf(body.get("descripcion")).trim() : null;
        String fechaSucesoStr = body.get("fecha_suceso") != null ? String.valueOf(body.get("fecha_suceso")).trim() : null;
        java.time.LocalDate fechaSuceso = null;
        if (fechaSucesoStr != null && !fechaSucesoStr.isBlank()) {
            try {
                fechaSuceso = java.time.LocalDate.parse(fechaSucesoStr);
            } catch (Exception e) {
                ctx.status(400).result("Fecha del suceso inválida (formato esperado: yyyy-MM-dd)");
                return;
            }
        }

        // Leer latitud y longitud si vinieron
        Double latitud = null;
        Double longitud = null;
        try {
            if (body.get("latitud") != null && !body.get("latitud").toString().isBlank()) {
                latitud = Double.valueOf(body.get("latitud").toString());
            }
            if (body.get("longitud") != null && !body.get("longitud").toString().isBlank()) {
                longitud = Double.valueOf(body.get("longitud").toString());
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("Latitud o longitud inválida");
            return;
        }

        // Leer etiquetas si vinieron
        List<String> etiquetasReq = null;
        Object et = body.get("etiquetas");
        if (et instanceof List<?> lista) {
            // Normalizamos: trim, sin vacíos, sin duplicados (case-insensitive), preservando orden
            LinkedHashMap<String, String> dedup = new LinkedHashMap<>();
            for (Object o : lista) {
                String s = String.valueOf(o);
                if (s == null) continue;
                String t = s.trim();
                if (t.isEmpty()) continue;
                String key = t.toLowerCase(Locale.ROOT);
                dedup.putIfAbsent(key, t); // guarda formato original pero deduplica por lower
            }
            etiquetasReq = new ArrayList<>(dedup.values());
        }

        // Leer categoría si vino
        String categoriaStr = body.get("categoria") != null ? String.valueOf(body.get("categoria")).trim() : null;

        // Si no hay cambios y no vienen etiquetas, coordenadas ni categoría, OK
        if ((nombre == null || nombre.isBlank())
                && (descripcion == null || descripcion.isBlank())
                && etiquetasReq == null
                && latitud == null && longitud == null
                && fechaSuceso == null
                && (categoriaStr == null || categoriaStr.isBlank())) {
            log.info("Patch hecho sin cambios hash={}", hash);
            ctx.status(204);
            return;
        }

        EntityManager em = DBUtils.getEntityManager();
        try {
            DBUtils.comenzarTransaccion(em);

            Hecho hecho = buscarPorHash(em, hash);
            if (hecho == null) {
                DBUtils.rollback(em);
                log.warn("Hecho no encontrado para patch hash={}", hash);
                ctx.status(404).result("Hecho no encontrado");
                return;
            }


            boolean huboCambio = false;
            if (nombre != null && !nombre.isBlank()) { hecho.setTitulo(nombre); huboCambio = true; }
            if (descripcion != null && !descripcion.isBlank()) { hecho.setDescripcion(descripcion); huboCambio = true; }
            if (fechaSuceso != null) { hecho.setFechaSuceso(fechaSuceso); huboCambio = true; }

            // Actualizar categoría si corresponde
            if (categoriaStr != null && !categoriaStr.isBlank()) {
                CategoriaRepository repoCat = CategoriaRepository.getInstance();
                Categoria categoria = repoCat.buscarPorNombre(categoriaStr);
                if (categoria == null) {
                    categoria = new Categoria(categoriaStr);
                    em.persist(categoria);
                    em.flush();
                }
                hecho.setCategoria(categoria);
                huboCambio = true;
            }

            // Actualizar coordenadas si corresponde
            if (latitud != null || longitud != null) {
                Coordenadas coords = hecho.getUbicacion();
                if (coords == null) {
                    coords = new Coordenadas();
                    hecho.setUbicacion(coords);
                    em.persist(coords);
                }
                if (latitud != null) { coords.setLatitud(latitud); huboCambio = true; }
                if (longitud != null) { coords.setLongitud(longitud); huboCambio = true; }
            }

            if (etiquetasReq != null) {
                List<Etiqueta> gestionadas = new ArrayList<>(etiquetasReq.size());
                for (String t : etiquetasReq) {
                    Etiqueta e = buscarEtiquetaPorNombre(em, t);
                    if (e == null) {
                        e = new Etiqueta(t); // Assumiendo constructor Etiqueta(String nombre)
                        em.persist(e);
                        em.flush();
                    }
                    gestionadas.add(e);
                }
                // Reemplaza el set de etiquetas completamente por lo enviado
                hecho.setEtiquetas(gestionadas);
                huboCambio = true;
            }

            // Si hubo algún cambio, actualiza la fecha de última modificación
            if (huboCambio) {
                hecho.setUltimaFechaModificacion(java.time.LocalDate.now());
            }

            em.merge(hecho);
            DBUtils.commit(em);
            log.info("Patch hecho aplicado ok hash={}", hash);
            ctx.status(204);
        } catch (Exception ex) {
            DBUtils.rollback(em);
            log.error("Error actualizando hecho hash={}", hash, ex);
            ctx.status(500).result("Error actualizando hecho: " + ex.getMessage());
        } finally {
            try { em.close(); } catch (Exception ignore) {}
        }
    }

    private Hecho buscarPorHash(EntityManager em, String hash) {
        try {
            return em.createQuery("from hecho h where lower(h.hash)=:hs", Hecho.class)
                    .setParameter("hs", hash.trim().toLowerCase(Locale.ROOT))
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    private Etiqueta buscarEtiquetaPorNombre(EntityManager em, String nombre) {
        try {
            return em.createQuery("from etiqueta e where lower(e.nombre)=:n", Etiqueta.class)
                    .setParameter("n", nombre.trim().toLowerCase(Locale.ROOT))
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}