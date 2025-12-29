package core.api.handlers.hechos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.api.DTO.FuenteDTO;
import core.api.DTO.HechoAIntegrarDINAMICO;
import core.api.handlers.colecciones.PatchAgregarFuentesColeccionHandler;
import core.models.agregador.ConfigLoader;
import core.models.agregador.HechoAIntegrarDTO;
import core.models.agregador.ServicioDeAgregacion;
import core.models.entities.fuentes.TipoFuente;
import core.models.entities.hecho.Contribuyente;
import core.models.entities.usuario.Usuario;
import core.models.repository.ContribuyentesRepository;
import core.models.repository.UsuarioRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PostHechoHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(PatchAgregarFuentesColeccionHandler.class);
    private final UsuarioRepository usuarioRepository = UsuarioRepository.getInstance();
    private final ContribuyentesRepository contribuyentesRepository = ContribuyentesRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        try {
            String urgenteHeader = context.header("X-Urgente");
            boolean urgente = Boolean.parseBoolean(urgenteHeader);

            String anonimoHeader = context.header("X-Anonimo");
            boolean anonimo = Boolean.parseBoolean(anonimoHeader);

            HechoAIntegrarDINAMICO dto = context.bodyAsClass(HechoAIntegrarDINAMICO.class);
            log.info("Crear hecho request urgente={} anonimoHeader={} tituloLen={} descLen={}",
                    urgente,
                    anonimo,
                    (dto != null && dto.getTitulo() != null ? dto.getTitulo().length() : 0),
                    (dto != null && dto.getDescripcion() != null ? dto.getDescripcion().length() : 0)
            );

            // Tomamos el correo que viene como contribuyente desde web
            String correoContribuyente = dto.getContribuyente();

            boolean tratarComoAnonimo = anonimo
                    || correoContribuyente == null
                    || "anonimo".equalsIgnoreCase(correoContribuyente.trim());

            String idContribuyente = null;

            if (tratarComoAnonimo) {
                log.info("Reporte tratado como ANONIMO. No se persiste contribuyente en la base.");
            } else {
                idContribuyente =  registrarContribuyenteDesdeUsuario(correoContribuyente);
            }

            // Construimos el DTO que va al cargador / servicio de agregación
            HechoAIntegrarDINAMICO hechoDTO = new HechoAIntegrarDINAMICO(
                    dto.getTitulo(),
                    dto.getDescripcion(),
                    dto.getCategoria(),
                    dto.getLatitud(),
                    dto.getLongitud(),
                    dto.getFechaSuceso(),
                    dto.getHoraSuceso(),
                    dto.getEtiquetas(),
                    idContribuyente,
                    dto.getMultimedia()
            );

            validarNuevoHecho(hechoDTO);

            if (urgente) {
                HechoAIntegrarDTO hechoUrgente = mapearADTOAgregacion(hechoDTO);
                hechoUrgente.setIdFuente(1);
                System.out.println(hechoUrgente.getHash());
                ServicioDeAgregacion.getInstance().hechoUnicoUrgente(hechoUrgente);

                log.info("Hecho marcado como URGENTE (anonimo={}): se agrega directo, no se envía al cargador dinámico",
                        tratarComoAnonimo);
                context.status(201).result("Hecho urgente agregado directamente");
                return;
            }

            HttpResponse<String> responseCargador = enviarHechoAlCargador(hechoDTO);
            int statusCargador = responseCargador.statusCode();
            String bodyCargador = responseCargador.body();

            if (statusCargador == 201) {
                log.info("Hecho enviado correctamente al cargador (anonimo={})", tratarComoAnonimo);
                context.status(201).result("Hecho enviado correctamente al cargador");
            } else if (statusCargador >= 400 && statusCargador < 500) {
                log.warn("Error 4xx del cargador: {} - {}", statusCargador, bodyCargador);
                context.status(statusCargador).result(bodyCargador);
            } else {
                log.error("Error del cargador: {} - {}", statusCargador, bodyCargador);
                context.status(502).result("Error al registrar el hecho en el cargador");
            }

        } catch (IllegalArgumentException e) {
            log.warn("Validación fallida al crear hecho: {}", e.getMessage());
            context.status(400).result(e.getMessage());
        } catch (IOException | InterruptedException e) {
            log.error("Error de comunicación con el cargador", e);
            context.status(502).result("Error de comunicación con el cargador de hechos");
        } catch (Exception e) {
            log.error("Error inesperado al crear hecho", e);
            context.status(500).result("Error interno del servidor");
        }
    }

    private String resolverNombreContribuyente(String correo) {
        if (correo == null) {
            return null;
        }

        String normalizado = correo.trim().toLowerCase();
        if (normalizado.isBlank()) {
            return null;
        }

        return usuarioRepository.findByCorreo(normalizado)
                .map(u -> {
                    String nombre = u.getNombre();
                    return (nombre != null && !nombre.trim().isEmpty())
                            ? nombre.trim()
                            : null;
                })
                .orElse(null);
    }

    private void validarNuevoHecho(HechoAIntegrarDINAMICO hecho) {
        if (hecho.getTitulo() == null) {
            throw new IllegalArgumentException("El nombre es obligatorio, elegí otro");
        }
    }

    private HttpResponse<String> enviarHechoAlCargador(HechoAIntegrarDINAMICO hecho) throws IOException, InterruptedException {
        String jsonHecho = this.hechoAJson(hecho);
        log.info("Hecho a enviar al cargador dinamico: " + jsonHecho);

        String url = ConfigLoader.getProperty("CargadorDinamico");
        url = url.concat("/agregarHecho");

        log.info("URL: " + url);

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(20))
                .header("Content-Type", "application/json; charset=utf-8")
                .POST(HttpRequest.BodyPublishers.ofString(jsonHecho))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private String hechoAJson(HechoAIntegrarDINAMICO hechoAIntegrarDTO){
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("hash", hechoAIntegrarDTO.getHash());
        jsonMap.put("titulo", hechoAIntegrarDTO.getTitulo());
        jsonMap.put("descripcion", hechoAIntegrarDTO.getDescripcion());
        jsonMap.put("categoria", hechoAIntegrarDTO.getCategoria());
        jsonMap.put("latitud", hechoAIntegrarDTO.getLatitud());
        jsonMap.put("longitud", hechoAIntegrarDTO.getLongitud());
        jsonMap.put("fechaSuceso", hechoAIntegrarDTO.getFechaSuceso());
        jsonMap.put("horaSuceso", hechoAIntegrarDTO.getHoraSuceso());
        jsonMap.put("etiquetas", hechoAIntegrarDTO.getEtiquetas());
        jsonMap.put("contribuyente", hechoAIntegrarDTO.getContribuyente());
        jsonMap.put("multimedia", hechoAIntegrarDTO.getMultimedia());
        try {
            return mapper.writeValueAsString(jsonMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }

    private HechoAIntegrarDTO mapearADTOAgregacion(HechoAIntegrarDINAMICO d) {
        HechoAIntegrarDTO dto = new HechoAIntegrarDTO(d.getTitulo(), d.getDescripcion(),d.getCategoria(), d.getLatitud(), d.getLongitud(), d.getFechaSuceso(), d.getHoraSuceso(),
                d.getEtiquetas(), d.getContribuyente(), d.getMultimedia(), "Cargado por la web");
        dto.setTipoFuente(String.valueOf(TipoFuente.DINAMICA));
        return dto;
    }


    private String registrarContribuyenteDesdeUsuario(String correo) {

        if (correo == null || correo.trim().isBlank()) {
            return null;
        }

        String normalizado = correo.trim().toLowerCase();


        // 1) Ver si el usuario existe
        Optional<Usuario> maybeUsuario = usuarioRepository.findByCorreo(normalizado);
        if (maybeUsuario.isEmpty()) {
            return null; // no hay usuario, no hay contribuyente
        }
        Usuario usuario = maybeUsuario.get();

        // 2) Ver si ya existe un contribuyente con ese mail
        Optional<Contribuyente> existente = contribuyentesRepository.findByMail(normalizado);
        if (existente.isPresent()) {
            log.debug("Contribuyente ya existe para el mail {}", normalizado);
            return String.valueOf(existente.get().getId());
        }

        // 3) Crear nuevo
        Contribuyente nuevo = new Contribuyente();
        nuevo.setNombre(usuario.getNombre());
        nuevo.setMail(normalizado);
        nuevo.setFechaNacimiento(usuario.getFechaNacimiento());
        contribuyentesRepository.add(nuevo);

        log.info("Contribuyente creado para el mail {}", normalizado);
        return String.valueOf(nuevo.getId());
    }

}
