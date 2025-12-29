package core.models.entities.fuentes;

import core.models.agregador.HechoAIntegrarDTO;

import java.util.List;

public class StrategyDinamica implements StrategyTipoConexion {

    @Override
    public List<HechoAIntegrarDTO> extraerHecho(String fuente){return null;}

    @Override
    public String devolverTipoDeConexion() {return "DINAMICA";};

}
