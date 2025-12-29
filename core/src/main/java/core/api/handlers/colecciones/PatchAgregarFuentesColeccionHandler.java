package core.api.handlers.colecciones;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.api.DTO.ActualizarFuentesColeccionDTO;
import core.api.DTO.ColeccionConFuentesDTO;
import core.api.DTO.FuenteDTO;
import core.models.agregador.ConfigLoader;
import core.models.entities.fuentes.TipoFuente;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.entities.colecciones.Coleccion;
import core.models.entities.fuentes.Fuente;
import core.models.repository.ColeccionesRepository;
import core.models.repository.FuentesRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

public class PatchAgregarFuentesColeccionHandler implements Handler
{
   private final ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();
    private final FuentesRepository fuentesRepository = FuentesRepository.getInstance();
    private static final Logger log = LoggerFactory.getLogger(PatchAgregarFuentesColeccionHandler.class);
    @Override
    public void handle(@NotNull Context ctx) throws JsonProcessingException {
        int idColeccion = Integer.parseInt(ctx.pathParam("id"));
        ActualizarFuentesColeccionDTO dto = ctx.bodyAsClass(ActualizarFuentesColeccionDTO.class);

        // 1) Traer la colección con FUENTES fetch-eadas (evita LAZY)
        var opt = coleccionesRepository.findByIdFetchFuentes(idColeccion);
        if (opt.isEmpty()) {
            ctx.status(404).result("Colección no encontrada");
            return;
        }
        Coleccion coleccion = opt.get();
        if (coleccion.getFuentes() == null) {
            coleccion.setFuentes(new ArrayList<>());
        }

        if (dto == null || dto.fuentes == null || dto.fuentes.isEmpty()) {
            ctx.status(400).result("Lista de fuentes vacía o inválida");
            return;
        }

        // 2) Evitar duplicados por ID
        Set<Integer> idsExistentes = coleccion.getFuentes().stream()
                .map(Fuente::getId)
                .collect(java.util.stream.Collectors.toSet());

        // 3) Resolver y agregar nuevas fuentes
        for (Integer idFuente : dto.fuentes) {
            if (idFuente == null) continue;

            Fuente fuente = fuentesRepository.getFuente(idFuente);
            if (fuente == null) {
                ctx.status(404).result("Fuente con ID " + idFuente + " no encontrada");
                return;
            }
            if (idsExistentes.add(idFuente)) { // true si no estaba
                coleccion.agregarFuente(fuente);
                enviarFuenteAlCargador(fuente);
            }
        }

        // 4) Persistir cambios (una sola vez)
        coleccionesRepository.update(coleccion);

        // 5) Devolver datos básicos de la colección + fuentes (DTO compuesto)
        var respuesta = ColeccionConFuentesDTO.from(coleccion);
        ctx.status(200).json(respuesta);
    }


    private void enviarFuenteAlCargador(Fuente fuente) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        //String jsonFuente = objectMapper.writeValueAsString(fuente);

        String jsonFuente = this.fuenteAJson(fuente);
        log.info("Fuente a enviar al cargador: " + jsonFuente);

        String url;
        if(fuente.getTipoFuente().equals(TipoFuente.PROXY)){
            url = ConfigLoader.getProperty("CargadorProxy");
        }
        else{
            url = ConfigLoader.getProperty("CargadorEstatico");
        }
        url = url.concat("/agregarFuente");

        log.info("URL: " + url);

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(20))
                .header("Content-Type", "application/json; charset=utf-8"
                )
                .POST(HttpRequest.BodyPublishers.ofString(jsonFuente))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();
            String responseBody = response.body();

            if (status != 201) {
                throw new RuntimeException("Error en la llamada HTTP (" + status + "): " + responseBody);
            }
            log.info("Fuente enviada al cargador: " + fuente.getNombre());
        } catch (Exception e) {
            log.info("Error al enviar fuente " + fuente + ": " + e.getMessage());
        }
    }

    public String fuenteAJson(Fuente fuente) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("nombre", fuente.getNombre());
        jsonMap.put("link", fuente.getLink());
        jsonMap.put("tipoFuente", fuente.getStrategyTipoConexion().devolverTipoDeConexion()); // si quieres el string: this.tipoFuente.toString()
        try {
            return mapper.writeValueAsString(jsonMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }


}

