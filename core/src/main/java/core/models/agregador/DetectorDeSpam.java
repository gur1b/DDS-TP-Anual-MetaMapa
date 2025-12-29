package core.models.agregador;

import core.models.entities.solicitud.SolicitudDeEliminacion;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetectorDeSpam {

    private static volatile DetectorDeSpam instance;

    public DetectorDeSpam() {
        if (instance != null) {
            throw new RuntimeException("Usa getInstance() para obtener el Singleton");
        }
    }


    public static DetectorDeSpam getInstance() {
        if (instance == null) {
            synchronized (DetectorDeSpam.class) {
                if (instance == null) {
                    instance = new DetectorDeSpam();
                }
            }
        }
        return instance;
    }

    // Palabras típicas de spam con un peso
    private static final Map<String, Double> PESOS_SPAM = new HashMap<>();

    //Umbral para decidir si es spam
    private static final double UMBRAL_SPAM = 3.0;

    static {
        // Publicidad "ambiguas" (pueden aparecer en un contexto legítimo)
        PESOS_SPAM.put("oferta", 0.7);
        PESOS_SPAM.put("promocion", 1.0);
        PESOS_SPAM.put("promo", 1.0);
        PESOS_SPAM.put("gratis", 0.7);

        // Publicidad / marketing típico, muy raro en una solicitud seria
        PESOS_SPAM.put("suscribite", 2.5);
        PESOS_SPAM.put("seguinos", 2.3);
        PESOS_SPAM.put("click", 2.0);
        PESOS_SPAM.put("comprar", 2.2);
        PESOS_SPAM.put("venta", 1.8);

        // Estafas / casinos / plata fácil: casi imposible que sean legítimas acá
        PESOS_SPAM.put("casino", 3.0);
        PESOS_SPAM.put("bono", 2.4);
        PESOS_SPAM.put("cripto", 2.8);
        PESOS_SPAM.put("inverti", 2.8);

        // Palabras irrelevantes / bots / texto basura
        PESOS_SPAM.put("asdf", 3.0);
        PESOS_SPAM.put("lorem", 2.5);
        PESOS_SPAM.put("ipsum", 2.5);
        PESOS_SPAM.put("prueba", 2.0);
        PESOS_SPAM.put("xxxx", 3.0);
        PESOS_SPAM.put("zzzz", 3.0);

        // URLs: fuertísimo indicador de spam en solicitudes de eliminación
        PESOS_SPAM.put("http", 3.5);
        PESOS_SPAM.put("https", 3.5);
        PESOS_SPAM.put("www", 3.5);
    }

    public  boolean esSpam(String texto) {
       if (contieneChocloSinEspacios(texto))
       {return true;}
       if (texto.length()<3)
       {return true;}

        if (texto == null || texto.isBlank()) {
            // si viene vacío,lo consideramos inválido / spam
            return true;
        }

        String normalizado = normalizar(texto);
        String[] palabras = normalizado.split("\\s+");

        if (contienePalabrasRaras(normalizado)) {
            return true;
        }

        // TF-IDF súper simplificado: sumo tf * peso
        double score = 0.0;

        for (String palabra : palabras) {
            Double peso = PESOS_SPAM.get(palabra);
            if (peso != null) {
                // cada aparición suma TF * IDF (acá TF = 1 por aparición)
                score += peso;
            }
        }
        return score >= UMBRAL_SPAM;
    }

    // Normaliza: minúsculas, sin acentos, solo letras/espacios
    private String normalizar(String texto) {
        String t = texto.toLowerCase(Locale.ROOT);

        t = Normalizer.normalize(t, Normalizer.Form.NFD);
        t = t.replaceAll("\\p{M}", ""); // elimina acentos

        // dejar solo letras, números básicos y espacios
        t = t.replaceAll("[^a-z0-9ñáéíóúü ]", " ");

        return t;
    }

    private boolean contieneChocloSinEspacios(String texto) {
        if (texto == null) return false;

        String limpio = texto.replace("\n", " ").replace("\r", " ").trim();

        int UMBRAL_CHOCLO = 30;

        String[] palabras = limpio.split("\\s+");

        for (String p : palabras) {
            if (p.length() > UMBRAL_CHOCLO) {
                return true;
            }
        }

        return false;
    }

    private boolean contienePalabrasRaras(String textoNormalizado) {
        if (textoNormalizado == null || textoNormalizado.isBlank()) return false;

        // dejamos solo letras y espacios
        String soloLetras = textoNormalizado.replaceAll("[^a-zñáéíóúü ]", " ");
        String[] palabras = soloLetras.split("\\s+");

        int raras = 0;

        for (String p : palabras) {
            if (p.length() >= 5) {
                int vocales = contarVocales(p);
                double ratioVocales = (double) vocales / p.length();

                // pocas vocales en palabras relativamente largas → pinta de teclado smash
                if (ratioVocales < 0.25) {
                    raras++;
                }
            }
        }

        //sensibilidad:
        // - 2 agresiva
        // - 3 permisiva
        return raras >= 2;
    }

    private int contarVocales(String palabra) {
        int count = 0;
        String vocales = "aeiouáéíóúü";
        for (int i = 0; i < palabra.length(); i++) {
            char c = palabra.charAt(i);
            if (vocales.indexOf(c) >= 0) {
                count++;
            }
        }
        return count;
    }
}
