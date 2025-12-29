package cargadorDinamica.model;

import cargadorDinamica.repository.DinamicaRepository;

import java.util.List;

public class StrategyDinamica implements StrategyTipoConexion {

    private final DinamicaRepository dinamicaRepository = DinamicaRepository.getInstance();

    @Override
    public List<HechoAIntegrarDTO> extraerHechosRecientes(String fuente, String codigoFuente){
        List<HechoAIntegrarDTO> hechos =dinamicaRepository.getHechosNoProcesados();
        hechos.forEach(h -> h.setTipoFuente("DINAMICA"));
        return hechos;
    }

    @Override
    public String devolverTipoDeConexion() {return "DINAMICA";};

}

