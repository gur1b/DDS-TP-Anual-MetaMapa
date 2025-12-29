package servicioEstadisticas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seeders.RepositoryServicioEstadisticasSeeder;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GeneradorTodasEstadisticasTest {

    private GeneradorTodasEstadisticas generadorTodasEstadisticas;
    private RepositoryServicioEstadisticasSeeder seeder;

    @BeforeEach
    void setUp() {

        generadorTodasEstadisticas = GeneradorTodasEstadisticas.getInstance();
        //Si usas el update en el persistence.xml entonces tenes que dejar comentado el seeder, pero si esta en create entonces hay que descomentarlo
        //seeder = RepositoryServicioEstadisticasSeeder.getInstance();
        //seeder.cargarHechos();
        generadorTodasEstadisticas.actualizarEstadisticas();
    }

    @Test
    void testCategoriaMasReportada() {
        List<Map<String, Object>> categorias = generadorTodasEstadisticas.getCategoriaMasReportada();
        assertNotNull(categorias);
        assertFalse(categorias.isEmpty());
        assertEquals("Incendio", categorias.get(0).get("categoria"));
    }

    @Test
    void testProvinciaConMasHechos() {
        List<Map<String, Object>> provincias = generadorTodasEstadisticas.getProvinciaConMasHechos();
        assertNotNull(provincias);
        assertFalse(provincias.isEmpty());
        assertEquals("Córdoba", provincias.get(0).get("provincia"));
    }

    @Test
    void testHorarioMasFrencuentePorCategoria() {
        List<Map<String, Object>> horarios = generadorTodasEstadisticas.horarioxCategoria("Robo");
        assertNotNull(horarios);
        assertFalse(horarios.isEmpty());
        assertTrue(horarios.get(0).get("hora").toString().contains("13:00"));
    }

    @Test
    void testProvinciaMasHechosPorCategoria() {
        List<Map<String, Object>> provincias = generadorTodasEstadisticas.provinciaConMasHechosEnCategoria("Amenaza");
        assertNotNull(provincias);
        assertFalse(provincias.isEmpty());
        assertEquals("Córdoba", provincias.get(0).get("provincia"));
    }

    @Test
    void testCantidadSolicitudesEliminacion() {
        Map<String, Object> estadisticas = generadorTodasEstadisticas.getCantSolicitudesEliminacion();
        assertNotNull(estadisticas);
        assertTrue(estadisticas.containsKey("solicitudes spam"));
        assertTrue(estadisticas.containsKey("total de solicitudes"));
        assertTrue((Long)estadisticas.get("solicitudes spam") >= 0);
        assertTrue((Long)estadisticas.get("total de solicitudes") >= 0);
    }



}
