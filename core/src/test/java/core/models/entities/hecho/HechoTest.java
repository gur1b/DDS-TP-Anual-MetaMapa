package core.models.entities.hecho;


import core.models.entities.hecho.Etiqueta;
import core.models.entities.hecho.Hecho;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class HechoTest {

    private Hecho hecho1;
    private Hecho hecho2;
    private List<Etiqueta> etiquetas;

    @BeforeEach
    void setUp(){

        this.etiquetas = new ArrayList<>();
        this.hecho1 = new Hecho(5, null, null,
                null,
                null, null,
                null, null,
                LocalDate.now().minusDays(5), LocalDate.now().minusDays(6),
                null,
                etiquetas, null,null, null,"12", LocalTime.now());
        this.hecho2 = new Hecho(5, null, null,
                null,
                null, null,
                null, null,
                LocalDate.now().minusDays(9), LocalDate.now().minusDays(6),
                null,
                etiquetas, null,null, null,"12", LocalTime.now());
    }

    @Test
    void testAgregarEtiqueta_DeberiaAgregarNuevaEtiqueta() {
        // Arrange
        Etiqueta nuevaEtiqueta = new Etiqueta("Importante");
        int tamañoInicial = hecho1.getEtiquetas().size();

        // Act
        hecho1.agregarEtiqueta(nuevaEtiqueta);

        // Assert
        assertEquals(tamañoInicial + 1, hecho1.getEtiquetas().size());
        assertTrue(hecho1.getEtiquetas().contains(nuevaEtiqueta));
    }

    @Test
    void testPasoUnaSemana_CuandoSucesoHaceMenosDe7Dias_DeberiaRetornarTrue() {

        // Act
        boolean resultado = hecho1.pasoUnaSemana();

        // Assert
        assertTrue(resultado, "Debería retornar true cuando el suceso fue hace menos de 7 días");
    }

    @Test
    void testPasoUnaSemana_CuandoSucesoHaceMasDe7Dias_DeberiaRetornarFalse() {
        // Arrange
        // Act
        boolean resultado = hecho2.pasoUnaSemana();

        // Assert
        assertTrue(resultado);
    }
}
