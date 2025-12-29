package web.service;

import web.config.RutasProperties;
import web.dto.ColeccionDTO;
import web.dto.HechoDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ColeccionService {
    private final WebClient metamapaApi;
    private final WebClient metamapaApiADMIN;

    public ColeccionService(RutasProperties props) {
        this.metamapaApi = WebClient.create(props.getBaseUrl());
        this.metamapaApiADMIN = WebClient.create(props.getAdminBaseUrl());
    }

    @Autowired
    private ObjectMapper objectMapper;

    // Obtener todas las colecciones del core
    public List<ColeccionDTO> getAll() {
        return metamapaApiADMIN.get()
                .uri("/colecciones")
                .retrieve()
                .bodyToFlux(ColeccionDTO.class)
                .collectList()
                .block();
    }
    public List<HechoDTO> getHechosDeColeccion(Integer id) {
        return metamapaApi.get()
                .uri(uri -> uri.path("/colecciones/{id}/hechos").build(id))
                .exchangeToFlux(response -> {
                    if (response.statusCode().is2xxSuccessful() && response.headers().contentType().isPresent() &&
                            response.headers().contentType().get().toString().contains("json")) {
                        return response.bodyToFlux(HechoDTO.class);
                    } else {
                        return response.bodyToFlux(HechoDTO.class);
                    }
                })
                .filter(h -> {
                    String estado = h.estado();
                    return estado == null || !estado.trim().equals("INACTIVO");
                })
                .collectList()
                .block();
    }

    public List<HechoDTO> getHechosVisibles(Integer id, String modoDeNavegacion) {
        return metamapaApi.get()
                .uri(uri -> uri.path("/colecciones/{id}/{modoDeNavegacion}/hechos").build(id, modoDeNavegacion))
                .exchangeToFlux(response -> {
                    if (response.statusCode().is2xxSuccessful() && response.headers().contentType().isPresent() &&
                            response.headers().contentType().get().toString().contains("json")) {
                        return response.bodyToFlux(HechoDTO.class);
                    } else {
                        return response.bodyToFlux(HechoDTO.class);
                    }
                })
                .filter(h -> {
                    String estado = h.estado();
                    return estado == null || !estado.trim().equals("INACTIVO");
                })
                .collectList()
                .block();
    }

    public ColeccionDTO getById(Integer id){
        try {
            // 1. Traemos la respuesta como un árbol JSON genérico (JsonNode)
            JsonNode root = metamapaApi.get()
                    .uri(uri -> uri.path("/colecciones/{id}").build(id))
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            // 2. Eliminamos manualmente los campos conflictivos para este endpoint
            if (root instanceof ObjectNode) {
                ObjectNode objectNode = (ObjectNode) root;
                objectNode.remove("fuentes"); // Borramos fuentes (que vienen como objetos)
                objectNode.remove("hechos");  // Borramos hechos (que vienen como objetos)
                // Nota: ColeccionDTO espera List<Integer>, pero el JSON trae List<Object>
            }

            // 3. Convertimos el JSON "limpio" a tu ColeccionDTO existente
            return objectMapper.treeToValue(root, ColeccionDTO.class);

        } catch (Exception e) {
            e.printStackTrace();
            return null; // O manejar el error como prefieras
        }
    }

    public boolean deleteById(Integer id) {
        try {
            var resp = metamapaApiADMIN.delete()
                    .uri(uri -> uri.path("/colecciones/{id}").build(id)) // baseUrl: http://localhost:8082/core/api
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return resp != null && resp.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    //VER
    public boolean patchColeccion(Integer id, String nuevoTitulo, String nuevaDescripcion, Object fuentes, String algoritmoConsenso, String modoDeNavegacion, Object criterios) {
        Map<String, Object> body = new HashMap<>();
        if (nuevoTitulo != null && !nuevoTitulo.isBlank()) {
            body.put("titulo", nuevoTitulo);
        }
        if (nuevaDescripcion != null && !nuevaDescripcion.isBlank()) {
            body.put("descripcionColeccion", nuevaDescripcion);
        }
        if (fuentes != null) {
            body.put("fuentes", fuentes);
        }
        if (algoritmoConsenso != null && !algoritmoConsenso.isBlank()) {
            body.put("algoritmoConsenso", algoritmoConsenso);
        }
        if (modoDeNavegacion != null && !modoDeNavegacion.isBlank()) {
            body.put("modoDeNavegacion", modoDeNavegacion);
        }
        if (criterios != null) {
            body.put("criterioDePertenencia", criterios);
        }
        try {
            var resp = metamapaApiADMIN
                    .patch()
                    .uri("/colecciones/{id}", id)
                    .bodyValue(body)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return resp != null && resp.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }
    public boolean crearColeccion(Map<String, Object> payload) {
        try {
            // Usamos metamapaApiADMIN (puerto 8082)
            var resp = metamapaApiADMIN
                    .post() // Usamos POST
                    .uri("/colecciones") // El endpoint del 'core'
                    .bodyValue(payload) // Enviamos el JSON
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            // El 'core' devuelve 201 (Created) si tiene éxito
            return resp != null && resp.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            // Imprime el error si el 'core' está caído o rechaza la petición
            e.printStackTrace();
            return false;
        }
    }

    public ResponseEntity<Void> ejecutarAgregacion(){
        return metamapaApiADMIN.post()
                .uri("/ejecutarServicio")
                .retrieve()
                .toBodilessEntity()
                .block();
    }


    public List<HechoDTO> getHechosFiltradosDeColeccion(
            Integer coleccionId,
            String modo,
            String titulo,
            String descripcion,
            String etiqueta,
            String categoria,
            String provincia,
            Boolean soloMultimedia,
            String fechaDesdeSuceso,
            String fechaHastaSuceso,
            String fechaDesdeCarga,
            String fechaHastaCarga,
            String horaDesdeSuceso,
            String horaHastaSuceso
    ) {
        try {
            return metamapaApi .get()
                    .uri(uriBuilder -> {
                        // Suponiendo: baseUrl = http://localhost:8081/core/api
                        uriBuilder.path("/colecciones/" + coleccionId + "/" + modo + "/hechos");
                        if (titulo != null && !titulo.trim().isEmpty()) uriBuilder.queryParam("titulo", titulo);
                        if (descripcion != null && !descripcion.trim().isEmpty()) uriBuilder.queryParam("descripcion", descripcion);
                        if (etiqueta != null && !etiqueta.trim().isEmpty()) uriBuilder.queryParam("etiqueta", etiqueta);
                        if (categoria != null && !categoria.trim().isEmpty()) uriBuilder.queryParam("categoria", categoria);
                        if (provincia != null && !provincia.trim().isEmpty()) uriBuilder.queryParam("provincia", provincia);
                        if (Boolean.TRUE.equals(soloMultimedia)) uriBuilder.queryParam("soloMultimedia", "true");
                        if (fechaDesdeSuceso != null && !fechaDesdeSuceso.trim().isEmpty()) uriBuilder.queryParam("fechaDesdeSuceso", fechaDesdeSuceso);
                        if (fechaHastaSuceso != null && !fechaHastaSuceso.trim().isEmpty()) uriBuilder.queryParam("fechaHastaSuceso", fechaHastaSuceso);
                        if (fechaDesdeCarga != null && !fechaDesdeCarga.trim().isEmpty()) uriBuilder.queryParam("fechaDesdeCarga", fechaDesdeCarga);
                        if (fechaHastaCarga != null && !fechaHastaCarga.trim().isEmpty()) uriBuilder.queryParam("fechaHastaCarga", fechaHastaCarga);
                        if (horaDesdeSuceso != null && !horaDesdeSuceso.trim().isEmpty()) uriBuilder.queryParam("horaDesdeSuceso", horaDesdeSuceso);
                        if (horaHastaSuceso != null && !horaHastaSuceso.trim().isEmpty()) uriBuilder.queryParam("horaHastaSuceso", horaHastaSuceso);
                        return uriBuilder.build();
                    })
                    .retrieve()
                    .bodyToFlux(HechoDTO.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            System.err.println("Error en getHechosFiltradosDeColeccion: " + e.getMessage());
            return List.of();
        }


    }

    public List<String> getCategorias() {
        try {
            String categoriasRaw = metamapaApi.get()
                    .uri("/categorias")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Parsear explícitamente el string del core a List<String>
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(categoriasRaw, new com.fasterxml.jackson.core.type.TypeReference<List<String>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return java.util.Collections.emptyList();
        }
    }

    public List<String> getEtiquetas() {
        try {
            String etiquetasRaw = metamapaApi.get()
                    .uri("/etiquetas")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(etiquetasRaw, new com.fasterxml.jackson.core.type.TypeReference<List<String>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return java.util.Collections.emptyList();
        }
    }
}

