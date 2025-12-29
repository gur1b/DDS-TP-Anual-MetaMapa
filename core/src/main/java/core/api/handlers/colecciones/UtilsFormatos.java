package core.api.handlers.colecciones;

import core.models.entities.colecciones.criterios.CriterioCategoria;
import core.models.entities.colecciones.criterios.CriterioUbicacion;
import core.models.entities.hecho.Categoria;
import core.models.entities.hecho.Coordenadas;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class UtilsFormatos {
    public CriterioCategoria transformarCategoriaEnCriterio(String categoria) {
        Categoria categoriaFiltro = new Categoria(categoria);
        return new CriterioCategoria(categoriaFiltro);
    }

    public LocalDate stringALocalDate(String dateString){
        if (dateString == null || dateString.isBlank()) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Debe ser dd-MM-yyyy", e);
        }}

    public CriterioUbicacion transformarUbicacionEnCriterio(String latitud, String longitud){
        Coordenadas coordenadas = new Coordenadas(Double.parseDouble(latitud), Double.parseDouble(longitud));
        return new CriterioUbicacion(coordenadas);
    }
}
