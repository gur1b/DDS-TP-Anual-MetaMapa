package web.service;

import web.config.RutasProperties;
import web.dto.SolicitudDeEliminacionDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SolicitudesEliminacionService {
    private final WebClient metamapaApi;
    private final WebClient metamapaApiADMIN;

    public SolicitudesEliminacionService(RutasProperties props) {
        this.metamapaApi = WebClient.create(props.getBaseUrl());
        this.metamapaApiADMIN = WebClient.create(props.getAdminBaseUrl());
    }


    public List<SolicitudDeEliminacionDTO> getAll() {
        return metamapaApiADMIN.get()
                .uri("/solicitudes")
                .retrieve()
                .bodyToFlux(SolicitudDeEliminacionDTO.class)
                .filter(dto -> dto.aceptada() == null)
                .collectList()
                .block();
    }


    public boolean rechazar(Integer id) {
        try {
            var resp = metamapaApiADMIN.post()
                    .uri(uri -> uri.path("/solicitudes/{id}/rechazar").build(id)) // baseUrl: http://localhost:8082/core/api
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return resp != null && resp.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean aceptar(Integer id) {
        try {
            var resp = metamapaApiADMIN.post()
                    .uri(uri -> uri.path("/solicitudes/{id}/aceptar").build(id)) // baseUrl: http://localhost:8082/core/api
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return resp != null && resp.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean crear(Integer idHecho, String descripcion) {
        Map<String, Object> body = new HashMap<>();
        body.put("hecho", idHecho);
        body.put("descripcion", descripcion);
        try {
            var resp = metamapaApi.post()
                    .uri("/solicitudes")
                    .bodyValue(body)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return resp != null && resp.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            System.err.println("Error creando solicitud de eliminación: " + e.getMessage());
            return false;
        }
    }
}
