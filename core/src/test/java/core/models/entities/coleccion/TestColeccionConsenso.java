package core.models.entities.coleccion;


import java.net.MalformedURLException;

import core.models.entities.colecciones.Coleccion;
import core.models.entities.colecciones.ModoDeNavegacion;
import core.models.entities.colecciones.StrategyAbsoluta;

import core.models.entities.colecciones.criterios.Criterio;
import core.models.entities.fuentes.Fuente;
import core.models.entities.fuentes.FuenteFactory;
import core.models.entities.fuentes.TipoConexion;
import core.models.entities.fuentes.TipoFuente;
import core.models.entities.hecho.Categoria;
import core.models.entities.hecho.Coordenadas;
import core.models.entities.hecho.Estado;
import core.models.entities.hecho.Hecho;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestColeccionConsenso {
    /*private Coleccion coleccion;
    private List<Fuente> fuentes;
    private List<Hecho> hechos;
    private List<Hecho> hechosVisibles;
    private List<Criterio> criterios;

    // Coordenadas para pruebas
    private Coordenadas coordenadas1 = new Coordenadas(123.0, 456.0);
    private Coordenadas coordenadas2 = new Coordenadas(893.0, 016.0);
    private Coordenadas coordenadas3 = new Coordenadas(973.0, 656.0);

    // Categorías para pruebas
    private Categoria categoriaIncendio = new Categoria("Incendio");
    private Categoria categoriaChoque = new Categoria("Choque");

    @BeforeEach
    void setUp() throws MalformedURLException {
        // Inicializar listas
        fuentes = new ArrayList<>();
        hechos = new ArrayList<>();
        hechosVisibles = new ArrayList<>();
        criterios = new ArrayList<>();

        // Crear fuentes con diferentes tipos de conexión
        Fuente fuente1 = FuenteFactory.crearFuente( "Fuente Estática 1","https://684b1942165d05c5d35b843b.mockapi.io/metamapa/hechos", TipoFuente.ESTATICA, TipoConexion.APIREST);
        Fuente fuente2 = FuenteFactory.crearFuente( "Fuente Automática 1", "https://684b1942165d05c5d35b843b.mockapi.io/metamapa/hechos", TipoFuente.DINAMICA, TipoConexion.APIREST);
        Fuente fuente3 = FuenteFactory.crearFuente( "Fuente Estática 2", "https://684b1942165d05c5d35b843b.mockapi.io/metamapa/hechos", TipoFuente.PROXY, TipoConexion.APIREST);
        fuentes.addAll(Arrays.asList(fuente1, fuente2, fuente3));


        // Crear hechos para cada fuente
        // Hechos similares reportados por diferentes fuentes
        Hecho hecho1Fuente1 = new Hecho(1, coordenadas1, categoriaIncendio, null,
                LocalDate.now(), null, Estado.ACEPTADO, null,
                LocalDate.now(), LocalDate.now().minusDays(1),
                TipoFuente.ESTATICA, null,
                "Incendio grave en zona residencial", "Incendio en Barrio Norte", fuente1.getCodigoDeFuente(),"12");

        Hecho hecho1Fuente2 = new Hecho(2, coordenadas1, categoriaIncendio, null,
                LocalDate.now(), null, Estado.ACEPTADO, null,
                LocalDate.now(), LocalDate.now().minusDays(1),
                TipoFuente.DINAMICA, null,
                "Fuego detectado en área residencial", "Incendio en Barrio Norte", fuente1.getCodigoDeFuente(),"12");

        Hecho hecho1Fuente3 = new Hecho(3, coordenadas1, categoriaIncendio, null,
                LocalDate.now(), null, Estado.ACEPTADO, null,
                LocalDate.now(), LocalDate.now().minusDays(1),
                TipoFuente.ESTATICA, null,
                "Se reporta incendio en vivienda", "Incendio en Barrio Norte", fuente2.getCodigoDeFuente(),"12");

        // Hechos diferentes
        Hecho hecho2 = new Hecho(4, coordenadas2, categoriaChoque, null,
                LocalDate.now(), null, Estado.ACEPTADO, null,
                LocalDate.now(), LocalDate.now().minusDays(2),
                TipoFuente.ESTATICA, null,
                "Colisión entre dos vehículos", "Accidente en Avenida Principal", fuente2.getCodigoDeFuente(),"12");

        Hecho hecho3 = new Hecho(5, coordenadas3, categoriaIncendio, null,
                LocalDate.now(), null, Estado.ACEPTADO, null,
                LocalDate.now(), LocalDate.now().minusDays(3),
                TipoFuente.PROXY, null,
                "Incendio forestal detectado", "Incendio en Reserva Natural", fuente2.getCodigoDeFuente(), "12");

        hechos.addAll(Arrays.asList(hecho1Fuente1, hecho1Fuente2, hecho1Fuente3, hecho2, hecho3));

        // Crear colección
        coleccion = new Coleccion(1, "Colección de Prueba",
                "Colección para pruebas de consenso",
                 criterios,fuentes, hechos, hechosVisibles, "test-handle");

        // Configurar modo de navegación y algoritmo de consenso
        coleccion.modoDeNavegacion = ModoDeNavegacion.CURADA;
        coleccion.algoritmoConsenso = new StrategyAbsoluta(); // Asumiendo que existe esta clase
    }

    /*

    hechos iguales
    hechos similares
    que si el algortimo es null te devuelven todos los hechos
    si no es curada te devuelve todos los hechos
    Se ejecute bien el algortimo correspondiente
    Que no se ejecute si es una hora fuera de las de baja carga

    */
/*
    @Test
    void testHechosIguales() {
        // Crear dos hechos idénticos
        Hecho hecho1 = new Hecho(1, coordenadas1, categoriaIncendio, null,
                LocalDate.now(), null, Estado.ACEPTADO, null,
                LocalDate.now(), LocalDate.now(),
                TipoFuente.ESTATICA, null,
                "Incendio graBe", "InSendio en Barrio Norte", "c1", "12345");

        Hecho hecho2 = new Hecho(2, coordenadas1, categoriaIncendio, null,
                LocalDate.now(), null, Estado.ACEPTADO, null,
                LocalDate.now(), LocalDate.now(),
                TipoFuente.ESTATICA, null,
                "Incendio grave", "Incendio en Barrio Norte", "c1", "1234");

        List<Hecho> hechos = Arrays.asList(hecho1, hecho2);

        List<Hecho> hechosIguales = coleccion.algoritmoConsenso.obtenerHechosIguales(hecho1,hechos);

        assertEquals(2, (hechosIguales).size());
    }

    @Test
    void testHechosSimilares() {
        // Crear dos hechos similares pero no idénticos
        LocalDate fechaHoy = LocalDate.now();
        LocalDate fechaAyer = fechaHoy.minusDays(1);

        Hecho hecho1 = new Hecho(1, coordenadas1, categoriaIncendio, null,
                fechaHoy, null, Estado.ACEPTADO, null,
                fechaHoy, fechaHoy,
                TipoFuente.ESTATICA, null,
                "Incendio grave", "Incendio en Barrio Norte", "C1","12");

        Hecho hecho2 = new Hecho(1, coordenadas1, categoriaIncendio, null,
                fechaHoy, null, Estado.ACEPTADO, null,
                fechaHoy, fechaHoy,
                TipoFuente.ESTATICA, null,
                " grabe", " Barrio Oeste", "C10","13");

        System.out.println(coleccion.algoritmoConsenso.similitudHechos(hecho1,hecho2));


        assertTrue(coleccion.algoritmoConsenso.esHechoSimilar(hecho1, hecho2));
    }

    @Test
    void testAlgoritmoNull() {
        // Configurar algoritmo como null
        coleccion.algoritmoConsenso = null;
        coleccion.modoDeNavegacion = ModoDeNavegacion.CURADA;

        // Actualizar colección
        coleccion.actualizarColeccionVisible();

        // Verificar que devuelve todos los hechos
        assertEquals(hechos.size(), coleccion.hechosVisibles.size());
        assertTrue(coleccion.hechosVisibles.containsAll(hechos));
    }

    @Test
    void testModoNoCurado() {
        // Configurar modo no curado
        coleccion.modoDeNavegacion = ModoDeNavegacion.IRRESTRICTO;

        // Actualizar colección
        coleccion.actualizarColeccionVisible();

        // Verificar que devuelve todos los hechos
        assertEquals(hechos.size(), coleccion.hechosVisibles.size());
        assertTrue(coleccion.hechosVisibles.containsAll(hechos));
    }

    @Test
    void testEjecucionAlgoritmo() {
        // Configurar algoritmo y modo
        coleccion.algoritmoConsenso = new StrategyAbsoluta();
        coleccion.modoDeNavegacion = ModoDeNavegacion.CURADA;

        // Actualizar colección
        coleccion.actualizarColeccionVisible();

        // Verificar que el algoritmo filtró los hechos
        assertTrue(coleccion.hechosVisibles.size() < hechos.size());
    }*/
}