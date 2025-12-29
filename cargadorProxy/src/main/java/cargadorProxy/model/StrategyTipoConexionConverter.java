package cargadorProxy.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StrategyTipoConexionConverter implements AttributeConverter<StrategyTipoConexion, String> {
    @Override
    public String convertToDatabaseColumn(StrategyTipoConexion strategy) {
        if (strategy == null) {
            return null;
        }
        return strategy.devolverTipoDeConexion();
    }

    @Override
    public StrategyTipoConexion convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return switch (dbData) {
            case "APIREST" -> new StrategyAPIREST();
            case "BIBLIOTECA" -> new StrategyBibliotecaCliente();
            default -> throw new IllegalArgumentException("Tipo de estrategia desconocido: " + dbData);
        };
    }
}
