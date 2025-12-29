package core.models.agregador;

import core.models.entities.colecciones.Coleccion;
import core.models.entities.fuentes.Fuente;
import core.models.entities.fuentes.FuenteFactory;
import core.models.entities.fuentes.TipoConexion;
import core.models.entities.fuentes.TipoFuente;
import core.models.entities.hecho.Hecho;
import core.models.repository.ColeccionesRepository;
import core.models.repository.FuentesRepository;
import core.models.repository.HechosRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DemoAgregadorCasoInicial {
    public static void main(String[] args){
        ServicioDeAgregacion servicioDeAgregacion = ServicioDeAgregacion.getInstance();

        FuentesRepository fuentesRepository = FuentesRepository.getInstance();
        Fuente fuenteCSV = FuenteFactory.crearFuente2(1, "Desastres Sanitarios", "desastres_sanitarios_contaminacion_argentina.csv", TipoFuente.ESTATICA, TipoConexion.CSV);
        Fuente fuenteAPI = FuenteFactory.crearFuente2(2, "API de ejemplo","https://684b1942165d05c5d35b843b.mockapi.io/metamapa/hechos",TipoFuente.PROXY, TipoConexion.APIREST);
        fuentesRepository.add(fuenteCSV);
        fuentesRepository.add(fuenteAPI);

        List<Fuente> fuentes = new ArrayList<>();
        fuentes.add(fuenteCSV);

        ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();
        Coleccion coleccionPrueba  = new Coleccion(1, "Todos", "Todos los hechos que existen", null, fuentes, null,null, null);
        coleccionesRepository.add(coleccionPrueba);

        HechosRepository hechosRepository = HechosRepository.getInstance();

        HechoAIntegrarDTO hecho1 = new HechoAIntegrarDTO(
                "Incendio en reserva natural",
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
                "Protesta estudiantil",
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
                "Lanzamiento de satélite argentino",
                "La CONAE lanzó un nuevo satélite de observación terrestre.",
                "Ciencia y Tecnología",
                "-31.4201", "-64.1888",
                "2025-08-20",
                Arrays.asList("satelite", "espacio", "tecnologia"),
                "CONAE",
                "http://ejemplo.com/satelite.jpg",
                "PROXY", false, 1
        );

        HechoAIntegrarDTO hecho4 = new HechoAIntegrarDTO(
                "Corte de energía en barrio céntrico",
                "Más de 20 manzanas quedaron sin luz durante 4 horas.",
                "Servicios",
                "-34.9214", "-57.9545",
                "2025-09-10",
                Arrays.asList("energia", "corte", "servicios"),
                "EDELAP",
                "http://ejemplo.com/corte.jpg",
                "PROXY", true, 1
        );

        HechoAIntegrarDTO hecho15 = new HechoAIntegrarDTO(
                "Corte de energía en barrio céntrico",
                "Más de 20 manzanas quedaron sin luz durante 4 horas.",
                "Servicios",
                "-34.9214", "-57.9545",
                "2025-09-10",
                Arrays.asList("energia", "corte", "servicios"),
                "EDELAP",
                "http://ejemplo.com/corte.jpg",
                "PROXY", true, 1
        );

        HechoAIntegrarDTO hecho16 = new HechoAIntegrarDTO(
                "Corte de energía en barrio céntrico",
                "Más de 20 manzanas quedaron sin luz durante 4 horas.",
                "Servicios",
                "-34.9214", "-57.9545",
                "2025-09-10",
                Arrays.asList("energia", "corte", "servicios"),
                "EDELAP",
                "http://ejemplo.com/corte.jpg",
                "PROXY", true, 1
        );

        HechoAIntegrarDTO hecho17 = new HechoAIntegrarDTO(
                "Corte de energía en barrio céntrico",
                "Más de 20 manzanas quedaron sin luz durante 4 horas.",
                "Servicios",
                "-34.9214", "-57.9545",
                "2025-09-10",
                Arrays.asList("energia", "corte", "servicios"),
                "EDELAP",
                "http://ejemplo.com/corte.jpg",
                "PROXY", true, 2
        );

        HechoAIntegrarDTO hecho5 = new HechoAIntegrarDTO(
                "Nuevo mural en San Telmo",
                "Artistas locales pintaron un mural conmemorativo en la plaza Dorrego.",
                "Cultura",
                "-34.6212", "-58.3731",
                "2025-07-15",
                Arrays.asList("arte", "cultura", "mural"),
                "Municipalidad",
                "http://ejemplo.com/mural.jpg",
                "ESTATICA", false, 2
        );

        HechoAIntegrarDTO hecho6 = new HechoAIntegrarDTO(
                "Concierto gratuito en el Obelisco",
                "Miles de personas asistieron al show de bandas locales.",
                "Entretenimiento",
                "-34.6037", "-58.3816",
                "2025-09-11",
                Arrays.asList("musica", "evento", "obelisco"),
                "Canal 7",
                "http://ejemplo.com/concierto.mp4",
                "PROXY", true, 2
        );

        HechoAIntegrarDTO hecho7 = new HechoAIntegrarDTO(
                "Descubrimiento arqueológico en Jujuy",
                "Se hallaron restos de una antigua civilización precolombina.",
                "Historia",
                "-24.1858", "-65.2995",
                "2025-06-30",
                Arrays.asList("arqueologia", "historia", "descubrimiento"),
                "Universidad Nacional de Jujuy",
                "http://ejemplo.com/arqueologia.jpg",
                "ESTATICA", false, 2
        );

        HechoAIntegrarDTO hecho8 = new HechoAIntegrarDTO(
                "Torneo nacional de ajedrez",
                "Se celebró el campeonato argentino de ajedrez en Córdoba.",
                "Deportes",
                "-31.4201", "-64.1888",
                "2025-08-05",
                Arrays.asList("ajedrez", "torneo", "deportes"),
                "Federación Argentina de Ajedrez",
                "http://ejemplo.com/ajedrez.jpg",
                "PROXY", true, 2
        );

        HechoAIntegrarDTO hecho9 = new HechoAIntegrarDTO(
                "Nueva planta de reciclaje",
                "Se inauguró una planta de reciclaje de plásticos en Rosario.",
                "Medio Ambiente",
                "-32.9442", "-60.6505",
                "2025-09-02",
                Arrays.asList("reciclaje", "medioambiente", "plásticos"),
                "Gobierno Provincial",
                "http://ejemplo.com/reciclaje.jpg",
                "ESTATICA", true, 1
        );

        HechoAIntegrarDTO hecho10 = new HechoAIntegrarDTO(
                "Festival de cine independiente",
                "Directores emergentes presentaron sus películas en Mendoza.",
                "Cultura",
                "-32.8908", "-68.8272",
                "2025-08-25",
                Arrays.asList("cine", "festival", "independiente"),
                "Asociación de Cine Independiente",
                "http://ejemplo.com/cine.mp4",
                "PROXY", false, 1
        );

        HechoAIntegrarDTO hecho11 = new HechoAIntegrarDTO(
                "Maratón solidaria",
                "Más de 5000 corredores participaron para recaudar fondos.",
                "Deportes",
                "-34.9205", "-57.9536",
                "2025-09-08",
                Arrays.asList("maraton", "solidaria", "deportes"),
                "Fundación Esperanza",
                "http://ejemplo.com/maraton.jpg",
                "ESTATICA", true, 1
        );

        HechoAIntegrarDTO hecho12 = new HechoAIntegrarDTO(
                "Presentación de libro histórico",
                "Se presentó un libro sobre la historia del ferrocarril argentino.",
                "Cultura",
                "-34.6037", "-58.3816",
                "2025-09-04",
                Arrays.asList("libro", "historia", "ferrocarril"),
                "Editorial Nacional",
                "http://ejemplo.com/libro.jpg",
                "ESTATICA", false, 1
        );

        HechoAIntegrarDTO hecho13 = new HechoAIntegrarDTO(
                "Nueva línea de subte",
                "Se inauguró la línea F del subte en Buenos Aires.",
                "Transporte",
                "-34.6037", "-58.3816",
                "2025-08-29",
                Arrays.asList("subte", "transporte", "inauguracion"),
                "GCBA",
                "http://ejemplo.com/subte.jpg",
                "DINAMICA", true, 1
        );

        HechoAIntegrarDTO hecho14 = new HechoAIntegrarDTO(
                "Competencia internacional de robótica",
                "Equipos de todo el mundo participaron en la ciudad de Salta.",
                "Ciencia y Tecnología",
                "-24.7821", "-65.4232",
                "2025-09-09",
                Arrays.asList("robotica", "competencia", "tecnologia"),
                "Universidad de Salta",
                "http://ejemplo.com/robotica.mp4",
                "DINAMICA", true, 1
        );

        // Podés agruparlos en una lista si querés:
        List<HechoAIntegrarDTO> hechos = Arrays.asList(
                hecho1, hecho2, hecho3, hecho4, hecho5, hecho6, hecho7,
                hecho8, hecho9, hecho10, hecho11, hecho12, hecho13, hecho14, hecho15, hecho16, hecho17
        );

        System.out.println("**Hay " + hechosRepository.obtenerTodas().size() + " hechos en el repositorio**");
        servicioDeAgregacion.actualizarColecciones(hechos);

        System.out.println("Hechos colección:" + coleccionPrueba.getHechos().size() );
        Hecho random1 = hechosRepository.obtenerTodas().get(new Random().nextInt(hechosRepository.obtenerTodas().size()));
        System.out.println("Hecho random: " + random1.getTitulo()  + "- Tipo de Fuente: " + random1.getFuenteDeOrigen());
        Hecho random2 = hechosRepository.obtenerTodas().get(new Random().nextInt(hechosRepository.obtenerTodas().size()));
        System.out.println("Hecho random: " + random2.getTitulo()  + "- Tipo de Fuente: " + random2.getFuenteDeOrigen());
        Hecho random3 = hechosRepository.obtenerTodas().get(new Random().nextInt(hechosRepository.obtenerTodas().size()));
        System.out.println("Hecho random: " + random3.getTitulo()  + "- Tipo de Fuente: " + random3.getFuenteDeOrigen());
    }
}
