package cargadorEstatica.model;

import java.util.List;

public interface StrategyTipoConexion {
    public List<HechoAIntegrarDTO> extraerHechosRecientes(String fuente, String codigoFuente);

    public String devolverTipoDeConexion();
}