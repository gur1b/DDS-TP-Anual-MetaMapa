package core.models.entities.fuentes;


import com.opencsv.CSVReader;
import core.models.entities.colecciones.criterios.FiltradorCriterios;
import core.models.agregador.HechoAIntegrarDTO;
import core.models.repository.HechosRepository;

import java.io.FileReader;

import java.util.*;
import java.util.stream.Collectors;

public class StrategyCSV implements StrategyTipoConexion {

    FiltradorCriterios filtradorCriterios = FiltradorCriterios.getInstance();
    HechosRepository hechosRepository = HechosRepository.getInstance();

    @Override
    public List<HechoAIntegrarDTO> extraerHecho(String fuenteBase) {
        List<HechoAIntegrarDTO> hechos = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(fuenteBase))) {
            String[] fila;
            reader.readNext(); // header

            while ((fila = reader.readNext()) != null) {
                if (fila.length != 6) {
                    System.out.println("Línea ignorada: no tiene 6 columnas.");
                    continue;
                }

                if (Arrays.stream(fila).anyMatch(col -> col == null || col.trim().isEmpty())) {
                    System.out.println("Fila ignorada: tiene campos vacíos. " + Arrays.toString(fila));
                    continue;
                }

                String titulo = fila[0].trim();
                String descripcion = fila[1].trim();
                String categoria = fila[2].trim();
                String latitud = fila[3].trim();
                String longitud = fila[4].trim();
                String fecha = fila[5].trim();

                HechoAIntegrarDTO hechoAIntegrarDTO = new HechoAIntegrarDTO(
                        titulo, descripcion, categoria, latitud, longitud, fecha, fuenteBase
                );
                hechos.add(hechoAIntegrarDTO);

                if (hechos.size() >= 100) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hechos.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public String devolverTipoDeConexion(){
        return "CSV";
    };
}

