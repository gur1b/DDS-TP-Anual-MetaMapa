package core.models.entities.normalizador;

import core.models.entities.hecho.Categoria;
import core.models.agregador.HechoAIntegrarDTO;
import core.models.agregador.normalizador.NormalizadorCategoria;
import core.models.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NormalizadorCategoriaTest {
    NormalizadorCategoria normalizadorCategoria = NormalizadorCategoria.getInstance();
    CategoriaRepository categoriaRepository = CategoriaRepository.getInstance();

    @BeforeEach
    void setUp(){
    }

    @Test
    void estandarizaCategoriaCuandoSonElMismoHechoYDistintaCategoria() {
        HechoAIntegrarDTO h1 = new HechoAIntegrarDTO("Choque Av X", "...", "choque", "-34.6", "-58.4", "2025-08-20");
        HechoAIntegrarDTO h2 = new HechoAIntegrarDTO("Choque Av X", "...", "accidente", "-34.6001", "-58.4", "2025-08-20");

        List<HechoAIntegrarDTO> lista = Arrays.asList(h1, h2);
        normalizadorCategoria.estandarizarCategoriasDuplicadas(lista);


        assertEquals("choque", h2.getCategoria(), "Debe copiar la categoría del primero al segundo");
        assertEquals("choque", h1.getCategoria());
    }

    @Test
    void noCambiaCategoriaCuandoNoSonElMismoHecho() {
        HechoAIntegrarDTO h1 = new HechoAIntegrarDTO("Choque Av X", "...", "choque", "-34.6", "-58.4", "2025-08-20");
        HechoAIntegrarDTO h2 = new HechoAIntegrarDTO("Robo", "...", "robo", "-34.60", "-58.41", "2025-08-20");

        normalizadorCategoria.estandarizarCategoriasDuplicadas(Arrays.asList(h1, h2));

        assertEquals("robo", h2.getCategoria(), "Si no son el mismo hecho, no debe tocar la categoría");
        assertEquals("choque", h1.getCategoria(), "Si no son el mismo hecho, no debe tocar la categoría");
    }

    @Test
    void estandarizaMultiplesCoincidenciasEnCascada() {
        HechoAIntegrarDTO h1 = new HechoAIntegrarDTO("Choque", "...", "choque", "-34.6", "-58.4", "2025-08-20");
        HechoAIntegrarDTO h2 = new HechoAIntegrarDTO("Choque", "...", "accidente", "-34.6001", "-58.4", "2025-08-20");
        HechoAIntegrarDTO h3 = new HechoAIntegrarDTO("Choque", "...", "siniestralidad", "-34.6002", "-58.4", "2025-08-20");

        List<HechoAIntegrarDTO> lista = Arrays.asList(h1, h2, h3);
        normalizadorCategoria.estandarizarCategoriasDuplicadas(lista);

        assertEquals("choque", h2.getCategoria());
        assertEquals("choque", h3.getCategoria());
    }

    @Test
    void testCategoriaYaExiste() {
        Categoria RoboExistente = new Categoria("robo");
        categoriaRepository.add(RoboExistente);
        Categoria resultado = normalizadorCategoria.obtenerCategoria("robo");

        assertEquals(RoboExistente, resultado);
        System.out.println("Categoria Existente: " + RoboExistente.getNombre());
        System.out.println("Resultado testCategoriaElegida: " + resultado.getNombre());

    }


    @Test
    void testCategoriaExisteEnSingular() {
        Categoria ChoqueExistente = new Categoria("choque");
        categoriaRepository.add(ChoqueExistente);

        Categoria resultado = normalizadorCategoria.obtenerCategoria("choques");
        assertEquals(ChoqueExistente, resultado);
        System.out.println("Categoria Existente: " + ChoqueExistente.getNombre());
        System.out.println("Resultado testCategoriaElegida: " + resultado.getNombre());
    }

    @Test
    void testCategoriaExisteEnPlural() {
        Categoria IncendioExistente = new Categoria("incendios");
        categoriaRepository.add(IncendioExistente);

        Categoria resultado = normalizadorCategoria.obtenerCategoria("incendio");
        assertEquals(IncendioExistente, resultado);
        System.out.println("Categoria Existente: " + IncendioExistente.getNombre());
        System.out.println("Resultado testCategoriaElegida: " + resultado.getNombre());
    }


}

