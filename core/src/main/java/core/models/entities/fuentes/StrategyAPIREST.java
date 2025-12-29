package core.models.entities.fuentes;

import core.models.entities.colecciones.criterios.FiltradorCriterios;
import core.models.agregador.HechoAIntegrarDTO;
import core.models.repository.HechosRepository;

import java.util.List;

public class StrategyAPIREST implements StrategyTipoConexion {

    FiltradorCriterios filtradorCriterios = FiltradorCriterios.getInstance();
    HechosRepository hechosRepository = HechosRepository.getInstance();

    @Override
    public List<HechoAIntegrarDTO> extraerHecho(String fuente){
        return List.of();
    };
    @Override
    public String devolverTipoDeConexion(){
        return "APIREST";
    };


}