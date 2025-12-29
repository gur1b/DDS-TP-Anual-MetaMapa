package utils;

import servicioEstadisticas.DTO.HechoDTO;
import servicioEstadisticas.model.entities.Hecho;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class HechoMapperUtils {
    private HechoMapperUtils() {}

    public static Hecho toEntity(HechoDTO req) {
        System.out.println("Antes de empezar a mapear" + req.toString());
        LocalDateTime fecha = parsearFechaFlexible(req.getFecha_suceso());
        System.out.println("Llega esto: " + fecha);
        if (fecha == null) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use 'yyyy-MM-dd', 'yyyy-MM-dd HH:mm:ss' o ISO 'yyyy-MM-ddTHH:mm:ss'");
        }
        return new Hecho(
                req.getHash(),
                null,// CAMBIAR DESPUES HOLA SOY UN PROBLEMA DEL FUTURO NO ME IGNORES HIJA DE PUTA!!!!!
                fecha,
                null,
                req.getProvincia()
                ,null // CAMBIAR DESPUES HOLA SOY UN PROBLEMA DEL FUTURO NO ME IGNORES HIJA DE PUTA!!!!!
        );
    }

    private static LocalDateTime parsearFechaFlexible(String fechaStr) {
        System.out.println("Antes de parsear: " + fechaStr);
        if (fechaStr == null || fechaStr.isEmpty()) return null;
        // Intentar 'yyyy-MM-dd HH:mm:ss' y ISO 'yyyy-MM-ddTHH:mm:ss'
        DateTimeFormatter[] formatos = new DateTimeFormatter[] {
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME
        };
        for (DateTimeFormatter f : formatos) {
            try {
                return LocalDateTime.parse(fechaStr, f);
            } catch (DateTimeParseException ignored) {}
        }
        // Si viene solo fecha (yyyy-MM-dd), usar inicio del día
        try {
            LocalDate d = LocalDate.parse(fechaStr, DateTimeFormatter.ISO_LOCAL_DATE);
            return d.atStartOfDay();
        } catch (DateTimeParseException ignored) {}
        return null;
    }
}