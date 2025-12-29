package web.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record HechoDTO(
        Integer id,
        String hash,
        String nombre,
        String descripcion,
        String contribuyente,
        LocalDate fechaCarga,
        LocalDate fechaSuceso,
        LocalTime horaSuceso,
        List<String> multimedia,
        List<String> etiquetas,
        String latitud,
        String longitud,
        List<String> categorias,
        String estado
) {}
