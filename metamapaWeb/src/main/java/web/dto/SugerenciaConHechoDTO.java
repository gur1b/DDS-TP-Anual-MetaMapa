package web.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record SugerenciaConHechoDTO(
    Integer id,
    String descripcionSolicitud,
    LocalDate fechaSugerencia,
    Boolean aceptada,

    String tituloSugerencia,
    String descripcionSugerencia,
    LocalDate fechaSucesoSugerencia,
    LocalTime horaSucesoSugerencia,
    String categoriaSugerencia,
    List<String> etiquetasSugerencia,

    //datos del hecho
    Integer idHecho,
    String tituloHecho,
    String descripcionHecho,
    String contribuyenteHecho,
    LocalDate fechaSucesoHecho,
    LocalTime horaSucesoHecho,
    String categoriaHecho,
    List<String> etiquetasHecho){
}
