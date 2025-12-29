package cargadorProxy.model;

import cargadorProxy.model.HechoAIntegrarDTO;
import cargadorProxy.model.StrategyTipoConexion;

import java.util.List;

public class StrategyBibliotecaCliente implements StrategyTipoConexion {

    public StrategyBibliotecaCliente() {
    }

    @Override
    public List<HechoAIntegrarDTO> agregarHecho(String FuenteBase, HechoAIntegrarDTO hecho) {
        return null;
    }

    @Override
    public List<HechoAIntegrarDTO> extraerHechosRecientes(String fuente, String codigoFuente){return null;}; //toma los hechos!!

    @Override
    public String devolverTipoDeConexion() {
        return "BIBLIOTECA";
    }
}
