package core.models.agregador;

import core.models.agregador.normalizador.*;
import core.models.entities.colecciones.Coleccion;
import core.models.entities.colecciones.criterios.Criterio;
import core.models.entities.colecciones.criterios.FiltradorCriterios;
import core.models.entities.hecho.*;
import core.models.repository.ColeccionesRepository;
import core.models.repository.HechosRepository;
import core.observabilidad.RegistroMetricas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.time.LocalDate;
import java.util.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ServicioDeAgregacion {
    private List<HechoAIntegrarDTO> hechosAIntegrar = new ArrayList<>();
    private List<Hecho> hechosLimpios = new ArrayList<>();

    private ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();
    private HechosRepository hechosRepository = HechosRepository.getInstance();

    private FiltradorCriterios filtradorCriterios = FiltradorCriterios.getInstance();

    private DetectorDeSpam detectorDeSpam = DetectorDeSpam.getInstance();
    private ComparadorHechos comparadorHechos = ComparadorHechos.getInstance();
    private NormalizadorFecha normalizadorFecha = NormalizadorFecha.getInstance();
    private NormalizadorHora normalizadorHora = NormalizadorHora.getInstance();
    private NormalizadorCategoria normalizadorCategoria = NormalizadorCategoria.getInstance();
    private NormalizadorCoordenada normalizadorCoordenada = NormalizadorCoordenada.getInstance();
    private NormalizadorEtiqueta normalizadorEtiqueta = NormalizadorEtiqueta.getInstance();
    private NormalizadorContribuyente normalizadorContribuyente = NormalizadorContribuyente.getInstance();
    private FactoryHecho factoryHecho = FactoryHecho.getInstance();

    private static final Logger log = LoggerFactory.getLogger(ServicioDeAgregacion.class);

    private static volatile ServicioDeAgregacion instance;

    private ServicioDeAgregacion() {
        if (instance != null) {
            throw new RuntimeException("Usa getInstance() para obtener el Singleton");
        }
    }

    public static ServicioDeAgregacion getInstance() {
        if (instance == null) {
            synchronized (ServicioDeAgregacion.class) {
                if (instance == null) {
                    instance = new ServicioDeAgregacion();
                }
            }
        }
        return instance;
    }

    //FLUJO:
    //1.  Obtenemos todos los HechosDTO a integrar de las fuentes, eliminando duplicados fuente a fuente. Pensar un algoritmo.
    //2.  Eliminamos los spam
    //3.  Por cada hecho a integrar verifique los duplicados contra la lista de hechosAIntegrar.
    //    - En caso de haber una coincidencia...elegimos una categoria para ponerle!
    //    - Cranear un poco mas lo de la categoria, onda cual tomamos. -> NormalizadorCategoria
    // 4. Normalizar la fecha
    //  - 4.1 si no se puede normalizar se manda a revisión manual
    // 5. Enviar al Factory para crear el hecho
    // 6. Agregar a las colecciones correspondientes (ver lo de los criterios de pertenencia)

    private int  eliminarSpam(List <HechoAIntegrarDTO> lista) {
        if (lista == null || lista.isEmpty()) return 0;

        int eliminados = 0;
        Iterator<HechoAIntegrarDTO> it = lista.iterator();

        while (it.hasNext()) {
            HechoAIntegrarDTO h = it.next();

            boolean tituloSpam = detectorDeSpam.esSpam(h.getTitulo());
            boolean descripcionSpam = detectorDeSpam.esSpam(h.getDescripcion());

            if (tituloSpam || descripcionSpam) {
                log.warn("SPAM eliminado. hash={} tituloSpam={} descripcionSpam={} titulo=\"{}\"",
                        h.getHash(), tituloSpam, descripcionSpam, safe(h.getTitulo()));
                it.remove();
            }
        }
        return eliminados;
    }

    private String safe(String s) {
        if (s == null) return "null";
        s = s.replaceAll("\\s+", " ").trim();
        return s.length() > 80 ? s.substring(0, 80) + "..." : s;
    }

    public int eliminarDuplicados(List<HechoAIntegrarDTO> hechos) {
        Objects.requireNonNull(hechos, "lista nula");
        if (hechos.isEmpty() || hechos.size() == 1) return 0;
       int before = hechos.size();
        for (int i = 0; i < hechos.size(); i++) {
            HechoAIntegrarDTO hi = hechos.get(i);
            for (int j = i + 1; j < hechos.size(); ) {
                if (comparadorHechos.hechoDuplicado(hi, hechos.get(j))) {
                    hechos.remove(j);
                } else {
                    j++; // solo avanzá si no eliminaste
                }
            }
        }
        int eliminados = before - hechos.size();
        if (eliminados > 0) {
            log.info("Duplicados eliminados. eliminados={} before={} after={}", eliminados, before, hechos.size());
        }
        return eliminados;
    }

    //1. Buscar los hechos parecidos, varios grupos de hechos parecidos
    //2. Dejamos una lista para los no parecidos
    //3. NormalizadorCategoria: 1 que normaliza normal, la busca en el repo
    //     NormalizadorCategroria que reciba una lista y haga la logica
    //     Si crea una categoria nueva y es solo, se deja o se manda a revisión??

    public void normalizarYCrearHechos() {
        int ok = 0;
        int fallidos = 0;
        for (HechoAIntegrarDTO dto : hechosAIntegrar) {
            try{
                if (dto.getCategoria() == null || dto.getCategoria().isBlank()) {
                    log.warn("Hecho sin categoría. hash={}", dto.getHash());}
                Categoria categoria = normalizadorCategoria.obtenerCategoria(dto.getCategoria()); //Solo la crea
                LocalDate fecha = normalizadorFecha.normalizarFecha(dto.getFechaSuceso()); // hace el quilombo de fecha
                LocalTime horaSuceso = normalizadorHora.normalizarHora(dto.getHoraSuceso());
                Coordenadas ubicacion = normalizadorCoordenada.obtenerCoordenadas(dto.getLatitud(), dto.getLongitud()); // solo la crea
                List<Etiqueta> etiquetas = normalizadorEtiqueta.obtenerEtiquetas(dto.getEtiquetas());
                Contribuyente contribuyente = normalizadorContribuyente.obtenerContribuyente(dto.getContribuyente());
                Hecho hecho = factoryHecho.convertirHecho(dto, fecha, categoria, ubicacion, etiquetas, contribuyente, horaSuceso); // factory que funciona
                hechosLimpios.add(hecho);
                ok++;
            } catch (NormalizadorFecha.ExcepcionRevisionManualFecha e) {
                log.warn("Fecha no normalizable → revisión manual. hash={} fechaRaw={}", dto.getHash(), dto.getFechaSuceso());
            } catch (Exception e) {
                fallidos++;
                log.error("Error creando hecho desde DTO. hash={}", dto.getHash(), e);
            }
        }

        log.info("Normalización: fin. fallidos={} output={}",
               fallidos, hechosLimpios.size());
    }

    private void agregarHechosAColecciones(Integer idColeccion)
    {
        long t0 = System.currentTimeMillis();
        Coleccion coleccion = coleccionesRepository.findByIdConCriterios(idColeccion);
        if (coleccion == null) {
            log.error("Colección inexistente. id={}", idColeccion);
            return;
        }

        List<Criterio> criterios = coleccion.getCriterioDePertenencia();

        // 2) Traigo solo los IDs de las fuentes de esa colección
        List<Integer> idsFuentesDeColeccion = coleccionesRepository.obtenerIdsFuentesDeColeccion(idColeccion);

        List<Hecho> hechosFiltradosFuentes = hechosRepository.obtenerHechosPorIdsFuente(idsFuentesDeColeccion);
        if (hechosFiltradosFuentes.isEmpty()) {
            log.warn("Colección con fuentes pero sin hechos asociados. id={} fuentes={}", idColeccion, idsFuentesDeColeccion.size());
            return;
        }

        List<Hecho> hechosFiltradosCriterio = filtradorCriterios.filtrarHechos(hechosFiltradosFuentes, criterios);
        List<Integer> idHechos = hechosFiltradosCriterio.stream().map(Hecho::getId).toList();
        coleccionesRepository.agregarHechosAColeccion(idColeccion, idHechos);

        long dt = System.currentTimeMillis() - t0;
        log.info("Colección actualizada. id={} hechosAgregados={} durationMs={}", idColeccion, idHechos.size(), dt);
    }


    public void limpiarHechos() {

         int spamEliminados = eliminarSpam(hechosAIntegrar);

         //int duplicadosEliminados = eliminarDuplicados(hechosAIntegrar);

         normalizadorCategoria.estandarizarCategoriasDuplicadas(hechosAIntegrar);

         //log.info("Limpieza: spamEliminados={} duplicadosEliminados={}", spamEliminados, duplicadosEliminados);
         log.info("Limpieza: spamEliminados={}", spamEliminados);
    }

    //EL QUE SE USA!!
    public void actualizarColecciones(List<HechoAIntegrarDTO> lista) {
        if (MDC.get("traceId") == null) {
            MDC.put("traceId", UUID.randomUUID().toString().substring(0, 8));
        }

        long t0 = System.currentTimeMillis();

            if (lista == null) {
                log.error("actualizarColecciones: lista=null");
                return;
            }

            log.info("Inicio actualizarColecciones. inputSize={}", lista.size());

            hechosAIntegrar.clear();
            hechosLimpios.clear();

            hechosAIntegrar.addAll(lista);
            log.info("Hechos cargados a integrar. size={}", hechosAIntegrar.size());

            limpiarHechos();
            log.info("Post-limpieza. hechosAIntegrar={}", hechosAIntegrar.size());

            RegistroMetricas.addHechosCreados(hechosAIntegrar.size());
            normalizarYCrearHechos();
            log.info("Post-normalización. hechosLimpios={}", hechosLimpios.size());

            try {
                hechosRepository.addAllEnUnaTransaccion(hechosLimpios);
                log.info("Persistencia OK. insertCount={}", hechosLimpios.size());
            } catch (Exception e) {
                log.error("Falló persistencia en una transacción. insertCount={}", hechosLimpios.size(), e);
                return;
            }

            List<Coleccion> colecciones = coleccionesRepository.obtenerTodas();
            log.info("Colecciones obtenidas. count={}", colecciones.size());


            int ok = 0;
            for (Coleccion coleccion : colecciones) {
                try {
                    agregarHechosAColecciones(coleccion.getId()); // adentro le metemos logs
                    ok++;
                } catch (Exception e) {
                    log.error("Error actualizando colección id={}", coleccion.getId(), e);
                }
            }

            long dt = System.currentTimeMillis() - t0;
            log.info("Fin actualizarColecciones. coleccionesOK={}/{} durationMs={}", ok, colecciones.size(), dt);

            hechosAIntegrar.clear();
            hechosLimpios.clear();
            colecciones.clear();

    }

    public void hechoUnicoUrgente(HechoAIntegrarDTO hechoUnico) {
        if (hechoUnico == null) {
            log.warn("hechoUnicoUrgente llamado con null");
            return;
        }
        log.info("hechoUnicoUrgente: hash={}", hechoUnico.getHash());
        actualizarColecciones(List.of(hechoUnico));
    }

}



