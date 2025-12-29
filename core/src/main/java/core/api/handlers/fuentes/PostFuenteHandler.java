package core.api.handlers.fuentes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.api.DTO.FuenteDTO;
import core.api.DTO.HechoAIntegrarDINAMICO;
import core.api.handlers.colecciones.PatchAgregarFuentesColeccionHandler;
import core.models.agregador.ConfigLoader;
import core.models.entities.fuentes.*;
import core.models.repository.FuentesRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.swing.plaf.PanelUI;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class PostFuenteHandler implements Handler {


    private static final Logger log = LoggerFactory.getLogger(PostFuenteHandler.class);


    FuentesRepository fuentesRepository = FuentesRepository.getInstance();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    private static final String CARGADOR_ESTATICO_BASE_URL = ConfigLoader.getProperty("CargadorEstatico");
    private static final String CARGADOR_DINAMICO_BASE_URL = ConfigLoader.getProperty("CargadorProxy");

    @Override
    public void handle(@NotNull Context context) throws Exception {
        FuenteDTO dto = context.bodyAsClass(FuenteDTO.class);
        System.out.println("DTO.strategyTipoConexion = " + dto.getStrategyTipoConexion());

        StrategyTipoConexion strategyTipoConexion = strategyStringToStrategy(dto.getStrategyTipoConexion());
        System.out.println("Strategy creada = " + strategyTipoConexion
                + " (tipo: " + (strategyTipoConexion != null ? strategyTipoConexion.getClass().getSimpleName() : "null") + ")");


        log.info("Creando fuente nombre={} strategy={}",
                (dto != null ? dto.getNombre() : "null"),
                (dto != null ? dto.getStrategyTipoConexion() : "null"));

        TipoFuente tipoFuente = obtenerTipoFuente(dto.getStrategyTipoConexion());

        if (strategyTipoConexion == null || tipoFuente == null) {
            log.warn("Tipo/estrategia no reconocidos strategy={}", dto.getStrategyTipoConexion());
            context.status(400).result("Tipo de fuente / estrategia no reconocidos");
            return;
        }

        Fuente fuenteCore = new Fuente(
                dto.getNombre(),
                dto.getLink(),
                tipoFuente,
                strategyTipoConexion
        );

        fuentesRepository.add(fuenteCore);
        FuenteDTO respuestaCore = FuenteDTO.from(fuenteCore);

        // 4) Construir el JSON que se manda al cargador
        //    Reusamos FuenteDTO pero completando tipoFuente y strategyTipoConexion
        FuenteDTO dtoParaCargador = new FuenteDTO();
        dtoParaCargador.setId(respuestaCore.getId());
        dtoParaCargador.setNombre(dto.getNombre());
        dtoParaCargador.setLink(dto.getLink());
        dtoParaCargador.setTipoFuente(tipoFuente.name());                 // "ESTATICA" o "PROXY"
        dtoParaCargador.setStrategyTipoConexion(dto.getStrategyTipoConexion()); // "CSV", "API REST" O "BIBLIOTECA"

        String jsonBody = mapper.writeValueAsString(dtoParaCargador);

        // 5) Elegir a qué cargador pegarle según la estrategia
        String urlCargador;

        if ("CSV".equalsIgnoreCase(dto.getStrategyTipoConexion())) {
            // fuente estática → cargadorEstatica
            urlCargador = CARGADOR_ESTATICO_BASE_URL + "/agregarFuente";
        } else if ("API REST".equalsIgnoreCase(dto.getStrategyTipoConexion()) || "BIBLIOTECA".equalsIgnoreCase(dto.getStrategyTipoConexion())) {
            // fuente API → cargador dinámico / proxy
            urlCargador = CARGADOR_DINAMICO_BASE_URL + "/agregarFuente";
        } else {
            log.warn("StrategyTipoConexion no soportada strategy={}", dto.getStrategyTipoConexion());
            context.status(400).result("StrategyTipoConexion no soportada: " + dto.getStrategyTipoConexion());
            return;
        }

        String traceId = MDC.get("traceId");
        // 6) Armar request HTTP hacia el cargador
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlCargador))
                .header("Content-Type", "application/json")
                .timeout(java.time.Duration.ofSeconds(10))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // 7) Enviar al cargador y manejar respuesta
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() / 100 != 2) {
            log.error("Error registrando fuente en cargador url={} status={} body={}",
                    urlCargador, response.statusCode(), safe(response.body()));

            // opcional: podrías deshacer (no guardar la fuente en el core si falló)
            context.status(502).result("Error al registrar fuente en cargador");
            return;
        }
        log.info("Fuente creada ok id={} tipoFuente={} strategy={} cargadorUrl={}",
                respuestaCore.getId(), tipoFuente, dto.getStrategyTipoConexion(), urlCargador);
        context.status(201).json(respuestaCore);
    }

    public static TipoFuente fromString(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return TipoFuente.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public StrategyTipoConexion strategyStringToStrategy(String strategy) {
        if(strategy == null) {
            System.out.println("LLEGA NULL");
            return null;
        }
        return switch(strategy){
            case "API REST" -> new StrategyAPIREST();
            case "CSV" -> new StrategyCSV();
            case "BIBLIOTECA" -> new StrategyBibliotecaCliente();
            default -> null;
        };
    }


    public TipoFuente obtenerTipoFuente(String tipoFuente) {
        return switch (tipoFuente) {
            case "CSV" -> TipoFuente.ESTATICA;
            case "API REST","BIBLIOTECA" -> TipoFuente.PROXY;
            default -> null;
        };
    }

    private String safe(String s) {
        if (s == null) return "-";
        return s.length() > 200 ? s.substring(0, 200) + "..." : s;
    }
}
