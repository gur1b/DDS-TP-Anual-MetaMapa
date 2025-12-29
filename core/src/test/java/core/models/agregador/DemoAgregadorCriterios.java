package core.models.agregador;

import core.models.entities.colecciones.Coleccion;
import core.models.entities.colecciones.criterios.Criterio;
import core.models.entities.colecciones.criterios.CriterioDescripcion;
import core.models.entities.colecciones.criterios.CriterioNombre;
import core.models.entities.fuentes.Fuente;
import core.models.entities.fuentes.FuenteFactory;
import core.models.entities.fuentes.TipoConexion;
import core.models.entities.fuentes.TipoFuente;
import core.models.repository.ColeccionesRepository;
import core.models.repository.FuentesRepository;
import core.models.repository.HechosRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DemoAgregadorCriterios {
    public static void main(String[] args){
        ServicioDeAgregacion servicioDeAgregacion = ServicioDeAgregacion.getInstance();

        FuentesRepository fuentesRepository = FuentesRepository.getInstance();
        Fuente fuenteCSV = FuenteFactory.crearFuente("Desastres Sanitarios", "eventosSanitariosPrueba1.csv", TipoFuente.ESTATICA, TipoConexion.CSV);
        Fuente fuenteAPI = FuenteFactory.crearFuente("API de ejemplo","https://684b1942165d05c5d35b843b.mockapi.io/metamapa/hechos",TipoFuente.PROXY, TipoConexion.APIREST);
        fuentesRepository.add(fuenteCSV);
        fuentesRepository.add(fuenteAPI);


        List<Fuente> fuentes = new ArrayList<>();
        fuentes.add(fuenteCSV);
        fuentes.add(fuenteAPI);

        HechoAIntegrarDTO hecho1 = new HechoAIntegrarDTO(
                "Incendio en reserva natural Buenos Aires",
                "Se detectó un incendio de gran magnitud en la zona norte de la reserva.",
                "Medio Ambiente",
                "-34.6037", "-58.3816",
                "2025-09-01",
                Arrays.asList("incendio", "emergencia", "reserva"),
                "Protección Civil",
                "http://ejemplo.com/incendio1.jpg",
                "ESTATICA", true, 1
        );

        HechoAIntegrarDTO hecho2 = new HechoAIntegrarDTO(
                "Protesta estudiantil  Buenos Aires",
                "Estudiantes se movilizaron reclamando mejoras edilicias.",
                "Social",
                "-34.6158", "-58.4333",
                "2025-09-05",
                Arrays.asList("protesta", "educacion"),
                "Diario Local",
                "http://ejemplo.com/protesta.png",
                "ESTATICA", true, 2
        );

        HechoAIntegrarDTO hecho3 = new HechoAIntegrarDTO(
                "Lanzamiento de satélite argentino  Buenos Aires",
                "La CONAE lanzó un nuevo satélite de observación terrestre. emergencia",
                "Ciencia y Tecnología",
                "-31.4201", "-64.1888",
                "2025-08-20",
                Arrays.asList("satelite", "espacio", "tecnologia"),
                "CONAE",
                "http://ejemplo.com/satelite.jpg",
                "PROXY", false, 1
        );

        HechoAIntegrarDTO hecho4 = new HechoAIntegrarDTO(
                "Corte de energía en barrio céntrico Buenos Aires",
                "Más de 20 manzanas quedaron sin luz durante 4 horas. emergencia",
                "Servicios",
                "-34.9214", "-57.9545",
                "2025-09-10",
                Arrays.asList("energia", "corte", "servicios"),
                "EDELAP",
                "http://ejemplo.com/corte.jpg",
                "PROXY", true, 1
        );

        HechoAIntegrarDTO hecho15 = new HechoAIntegrarDTO(
                "Corte de energía en barrio céntrico  Buenos Aires",
                "Más de 20 manzanas quedaron sin luz durante 4 horas. emergencia",
                "Servicios",
                "-34.9214", "-57.9545",
                "2025-09-10",
                Arrays.asList("energia", "corte", "servicios"),
                "EDELAP",
                "http://ejemplo.com/corte.jpg",
                "PROXY", true, 1
        );


        HechoAIntegrarDTO hecho17 = new HechoAIntegrarDTO(
                " Corte de energía en barrio céntrico Buenos Aires",
                "Más de 20 manzanas quedaron sin luz durante 4 horas. emergencia",
                "Servicios",
                "-34.9214", "-57.9545",
                "2025-09-10",
                Arrays.asList("energia", "corte", "servicios"),
                "EDELAP",
                "http://ejemplo.com/corte.jpg",
                "PROXY", true, 2
        );

        HechoAIntegrarDTO hecho18 = new HechoAIntegrarDTO(
                " Corte de energía en barrio céntrico Buenos Aires",
                "Más de 20 manzanas quedaron sin luz durante 4 horas. emergencia",
                "Servicios",
                "-34.9214", "-57.9545",
                "2025-09-10",
                Arrays.asList("energia", "corte", "servicios"),
                "EDELAP",
                "http://ejemplo.com/corte.jpg",
                "PROXY", true, 2
        );
        HechoAIntegrarDTO hecho19 = new HechoAIntegrarDTO(
                " Corte de energía en barrio céntrico Buenos Aires",
                "Más de 20 manzanas quedaron sin luz durante 4 horas. emergencia",
                "Servicios",
                "-34.9214", "-57.9545",
                "2025-09-10",
                Arrays.asList("energia", "corte", "servicios"),
                "EDELAP",
                "http://ejemplo.com/corte.jpg",
                "PROXY", true, 2
        );
        HechoAIntegrarDTO hecho20 = new HechoAIntegrarDTO(
                " Corte de energía en barrio céntrico Buenos Aires",
                "Más de 20 manzanas quedaron sin luz durante 4 horas.  emergencia",
                "Servicios",
                "-34.9214", "-57.9545",
                "2025-09-10",
                Arrays.asList("energia", "corte", "servicios"),
                "EDELAP",
                "http://ejemplo.com/corte.jpg",
                "PROXY", true, 2
        );

        CriterioNombre criterioNombre1 = new CriterioNombre("Buenos Aires");
        CriterioDescripcion criterioNombre2 = new CriterioDescripcion("emergencia");
        List<Criterio> criterios = List.of(criterioNombre1, criterioNombre2);

        List<HechoAIntegrarDTO> hechos = Arrays.asList(
                hecho1, hecho2, hecho3, hecho4, hecho15, hecho17, hecho18, hecho19, hecho20
        );
        ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();
        Coleccion coleccionPrueba  = new Coleccion(1, "Emergencia en Buenos Aires", "", criterios, fuentes, null, null, null);
        coleccionesRepository.add(coleccionPrueba);


        HechosRepository hechosRepository = HechosRepository.getInstance();

        System.out.println("**Hay " + hechosRepository.obtenerTodas().size() + " hechos en el repositorio**");
        servicioDeAgregacion.actualizarColecciones(hechos);
        System.out.println("**La coleccion " + coleccionPrueba.toString() + " tiene " + coleccionPrueba.getHechos().size() + " hechos**");
        System.out.println("**Hay " + hechosRepository.obtenerTodas().size() + " hechos en el repositorio**");

    }
}
