package core.models.repository.seeders;

import core.models.entities.colecciones.Coleccion;
import core.models.entities.colecciones.ModoDeNavegacion;
import core.models.entities.colecciones.TipoConsenso;
import core.models.entities.colecciones.criterios.*;
import core.models.entities.fuentes.Fuente;
import core.models.entities.hecho.Categoria;
import core.models.entities.hecho.Hecho;
import core.models.repository.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
public class ColeccionesRepositorySeeder {
    private static volatile ColeccionesRepositorySeeder instance;
    private final ColeccionesRepository coleccionesRepository;
    private final FuentesRepository fuentesRepository;
    private final CategoriaRepository categoriaRepository;

    private ColeccionesRepositorySeeder() {
        this.coleccionesRepository = ColeccionesRepository.getInstance();
        this.categoriaRepository = CategoriaRepository.getInstance();
        this.fuentesRepository = FuentesRepository.getInstance();
    }

    public static ColeccionesRepositorySeeder getInstance() {
        if (instance == null) {
            synchronized (ColeccionesRepositorySeeder.class) {
                if (instance == null) {
                    instance = new ColeccionesRepositorySeeder();
                }
            }
        }
        return instance;
    }

    public void cargarColeccionesRepositorySeeder() {
        /*
        // Obtener hechos (validar que existan)
        Hecho hecho1 = hechosRepository.findById(1);
        Hecho hecho2 = hechosRepository.findById(2);
        Hecho hecho3 = hechosRepository.findById(3);
        Hecho hecho4 = hechosRepository.findById(4);
        Hecho hecho5 = hechosRepository.findById(5);

        if (hecho1 == null || hecho2 == null || hecho3 == null || hecho4 == null || hecho5 == null) {
            throw new IllegalStateException("No se encontraron todos los hechos necesarios");
        }
        */

        // Obtener fuentes (validar que existan)
        Fuente fuente1 = fuentesRepository.findById(1);
        Fuente fuente2 = fuentesRepository.findById(2);
        Fuente fuente3 = fuentesRepository.findById(3);
        Fuente fuente4 = fuentesRepository.findById(4);

        if (fuente1 == null || fuente2 == null || fuente3 == null || fuente4 == null) {
            throw new IllegalStateException("No se encontraron todas las fuentes necesarias");
        }

        List<Fuente> fuentes1 = List.of(fuente1);
        List<Fuente> fuentes2 = List.of(fuente2, fuente3);
        List<Fuente> fuentes3 = List.of(fuente1, fuente2, fuente3, fuente4);

        // Crear colecciones
        CriteriosRepository criteriosRepository = CriteriosRepository.getInstance();
        List<Criterio> criteriosColec1 = new ArrayList<>();
        List<Criterio> criteriosColec2 = new ArrayList<>();
        List<Criterio> criteriosColec3 = new ArrayList<>();


        CriterioHoraSuceso criterioHoraNocturna = new CriterioHoraSuceso(LocalTime.of(18, 0), LocalTime.of(6,0));
        Categoria violencia = new Categoria("Violencia");
        categoriaRepository.add(violencia);
        CriterioCategoria criterioViolencia = new CriterioCategoria(violencia);
        Categoria robo = new Categoria("Robo");
        categoriaRepository.add(robo);
        CriterioCategoria criterioRobo = new CriterioCategoria(robo);
        Categoria estafa = new Categoria("Estafa");
        categoriaRepository.add(estafa);
        CriterioCategoria criterioEstafa = new CriterioCategoria(estafa);
        CriterioFechaSuceso criterioFecha = new CriterioFechaSuceso(LocalDate.of(2024,12,31), LocalDate.of(2026,1,1));

        criteriosRepository.add(criterioHoraNocturna);
        criteriosRepository.add(criterioViolencia);
        criteriosRepository.add(criterioRobo);
        criteriosRepository.add(criterioEstafa);
        criteriosRepository.add(criterioFecha);

        criteriosColec1.add(criterioHoraNocturna);
        criteriosColec1.add(criterioViolencia);
        criteriosColec2.add(criterioRobo);
        criteriosColec3.add(criterioEstafa);
        criteriosColec3.add(criterioFecha);


        Coleccion coleccion1 = new Coleccion(1, "Violencia Nocturna", "Hechos violentos ocurridos en horas de noche y madrugada", criteriosColec1, fuentes2, null,null, null);
        Coleccion coleccion2 = new Coleccion(2, "Robos", "Todos los robos", criteriosColec2, fuentes3, null,null, null);
        Coleccion coleccion3 = new Coleccion(3, "Estafas del año 2025", "Estafas llevadas a cabo durante el año 2025", criteriosColec3, fuentes3, null,null, null);

        // Guardar colecciones
        coleccionesRepository.add(coleccion1);
        coleccionesRepository.add(coleccion2);
        coleccionesRepository.add(coleccion3);
    }
}