package servicioEstadisticas.csv;

import servicioEstadisticas.GeneradorTodasEstadisticas;

import java.io.IOException;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

public class GeneradorDeEstadisticas {
    GeneradorCSV csv = GeneradorCSV.getInstance();

    public void generarEstadisticas(){
        List<String> headers = new ArrayList<>();
        headers.add("provincia");
        headers.add("cantidadHechos");

        List<Map<String, String>> datos = List.of(
                Map.of("provincia", "Corrientes",  "cantidadHechos", "3"),
                Map.of("provincia", "Entre Rios",  "cantidadHechos", "2"),
                Map.of("provincia", "Misiones",    "cantidadHechos", "1")
        );

        try {
            csv.writeCsv("PRUEBA", datos, headers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    GeneradorTodasEstadisticas generadorTodasEstadisticas = new GeneradorTodasEstadisticas();

    public void generarCSVProvinciaConMasHechos() {
        try {
            csv.writeCsv(
                    "provincia_mas_hechos",
                    csv.toStringRows(generadorTodasEstadisticas.getProvinciaConMasHechos()),
                    List.of("provincia", "cantidad")
            );
        } catch (IOException e) {
            throw new RuntimeException("Error generando CSVs de estadísticas", e);
        }
    }

    public void generarCSVCategoriaMasReportada() {
        try {
            csv.writeCsv(
                    "categoria_mas_reportada",
                    csv.toStringRows(generadorTodasEstadisticas.getCategoriaMasReportada()),
                    List.of("categoria", "cantidad")
            );
        } catch (IOException e) {
            throw new RuntimeException("Error generando CSVs de estadísticas", e);
        }
    }

    public void generarCSVHorarioPorCategoria(String categoria) {
        try {
            csv.writeCsv(
                    "horario_por_categoria_" + categoria,
                    csv.toStringRows(generadorTodasEstadisticas.horarioxCategoria(categoria)),
                    List.of("hora", "cantidad")
            );
        } catch (IOException e) {
            throw new RuntimeException("Error generando CSVs de estadísticas", e);
        }
    }

    public void generarCSVProvinciaConMasHechosPorCategoria(String categoria) {
        try {
            csv.writeCsv(
                    "provincia_mas_hechos_categoria_" + categoria,
                    csv.toStringRows(generadorTodasEstadisticas.provinciaConMasHechosEnCategoria(categoria)),
                    List.of("provincia", "cantidad")
            );
        } catch (IOException e) {
            throw new RuntimeException("Error generando CSVs de estadísticas", e);
        }
    }


    public void generarTodasEstadisticas(String categoria)
    {
        generadorTodasEstadisticas.actualizarEstadisticas();
        try {
            csv.writeCsv(
                    "provincia_mas_hechos",
                    csv.toStringRows(generadorTodasEstadisticas.getProvinciaConMasHechos()),
                    List.of("provincia", "cantidad")
            );

            csv.writeCsv(
                    "categoria_mas_reportada",
                    csv.toStringRows(generadorTodasEstadisticas.getCategoriaMasReportada()),
                    List.of("categoria", "cantidad")
            );

            csv.writeCsv(
                    "solicitudes_eliminacion",
                    List.of(csv.toStringRow(generadorTodasEstadisticas.getCantSolicitudesEliminacion())),
                    List.of( "solicitudes spam", "total de solicitudes")
            );


            csv.writeCsv(
                    "horario_por_categoria_" + categoria,
                    csv.toStringRows(generadorTodasEstadisticas.horarioxCategoria(categoria)),
                    List.of("hora", "cantidad")
            );

            csv.writeCsv(
                    "provincia_mas_hechos_categoria_" + categoria,
                    csv.toStringRows(generadorTodasEstadisticas.provinciaConMasHechosEnCategoria(categoria)),
                    List.of("provincia", "cantidad")
            );



        } catch (IOException e) {
            throw new RuntimeException("Error generando CSVs de estadísticas", e);
        }

    }

}

