package web.service;

import web.config.RutasProperties;
import web.dto.FuenteDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class FuenteService {
    private final WebClient metamapaApiADMIN;

    public FuenteService(RutasProperties props) {
        this.metamapaApiADMIN = WebClient.create(props.getAdminBaseUrl());
    }

    // Obtener todas las colecciones del core
    public List<FuenteDTO> getAll() {
        return metamapaApiADMIN.get()
                .uri("/fuentes")
                .retrieve()
                .bodyToFlux(FuenteDTO.class)
                .collectList()
                .block();
    }

    public ResponseEntity<Void> postearFuente(String fuenteJson){
        return metamapaApiADMIN.post()
                .uri("/fuente")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(fuenteJson)
                .retrieve()
                .toBodilessEntity()  // o .bodyToMono(Void.class)
                .block();
    }

    public ResponseEntity<Void> eliminarFuente(Integer idFuente) {
        return metamapaApiADMIN.post()
                .uri("/fuentes/{id}/eliminar", idFuente)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
