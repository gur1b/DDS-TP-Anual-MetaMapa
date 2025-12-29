package web.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record CriterioDTO(
        String type,        // "nombre", "descripcion", "categoria", "ubicacion", "fechaSuceso", "fechaCarga"
        String palabraClave,
        String categoria,
        Double latitud,
        Double longitud,
        LocalDate desde,
        LocalDate hasta,
        LocalTime horaDesde,
        LocalTime horaHasta
) {}
