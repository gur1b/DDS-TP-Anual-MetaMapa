package web.service;

import web.config.RutasProperties;
import web.dto.HechoDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HechoService {
    private final WebClient metamapaApiADMIN;
    private final WebClient metamapaApiPublic;

    public HechoService(RutasProperties props) {
        this.metamapaApiPublic = WebClient.create(props.getBaseUrl());
        this.metamapaApiADMIN = WebClient.create(props.getAdminBaseUrl());
    }

    public List<HechoDTO> getAll() {
        return metamapaApiADMIN.get()
                .uri("/hechos")
                .exchangeToFlux(response -> {
                    if (response.statusCode().is2xxSuccessful() && response.headers().contentType().isPresent() &&
                            response.headers().contentType().get().toString().contains("json")) {
                        return response.bodyToFlux(HechoDTO.class);
                    } else {
                        return response.bodyToFlux(HechoDTO.class);
                    }
                })
                .collectList()
                .block();
    }

    //PARA EL MAPITA DE MIS HECHOS
    public List<HechoDTO> getByContribuyente(String email) {
        if (email == null || email.isBlank()) return List.of();
        try {
            return metamapaApiADMIN.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/hechos")
                            .queryParam("contribuyente", email)
                            .build())
                    .retrieve()
                    .bodyToFlux(HechoDTO.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            System.err.println("Error en getByContribuyente: " + e.getMessage());
            return List.of();
        }
    }

    // EDITAR (admin 8082) — PATCH /core/api/hechos/{hash}
    public boolean patchByHash(String hash, String nombre, String descripcion, List<String> etiquetas, String latitud, String longitud, String fechaSuceso, String categoria) {
        if (hash == null || hash.isBlank()) return false;

        Map<String, Object> body = new HashMap<>();
        if (nombre != null) body.put("nombre", nombre);
        if (descripcion != null) body.put("descripcion", descripcion);
        if (etiquetas != null) body.put("etiquetas", etiquetas);
        if (latitud != null) body.put("latitud", latitud);
        if (longitud != null) body.put("longitud", longitud);
        if (fechaSuceso != null) body.put("fecha_suceso", fechaSuceso);
        if (categoria != null) body.put("categoria", categoria);

        // Si no hay cambios, lo consideramos OK
        if (body.isEmpty()) return true;

        try {
            metamapaApiADMIN.patch()
                    .uri("/hechos/{hash}", hash)
                    .bodyValue(body)
                    .retrieve()
                    .toBodilessEntity()
                    .block(); // 204 esperado
            return true;
        } catch (WebClientResponseException e) {
            System.err.println("PATCH /hechos/" + hash + " error " + e.getStatusCode() + ": " + e.getResponseBodyAsString());
            return false;
        } catch (Exception e) {
            System.err.println("PATCH /hechos/" + hash + " error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteByHash(String hash) {
        if (hash == null || hash.isBlank()) return false;

        try {
            metamapaApiADMIN.delete()
                    .uri("/hechos/{hash}", hash)
                    .retrieve()
                    .toBodilessEntity()
                    .block(); // 204 esperado
            return true;
        } catch (WebClientResponseException e) {
            System.err.println("DELETE /hechos/" + hash + " error " + e.getStatusCode() + ": " + e.getResponseBodyAsString());
            return false;
        } catch (Exception e) {
            System.err.println("DELETE /hechos/" + hash + " error: " + e.getMessage());
            return false;
        }
    }
}