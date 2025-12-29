package cargadorProxy.model;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@Service
public class StrategyAPIREST implements StrategyTipoConexion {

    private final WebClient webClient;


    public StrategyAPIREST() {
        this.webClient = WebClient.builder()
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Override
    public List<HechoAIntegrarDTO> agregarHecho(String FuenteBase, HechoAIntegrarDTO hecho){return null;}

    @Override
    public List<HechoAIntegrarDTO> extraerHechosRecientes(String fuente, String codigoFuente) {
        try {
            Mono<List<HechoAIntegrarDTO>> mono = webClient.get()
                    .uri(URI.create(fuente.trim()))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToFlux(HechoAIntegrarDTO.class)
                    .collectList();

            return mono.block();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public String devolverTipoDeConexion() {
        return "APIREST";
    }
}
