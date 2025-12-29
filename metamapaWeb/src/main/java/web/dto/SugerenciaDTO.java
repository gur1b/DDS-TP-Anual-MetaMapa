package web.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record SugerenciaDTO (
    String descripcionSolicitud,
    LocalDate fechaSugerencia,

    String tituloSugerencia,
    String descripcionSugerencia,
    LocalDate fechaSucesoSugerencia,
    LocalTime horaSucesoSugerencia,
    String categoriaSugerencia,
    List<String> etiquetasSugerencia,

    // datos del hecho
    Integer idHecho
){
}
