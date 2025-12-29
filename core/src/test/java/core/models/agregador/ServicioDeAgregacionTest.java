package core.models.agregador;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServicioDeAgregacionTest {


    ServicioDeAgregacion servicioDeAgregacion = ServicioDeAgregacion.getInstance();

    @BeforeEach
    void setUp(){

    }

    List<HechoAIntegrarDTO> hechos = new ArrayList<>(List.of(
            new HechoAIntegrarDTO("Robo en Palermo", "Ingresaron a un local", "Robo", "-34.5803", "-58.4200", "2025-08-20"),
            new HechoAIntegrarDTO("  robo  en  palermo ", "ingresaron a un local", "Robo", "-34.5805", "-58.4200", "2025-08-20"),
            new HechoAIntegrarDTO("  robo  en  palermo ", "ingresaron a un local", "Robo", "-34.5805", "-58.4200", "2025-08-20"),

            new HechoAIntegrarDTO("Incendio en Almagro", "Sin heridos", "Incendio", "-34.6100", "-58.4200", "2025-08-19"),

            new HechoAIntegrarDTO("Choque en Caballito", "Dos autos", "Choque", "-34.6200", "-58.4400", "2025-08-18"),
            new HechoAIntegrarDTO("Choque en Caballito", "Dos autos", "Choque", "-34.6201", "-58.4400", "2025-08-18")
    ));

    List<HechoAIntegrarDTO> hechos2 = new ArrayList<>(List.of(
            new HechoAIntegrarDTO("Robo en Palermo", "Ingresaron a un local", "Robo", "-34.5800", "-58.4200", "2025-08-20"),

            new HechoAIntegrarDTO("Choque en Caballito", "Dos autos", "Choque", "-34.6201", "-58.4400", "2025-08-18")
    ));

    @Test
    void eliminoDuplicadosExitosamente(){
            servicioDeAgregacion.eliminarDuplicados(hechos);
            System.out.print(hechos);
            assertEquals(3, hechos.size());
    }


    @Test
    void NOeliminoDuplicadosExitosamente(){
        servicioDeAgregacion.eliminarDuplicados(hechos2);
        assertEquals(2, hechos2.size());
    }
}
