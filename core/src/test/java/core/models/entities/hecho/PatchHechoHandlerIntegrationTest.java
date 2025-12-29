package core.models.entities.hecho;

import core.models.entities.hecho.Coordenadas;
import core.models.entities.hecho.Hecho;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.DBUtils;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

public class PatchHechoHandlerIntegrationTest {
    private EntityManager em;
    private Hecho hecho;

    @Test
    public void testActualizarCoordenadasId1() {
        em = DBUtils.getEntityManager();
        DBUtils.comenzarTransaccion(em);
        Coordenadas coords = em.find(Coordenadas.class, 1);
        assertNotNull(coords, "No existe coordenadas con id_ubicacion 1");

        // Cambiar coordenadas a 1.0 y 1.0
        coords.setLatitud(1.0);
        coords.setLongitud(1.0);
        em.merge(coords);
        DBUtils.commit(em);

        // Verificar que se guardó
        em = DBUtils.getEntityManager();
        Coordenadas actualizado = em.find(Coordenadas.class, 1);
        assertNotNull(actualizado);
        assertEquals(1.0, actualizado.getLatitud());
        assertEquals(1.0, actualizado.getLongitud());
        em.close();
    }
}
