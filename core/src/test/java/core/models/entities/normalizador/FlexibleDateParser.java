package core.models.entities.normalizador;

import core.models.agregador.normalizador.NormalizadorFecha;

import java.time.LocalDate;

public class FlexibleDateParser {

    // Demo
    public static void main(String[] args) {
        LocalDate hoy = LocalDate.now();
        /*
        List<String> tests = new ArrayList<>();
        tests.add("2025/08/05"); // YMD
        tests.add("05/08/2025"); // DMY
        tests.add("08/05/2025"); // MDY
        tests.add("02/11/2025"); // ambiguo: DMY=2025-11-02 (futuro), MDY=2025-02-11 (pasado) → elegir MDY
        tests.add("03/02/2025"); // ambos pasados → revisión
        tests.add("12/12/2026"); // ambos futuros → revisión
        tests.add("5-8-25");     // 2 dígitos año (ambiguo; ambos pasados → revisión)
        */
        String[] tests = {
                "2025/08/05",
                "05/08/2025",
                "08/05/2025",
                "5-8-25",
                "13/02/25",
                "02/13/25",
                "2025-8-5",
                "31/04/2024" // inválida
        };


        for (String t : tests) {
            try {
                System.out.printf("%s -> %s%n", t, NormalizadorFecha.getInstance().normalizarFecha(t));
            } catch (NormalizadorFecha.ExcepcionFechaAmbigua e) {
                System.out.printf("%s -> REVISIÓN: %s%n", t, e.getMessage());
            } catch (Exception e) {
                System.out.printf("%s -> ERROR: %s%n", t, e.getMessage());
            }
        }
    }
}
