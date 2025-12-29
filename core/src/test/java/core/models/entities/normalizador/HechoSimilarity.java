package core.models.entities.normalizador;

import core.models.agregador.HechoAIntegrarDTO;
import core.models.agregador.normalizador.ComparadorHechos;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public final class HechoSimilarity {

    ComparadorHechos comparadorHechos= ComparadorHechos.getInstance();

    @Test
    void testHechosIdenticos() {
        HechoAIntegrarDTO h1 = new HechoAIntegrarDTO(
                "Robo en casa",
                "Robo con fuerza en vivienda",
                "Robo",
                "-34.6037", "-58.3816",
                "2023-05-15");

        HechoAIntegrarDTO h2 = new HechoAIntegrarDTO(
                "Robo en casa",
                "Robo con fuerza en vivienda",
                "Robo",
                "-34.6037", "-58.3816",
                "2023-05-15");

        Boolean resultado = comparadorHechos.esElMismoHecho(h1, h2);
        double similitud = comparadorHechos.similitudDeHechos(h1,h2);
        System.out.println("Resultado identico: " + similitud);
        assertTrue(resultado);
    }

    @Test
    void testHechosSimilares() {
        HechoAIntegrarDTO h1 = new HechoAIntegrarDTO(
                "Robo en casa",
                "Robo con fuerza en vivienda",
                "Robo",
                "-34.6037", "-58.3816",
                "2023-05-15");

        HechoAIntegrarDTO h2 = new HechoAIntegrarDTO(
                "Robo domicilio",
                "Hurto en vivienda con fuerza",
                "Robo",
                "-34.6040", "-58.3820",
                "2023-05-16");

        Boolean resultado = comparadorHechos.esElMismoHecho(h1, h2);
        double similitud = comparadorHechos.similitudDeHechos(h1,h2);
        System.out.println("Resultado similar: " + similitud);
        assertFalse(resultado);
    }

    @Test
    void testHechosDiferentes() {
        HechoAIntegrarDTO h1 = new HechoAIntegrarDTO(
                "Robo en casa",
                "Robo con fuerza en vivienda",
                "Robo",
                "-34.6037", "-58.3816",
                "2023-05-15");

        HechoAIntegrarDTO h2 = new HechoAIntegrarDTO(
                "Accidente de tránsito",
                "Choque entre dos vehículos",
                "Accidente",
                "-34.6137", "-58.4816",
                "2023-06-20");

        Boolean resultado = comparadorHechos.esElMismoHecho(h1, h2);
        double similitud = comparadorHechos.similitudDeHechos(h1,h2);
        System.out.println("Resultado diferente: " + similitud);
        assertFalse(resultado);
    }
}
