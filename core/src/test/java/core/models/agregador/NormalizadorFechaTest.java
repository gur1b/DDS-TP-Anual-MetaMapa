package core.models.agregador;


import core.models.agregador.normalizador.NormalizadorFecha;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

public class NormalizadorFechaTest {


    private final List<String> fechasDePrueba = List.of(
            // ISO y variantes obvias
            "2024-01-05",
            "2024/01/05",
            "2024-01-05T10:15:30Z",
            "2024-01-05T10:15:30+01:00",

            // Formatos claros D/M/Y vs M/D/Y
            "31/01/2024", // claro DMY
            "01/31/2024", // claro MDY
            "15/02/24",   // DMY 2
            "02/15/24",   // MDY 2

            // Casos ambiguos típicos
            "01/05/2024", // ¿1 mayo o 5 enero?
            "05/06/24",   // ambiguo
            "06/05/24",   // ambiguo
            "11/12/24",
            "12/11/24",

            // Cosas raras / inválidas
            "31/13/2020",
            "99/01/20",
            "2024-99-99",
            "texto cualquiera"
    );

    @Test
    void estadisticasNormalizarAIso() {
        int ok = 0;
        int ambiguas = 0;
        int invalidas = 0;

        System.out.println("-------Test normalizarAIso---------");
        for (String raw : fechasDePrueba) {
            try {
                String iso = NormalizadorFecha.normalizarAIso(raw);
                ok++;
                System.out.println("[OK]" + raw + " -> " + iso);
            } catch (NormalizadorFecha.ExcepcionFechaAmbigua e) {
                ambiguas++;
                System.out.println("[AMBIGUA]" + raw + " -> " + e.getMessage());
            } catch (Exception e) {
                invalidas++;
                System.out.println("[INVALIDA]" + raw + " -> " + e.getClass().getSimpleName() +
                        ": " + e.getMessage());
            }
        }

        System.out.println("------ Resumen normalizarAIso ----");
        System.out.println("Total:          " + fechasDePrueba.size());
        System.out.println("OK:             " + ok);
        System.out.println("Ambiguas:       " + ambiguas);
        System.out.println("Inválidas/otras:" + invalidas);
    }

    @Test
    void estadisticasNormalizarFechaConRevisionManual() {
        NormalizadorFecha normalizador = NormalizadorFecha.getInstance();

        int ok = 0;
        int revisionManual = 0;
        int errores = 0;

        System.out.println("-----------Test normalizarFecha-------------");
        for (String raw : fechasDePrueba) {
            try {
                LocalDate fecha = normalizador.normalizarFecha(raw);
                ok++;
                System.out.println("[OK]" + raw + " -> " + fecha);
            } catch (NormalizadorFecha.ExcepcionRevisionManualFecha e) {
                revisionManual++;
                System.out.println("[REVISION]" + raw + " -> " + e.getMessage());
            } catch (Exception e) {
                errores++;
                System.out.println("[ERROR]" + raw + " -> " + e.getClass().getSimpleName() +
                        ": " + e.getMessage());
            }
        }

        System.out.println("--- Resumen normalizarFecha ---");
        System.out.println("Total:" + fechasDePrueba.size());
        System.out.println("OK:" + ok);
        System.out.println("En revisión:" + revisionManual);
        System.out.println("Errores reales:" + errores);
    }
}

