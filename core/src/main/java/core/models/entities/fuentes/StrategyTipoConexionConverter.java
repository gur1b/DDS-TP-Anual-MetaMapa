package core.models.entities.fuentes;

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
            case "CSV" -> new StrategyCSV();
            case "DINAMICA" -> new StrategyDinamica();
            default -> throw new IllegalArgumentException("Tipo de estrategia desconocido: " + dbData);
        };
    }
}
