package core.models.entities.fuentes;

import core.models.agregador.HechoAIntegrarDTO;
import core.models.repository.HechosRepository;

import java.io.File;
import java.util.List;


public class CSVTest {
    public static void main(String[] args) {
        StrategyCSV strategy = new StrategyCSV();
        Fuente fuente = FuenteFactory.crearFuente("Desastres Sanitarios", "desastres_sanitarios_contaminacion_argentina.csv", TipoFuente.ESTATICA, TipoConexion.CSV);
        HechosRepository hechosRepository = HechosRepository.getInstance();

        System.out.println("*********DEMO CSV: SOLO MUESTRA LOS PRIMEROS 15**************");
        System.out.println("Hay " + hechosRepository.obtenerTodas().size() + " hechos en el repositorio");
        System.out.println("Buscando archivo...");
        File archivo = new File(fuente.getLink());
        if (!archivo.exists()) {
            System.out.println("...Archivo no encontrado: " + fuente.getLink());
            return;
        }
        else {System.out.println("...¡Se encontro el archivo!");}
        System.out.println( "                          ");

        List<HechoAIntegrarDTO> todosLosHechos = fuente.extraerHechos();

        List<HechoAIntegrarDTO> primeros15 = todosLosHechos.stream().limit(15).toList();
        System.out.println("Hay " + hechosRepository.obtenerTodas().size() + " Hechos en el repositorio");
        System.out.println("Ejemplo de 15 hechos encontrados:");
        System.out.println("************************************************************");

        primeros15.forEach(System.out::println);

    }
    }

