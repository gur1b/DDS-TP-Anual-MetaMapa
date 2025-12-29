package web.service;

import web.config.RutasProperties;
import web.dto.HechoDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ReportarService {
    private final WebClient metamapaApi;

    public ReportarService(RutasProperties props) {
        this.metamapaApi = WebClient.create(props.getBaseUrl());
    }

    public String getCategorias(){
        return metamapaApi.get()
                .uri("/categorias")
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful() && response.headers().contentType().isPresent() &&
                            response.headers().contentType().get().toString().contains("json")) {
                        return response.bodyToMono(String.class);
                    } else {
                        return response.bodyToMono(String.class);
                    }
                })
                .block();
    }

    public List<HechoDTO> getAll() {
        return metamapaApi.get()
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

    public ResponseEntity<Void> postearHecho(String hechoJson, boolean urgente, boolean anonimo){
        return metamapaApi.post()
                .uri("/hechos/reportar")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Urgente", String.valueOf(urgente))
                .header("X-Anonimo", String.valueOf(anonimo)) // NUEVO
                .bodyValue(hechoJson)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

}