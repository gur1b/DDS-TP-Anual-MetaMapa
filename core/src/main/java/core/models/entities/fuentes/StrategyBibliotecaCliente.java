package core.models.entities.fuentes;

import core.models.agregador.HechoAIntegrarDTO;

import java.util.List;

public class StrategyBibliotecaCliente implements StrategyTipoConexion {

    @Override
    public List<HechoAIntegrarDTO> extraerHecho(String fuente){return null;}; //toma los hechos!!

    @Override
    public String devolverTipoDeConexion() {return "BIBLIOTECA";};

}
