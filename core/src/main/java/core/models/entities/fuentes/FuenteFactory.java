package core.models.entities.fuentes;

import java.util.concurrent.atomic.AtomicInteger;

public class FuenteFactory {
    private static AtomicInteger idContador = new AtomicInteger(1);

    public static Fuente crearFuente(String nombre, String link, TipoFuente tipo, TipoConexion strategy){
        StrategyTipoConexion strategyTipoConexion = crearStrategy(strategy);
        Fuente fuente = new Fuente();
        fuente.setNombre(nombre);
        fuente.setLink(link);
        fuente.setTipoFuente(tipo);
        fuente.setStrategyTipoConexion(strategyTipoConexion);
        int nuevoId = idContador.getAndIncrement();
        fuente.setId(nuevoId);
        return fuente;
    }


    public static Fuente crearFuente2(Integer id, String nombre, String link, TipoFuente tipo, TipoConexion strategy){
        return crearFuente(nombre, link, tipo, strategy);
    }


    private static StrategyTipoConexion crearStrategy(TipoConexion tipoConexion) {
        return switch (tipoConexion) {
            case CSV -> new StrategyCSV();
            case APIREST -> new StrategyAPIREST();
            case DINAMICA -> new StrategyDinamica();
            default -> throw new IllegalArgumentException("Tipo de conexión no soportada: " + tipoConexion);
        };
    }
}

