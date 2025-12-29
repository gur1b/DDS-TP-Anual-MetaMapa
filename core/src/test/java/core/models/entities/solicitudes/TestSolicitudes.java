package core.models.entities.solicitudes;

import core.models.entities.fuentes.TipoFuente;
import core.models.entities.hecho.Categoria;
import core.models.entities.hecho.Coordenadas;
import core.models.entities.hecho.Estado;
import core.models.entities.hecho.Hecho;
import core.models.entities.solicitud.SolicitudDeEliminacion;
import core.models.repository.HechosRepository;
import core.models.repository.seeders.HechosRepositorySeeder;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestSolicitudes {

    Coordenadas coordenadas1 = new Coordenadas(123.0, 456.0);

    Categoria categoriaIncendio = new Categoria("Incendio");


    Hecho hecho1 = new Hecho(1, coordenadas1, categoriaIncendio,
            null, null, null, Estado.ACEPTADO, null,
            LocalDate.now().minusDays(2), LocalDate.now().minusDays(3),
            TipoFuente.ESTATICA, null, "No hubo heridos, fue por una sartén", "incendio en Casa", null,"12", LocalTime.now());
    HechosRepository hechosRepository = HechosRepository.getInstance();
    HechosRepositorySeeder hechosRepositorySeeder = HechosRepositorySeeder.getInstance();

    SolicitudDeEliminacion soli1 = new SolicitudDeEliminacion(1, hecho1, "aaawdkjas", null, null);

    @Test
    void seRechaza(){
        //soli1.revisarPorSpam();

        assertFalse(soli1.getAceptada());
    }


    @Test
    void seAcepta(){
        soli1.aceptarSolicitud();

        assertTrue(soli1.getAceptada());
    }
}
