package web.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import web.config.RutasProperties;

import java.util.List;
import java.util.Map;

@Service
public class EstadisticasService {
    private final WebClient estadisticasApi;

    public EstadisticasService(RutasProperties props) {
        this.estadisticasApi = WebClient.create(props.getEstadisticasBaseUrl());
    }

    public List<Map> provinciaConMasHechos() {
        return estadisticasApi.get()
                .uri("/provincia-con-mas-hechos")
                .retrieve()
                .bodyToFlux(Map.class)
                .collectList()
                .block();
    }

    public List<Map> categoriaMayorCantidad() {
        return estadisticasApi.get()
                .uri("/categoria-mayor-cantidad")
                .retrieve()
                .bodyToFlux(Map.class)
                .collectList()
                .block();
    }

    public Map<String, Object> cantidadSpam() {
        return estadisticasApi.get()
                .uri("/cantidad-spam")
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public List<Map> provinciaConMasHechosPorCategoria(String categoria) {
        return estadisticasApi.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/provincia-con-mas-hechos-por-categoria")
                        .queryParam("categoria", categoria)
                        .build())
                .retrieve()
                .bodyToFlux(Map.class)
                .collectList()
                .block();
    }

    public List<Map> horarioPorCategoria(String categoria) {
        return estadisticasApi.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/horario-categoria")
                        .queryParam("categoria", categoria)
                        .build())
                .retrieve()
                .bodyToFlux(Map.class)
                .collectList()
                .block();
    }
}