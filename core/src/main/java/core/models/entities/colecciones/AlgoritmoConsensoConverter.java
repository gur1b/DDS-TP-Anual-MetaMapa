package core.models.entities.colecciones;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class AlgoritmoConsensoConverter implements AttributeConverter<AlgoritmoConsenso, String> {
    @Override
    public String convertToDatabaseColumn(AlgoritmoConsenso strategy) {
        if (strategy == null) {
            return null;
        }
        return strategy.devolverTipoDeConsenso();
    }

    @Override
    public AlgoritmoConsenso convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return switch (dbData) {
            case "ABSOLUTO" -> new StrategyAbsoluta();
            case "MAYORIA_SIMPLE" -> new StrategyMayoriaSimple();
            case "MULTIPLES_MENCIONES" -> new StrategyMultiplesMenciones();
            default -> throw new IllegalArgumentException("Tipo de algoritmo desconocido: " + dbData);
        };
    }
}
