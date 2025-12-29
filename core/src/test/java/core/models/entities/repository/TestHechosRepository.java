package core.models.entities.repository;

import core.models.entities.fuentes.TipoFuente;
import core.models.entities.hecho.Categoria;
import core.models.entities.hecho.Coordenadas;
import core.models.entities.hecho.Estado;
import core.models.entities.hecho.Hecho;
import core.models.repository.CategoriaRepository;
import core.models.repository.CoordenadasRepository;
import core.models.repository.HechosRepository;
import core.models.repository.seeders.HechosRepositorySeeder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestHechosRepository {

    HechosRepository hechosRepository = HechosRepository.getInstance();
    CategoriaRepository categoriaRepository = CategoriaRepository.getInstance();
    CoordenadasRepository coordenadasRepository = CoordenadasRepository.getInstance();
    HechosRepositorySeeder hechosRepositorySeeder = HechosRepositorySeeder.getInstance();

    Coordenadas coordenadas1 = new Coordenadas(-34.59864326986724, -58.42015420354632);
    Coordenadas coordenadas2 = new Coordenadas(-34.567764578495826, -58.41914572689126);

    Categoria categoriaIncendio = new Categoria("Incendio");


    Hecho hecho1 = new Hecho(
            1,
            coordenadas1,
            categoriaIncendio,
            null,
            null,
            null,
            Estado.ACEPTADO,
            null,
            LocalDate.now().minusDays(2),
            LocalDate.now().minusDays(3),
            TipoFuente.ESTATICA,
            null,
            "No hubo heridos, fue por una sartén",
            "incendio en Casa",
            null,
            "34",
            LocalTime.now());

    Hecho hecho2 = new Hecho(2, coordenadas2, categoriaIncendio,
            null, null, null, Estado.ACEPTADO, null,
            LocalDate.now().minusDays(2), LocalDate.now().minusDays(3),
            TipoFuente.ESTATICA, null, "No hubo heridos, fue por una sartén", "en casa", null, "59", LocalTime.now());



    @BeforeEach
    void SetUp(){

    }

    @Test
    void agregarARepos(){
        categoriaRepository.add(categoriaIncendio);
        coordenadasRepository.add(coordenadas1);
        hechosRepository.add(hecho1);
        hechosRepository.add(hecho2);
    }


    @Test
    void SIMatchDeTitulo(){
        hechosRepositorySeeder.cargarHechosSeeder();
        assertTrue(hechosRepository.esHechoDuplicado(hecho1));
    }


    @Test
    void NOMatchDeTitulo(){
        hechosRepositorySeeder.cargarHechosSeeder();

        assertFalse(hechosRepository.esHechoDuplicado(hecho2));
    }

    @Test
    void AgregarListaHechos(){
        List<Hecho> hechos = new ArrayList<>();
        hechos.add(hecho1);
        hechos.add(hecho2);
        hechosRepository.addAllEnUnaTransaccion(hechos);
    }
}
