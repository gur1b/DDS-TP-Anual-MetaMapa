package core.models.agregador;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import core.observabilidad.RegistroMetricas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class HandlerCargadores {

    private static final Logger log = LoggerFactory.getLogger(HandlerCargadores.class);

    private static final String TRACE_KEY = "traceId";          // mismo que en logback.xml (%X{traceId})
    private static final String TRACE_HEADER = "X-Trace-Id";    // header que mandás a cargadores


    private static String newSpanId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private static volatile HandlerCargadores instance;
    List<HechoAIntegrarDTO> resultado = new ArrayList<>();
    List<HechoAIntegrarDTO> hechosExtraidos = new ArrayList<>();

    private HandlerCargadores() {
        if (instance != null) {
            throw new RuntimeException("Usa getInstance() para obtener el Singleton");
        }
    }

    public static HandlerCargadores getInstance() {
        if (instance == null) {
            synchronized (HandlerCargadores.class) {
                if (instance == null) {
                    instance = new HandlerCargadores();
                }
            }
        }
        return instance;
    }
    String path = "/obtenerHechos";
    String dinamico = ConfigLoader.getProperty("CargadorDinamico").concat(path);
    String proxy = ConfigLoader.getProperty("CargadorProxy").concat(path);
    String estatico = ConfigLoader.getProperty("CargadorEstatico").concat(path);
    List<HechoAIntegrarDTO> hechosAIntegrar = new ArrayList<>();

    public List<HechoAIntegrarDTO> extraerHechosAIntegrar() {
        String traceId = MDC.get("traceId");
        // Lista local, nueva en cada ejecución
        long t0 = System.currentTimeMillis();
        log.info("Inicio extracción de hechos desde cargadores.");
        List<HechoAIntegrarDTO> resultado = new ArrayList<>();

        try {
            List<HechoAIntegrarDTO> d = extraerHechoConMetricas(dinamico, TipoCargador.DINAMICO);
            int n = (d == null ? 0 : d.size());
            log.info("Cargador DINAMICO OK. items={}", n);
            if (d != null) resultado.addAll(d);
        } catch (Exception e) {
            log.error("Cargador DINAMICO falló.", e);
        }

        try {
            List<HechoAIntegrarDTO> p = extraerHechoConMetricas(proxy, TipoCargador.PROXY);
            int n = (p == null ? 0 : p.size());
            log.info("Cargador PROXY OK. items={}", n);
            if (p != null) resultado.addAll(p);
        } catch (Exception e) {
            log.error("Cargador PROXY falló.", e);
        }

        try {
            List<HechoAIntegrarDTO> eList = extraerHechoConMetricas(estatico, TipoCargador.ESTATICO);
            int n = (eList == null ? 0 : eList.size());
            log.info("Cargador ESTATICO OK. items={}", n);
            if (eList != null) resultado.addAll(eList);
        } catch (Exception e) {
            log.error("Cargador ESTATICO falló.", e);
        }

        long dt = System.currentTimeMillis() - t0;
        log.info("Fin extracción de cargadores. totalItems={} durationMs={}", resultado.size(), dt);

        if (resultado.isEmpty()) {
            log.warn("No se obtuvieron hechos de ningún cargador. (totalItems=0)");
        }

        return resultado; // Nueva lista en cada invocación
    }

    public List<HechoAIntegrarDTO> extraerHecho(String fuente) {

        List<HechoAIntegrarDTO> hechosExtraidos = new ArrayList<>();

        if (fuente == null || fuente.isBlank()) {
            log.warn("extraerHecho: fuente vacía o nula");
            return hechosExtraidos;
        }

        // viene del Scheduler (no lo generes acá)
        String traceId = MDC.get("traceId");
        if (traceId == null || traceId.isBlank()) {
            // por seguridad si alguien llama fuera del scheduler
            traceId = UUID.randomUUID().toString().substring(0, 8);
            MDC.put("traceId", traceId);
        }

        // correlationId propio de este handler (lo querés mantener)
        String correlationId = MDC.get("correlationId");
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString().substring(0, 8);
            MDC.put("correlationId", correlationId);
        }

        // opcional: distinguir cada request a cargador
        String spanId = UUID.randomUUID().toString().substring(0, 8);

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        MDC.put("spanId", spanId);
        try {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fuente))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .header("X-Trace-Id", traceId)              // traceId del scheduler
                .header("X-Correlation-Id", correlationId)  // correlationId mantenido
                .header("X-Span-Id", spanId)                // opcional
                .GET()
                .build();

        long start = System.nanoTime();
        log.info("Request a cargador. url={} span={}", fuente, spanId);


            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            long durMs = (System.nanoTime() - start) / 1_000_000;

            int status = response.statusCode();
            if (status != 200) {
                log.warn("Respuesta cargador no-200. url={} status={} durationMs={} span={}",
                        fuente, status, durMs, spanId);
                return List.of();
            }

            HechoAIntegrarDTO[] array = objectMapper.readValue(response.body(), HechoAIntegrarDTO[].class);
            Collections.addAll(hechosExtraidos, array);

            log.info("Respuesta cargador OK. url={} status=200 items={} durationMs={} span={}",
                    fuente, hechosExtraidos.size(), durMs, spanId);

            return hechosExtraidos;

        } catch (Exception e) {
            log.error("Fallo request a cargador. url={}", fuente, e);
            return List.of();
        }finally {
            MDC.remove("spanId");
        }
    }


    // ==== NUEVO: enum interno para identificar cargador ====
    private enum TipoCargador { DINAMICO, PROXY, ESTATICO }

    private void registrarRequest(TipoCargador tipo) {
        switch (tipo) {
            case DINAMICO -> RegistroMetricas.incReqDinamico();
            case PROXY    -> RegistroMetricas.incReqProxy();
            case ESTATICO -> RegistroMetricas.incReqEstatico();
        }
    }

    private void registrarError(TipoCargador tipo) {
        switch (tipo) {
            case DINAMICO -> RegistroMetricas.incErrDinamico();
            case PROXY    -> RegistroMetricas.incErrProxy();
            case ESTATICO -> RegistroMetricas.incErrEstatico();
        }
    }

    private void registrarTiempo(TipoCargador tipo, long ms) {
        switch (tipo) {
            case DINAMICO -> RegistroMetricas.addTimeDinamico(ms);
            case PROXY    -> RegistroMetricas.addTimeProxy(ms);
            case ESTATICO -> RegistroMetricas.addTimeEstatico(ms);
        }
    }

    // ==== NUEVO: wrapper con métricas por cargador ====
    private List<HechoAIntegrarDTO> extraerHechoConMetricas(String fuente, TipoCargador tipo) {
        registrarRequest(tipo);
        long start = System.nanoTime();
        try {
            List<HechoAIntegrarDTO> res = extraerHecho(fuente);
            long durMs = (System.nanoTime() - start) / 1_000_000;
            registrarTiempo(tipo, durMs);
            return res;
        } catch (Exception e) {
            long durMs = (System.nanoTime() - start) / 1_000_000;
            registrarTiempo(tipo, durMs);
            registrarError(tipo);
            log.error("Error en extracción con métricas. tipo={} durationMs={}", tipo, durMs, e);
            throw e;
        }
    }
}