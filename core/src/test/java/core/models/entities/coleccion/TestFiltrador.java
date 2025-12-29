package core.models.entities.coleccion;

import core.models.entities.colecciones.Coleccion;
import core.models.entities.colecciones.criterios.*;
import core.models.entities.fuentes.Fuente;
import core.models.entities.fuentes.TipoFuente;
import core.models.entities.hecho.Categoria;
import core.models.entities.hecho.Coordenadas;
import core.models.entities.hecho.Estado;
import core.models.entities.hecho.Hecho;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestFiltrador {
    //chequar qeu el filtro solo devuelve, no cambia la colección.
    final FiltradorColecciones filtradorColecciones = FiltradorColecciones.getInstance();

    Coordenadas coordenadas1 = new Coordenadas(123.0, 456.0);
    Coordenadas coordenadas2 = new Coordenadas(893.0, 016.0);
    Coordenadas coordenadas3 = new Coordenadas(973.0, 656.0);
    Coordenadas coordenadas4 = new Coordenadas(223.0, 033.0);

    Categoria categoriaIncendio = new Categoria("Incendio");
    Categoria categoriaChoque = new Categoria("Choque");
    Categoria categoriaRobo = new Categoria("Robo");

    List<Hecho> hechosVisibles1= new ArrayList<>();

    List<Hecho> hechos1 = new ArrayList<>();

    @Mock
    private Fuente fuente;

    Coleccion coleccion1 = new Coleccion(1, "Todos", "Todos los hechos que existen", null, Collections.singletonList(fuente), hechos1,hechosVisibles1, null);


    Hecho hecho1 = new Hecho(1, coordenadas1, categoriaIncendio, null,
            null, null, Estado.ACEPTADO, null,
            LocalDate.now().minusDays(2), LocalDate.now().minusDays(3),
            TipoFuente.ESTATICA, null, "No hubo heridos, el perro salto por la ventana, fue por una sartén", "Incendio en casa", null,"12", LocalTime.now());
    Hecho hecho2 = new Hecho(2, coordenadas2, categoriaChoque, null,
            null, null, Estado.ACEPTADO, null,
            LocalDate.now().minusDays(2), LocalDate.now().minusDays(3),
            TipoFuente.ESTATICA, null, "Un perro cruzo por la calle y frenó de golpe, todos a salvo.", "Choque entre moto y gol", null,"12", LocalTime.now());
    Hecho hecho3 = new Hecho(3, coordenadas3, categoriaIncendio, null,
            null, null, Estado.ACEPTADO, null,
            LocalDate.now().minusDays(1), LocalDate.now().minusDays(2),
            TipoFuente.ESTATICA, null, "Causa desconocida", "Departamento en un edicio", null,"12", LocalTime.now());
    Hecho hecho4 = new Hecho(4, coordenadas4, categoriaChoque, null,
            null, null, Estado.ACEPTADO, null,
            LocalDate.now().minusDays(1), LocalDate.now().minusDays(2),
            TipoFuente.ESTATICA, null, "Parecía que el conductor iba borracho, se llevó puesto una maceta que estaba en la calle", "Choque con maceta", null,"12", LocalTime.now());
    Hecho hecho5 = new Hecho(5, coordenadas1, categoriaRobo, null,
            null, null, Estado.ACEPTADO, null,
            LocalDate.now().minusDays(5), LocalDate.now().minusDays(6),
            TipoFuente.ESTATICA, null, "Se robó unas manzanas y bolsas", "Hurto en una verdulería", null,"12", LocalTime.now());

    void inicializarColeccion (){
                coleccion1.agregarHecho(hecho1);
                coleccion1.agregarHecho(hecho3);
                coleccion1.agregarHecho(hecho4);
                coleccion1.agregarHecho(hecho2);
                coleccion1.agregarHecho(hecho5);}


    @BeforeEach
    void setUp() {
        inicializarColeccion();
    }

    @Test//filtre por perro, con categoria incendio -> 1
    void TestFiltroPorDescripcionYCategoria() {
        Categoria categoria = new Categoria("Incendio");
        CriterioDescripcion filtroPerro = new CriterioDescripcion("perro");
        CriterioCategoria filtroCategoria = new CriterioCategoria(categoria);

        List<Criterio> criterios = Arrays.asList(filtroPerro, filtroCategoria);

        List<Hecho> hechosFiltrados = filtradorColecciones.filtrarColeccion(coleccion1, criterios);

        assertEquals( 1, hechosFiltrados.size());
        assertEquals(5, coleccion1.getHechos().size());
    }
    
    @Test
        //filtre por descripcion perro
    void TestFiltroPorDescripcionYFecha() {
        CriterioDescripcion filtroPerro = new CriterioDescripcion("perro");

        List<Criterio> criterios = Arrays.asList(filtroPerro);
        List<Hecho> hechosFiltrados = filtradorColecciones.filtrarColeccion(coleccion1, criterios);

        assertEquals( 2, hechosFiltrados.size());
        assertEquals(5, coleccion1.getHechos().size());
    }

    //filtra por perro, que haya paso, que paso hace 2 dias -> 2
    @Test
    void TestFiltroPorDescripcionYFechaHace2Dias() {
        CriterioDescripcion filtroPerro = new CriterioDescripcion("perro");
        CriterioFechaSuceso criterioFechaSuceso = new CriterioFechaSuceso(LocalDate.now().minusDays(4), LocalDate.now());

        List<Criterio> criterios = Arrays.asList(filtroPerro, criterioFechaSuceso);
        List<Hecho> hechosFiltrados = filtradorColecciones.filtrarColeccion(coleccion1, criterios);

        assertEquals(2, hechosFiltrados.size());
        assertEquals(5, coleccion1.getHechos().size());
    }


    @Test
    void TestFiltroEntreDias() {
        CriterioFechaSuceso criterioFechaSuceso = new CriterioFechaSuceso(LocalDate.now().minusDays(5),  LocalDate.now());

        List<Criterio> criterios = Arrays.asList(criterioFechaSuceso);
        List<Hecho> hechosFiltrados = filtradorColecciones.filtrarColeccion(coleccion1, criterios);

        assertEquals(4, hechosFiltrados.size());
        assertEquals(5, coleccion1.getHechos().size());
    }


    @Test
    void TestFiltroPorUbicacion() {
        CriterioUbicacion criterioUbicacion = new CriterioUbicacion(coordenadas1);
        CriterioCategoria criterioCategoria = new CriterioCategoria(categoriaIncendio);

        List<Criterio> criterios = Arrays.asList(criterioUbicacion, criterioCategoria);

        List<Hecho> hechosFiltrados = filtradorColecciones.filtrarColeccion(coleccion1, criterios);

        assertEquals(1, hechosFiltrados.size());
        assertEquals(5, coleccion1.getHechos().size());
    }

    @Test
    void TestCriterioVacio() {
        List<Hecho> hechosFiltrados = filtradorColecciones.filtrarColeccion(coleccion1, null);

        assertEquals(5, hechosFiltrados.size());
        assertEquals(5, coleccion1.getHechos().size());
    }

}
