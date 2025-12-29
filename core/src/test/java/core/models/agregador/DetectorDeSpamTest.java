package core.models.agregador;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DetectorDeSpamTest {

    DetectorDeSpam detector = DetectorDeSpam.getInstance();

    @BeforeEach
    void setUp(){
    }

    @Test
    void textoMuyCortoDebeSerSpam() {
        String textoCorto = "ok"; // length = 2

        boolean esSpam = detector.esSpam(textoCorto);

        assertTrue(esSpam, "Un texto muy corto es considerado spam");
    }

    @Test
    void textoLargoLegitimoSinPalabrasSospechosasNoEsSpam() {
        String texto = "La información publicada es incorrecta y puede afectar a mi integridad y a la de mi familia." +
                "No tiene detalle suficiente. Tiene datos que no autoricé" ;


        boolean esSpam = detector.esSpam(texto);

        assertFalse(esSpam, "Un texto normal. NO debería ser spam");
    }

    @Test
    void textoClasicoDeSpamDebeSerDetectadoComoSpam() {
        String textoSpam = "OFERTA EXCLUSIVA!!! Ganá dinero rápido desde tu casa, hacé CLICK en " +
                "este link http://superpromo123.com y obtené BONOS GRATIS. ";

        boolean esSpam = detector.esSpam(textoSpam);

        assertTrue(esSpam, "Spam clarísimo. Venta fake.");
    }

    @Test
    void textoLegitimoConAlgunaPalabraSospechosaNoDebeSerSpam() {
        String texto = "La publicación no se refiere a " +
                "una oferta comercial sino a un hecho real en el que fui víctima de un error. " +
                "En ningún momento yo acepté que se compartiera esta información así de gratis y considero que " +
                "la exposición pública puede perjudicar mi situación laboral y personal. " ;

        boolean esSpam = detector.esSpam(texto);

        assertFalse(esSpam, "Tiene palabras sospechosas del umbral pero claramente no es spam");
    }
}
