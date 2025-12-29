package web.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record SolicitudDeEliminacionDTO(
        Integer id,
        String descripcionSolicitud,
        Integer idHecho,
        String nombre,
        String descripcionHecho,
        String contribuyente,
        LocalDate fechaCarga,
        LocalDate fechaSuceso,
        LocalTime horaSuceso,
        Boolean aceptada) {
}
