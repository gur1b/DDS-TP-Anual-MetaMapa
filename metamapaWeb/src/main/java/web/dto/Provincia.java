package web.dto;

import java.text.Normalizer;

public enum Provincia {
    BUENOS_AIRES("Buenos Aires"),
    CIUDAD_AUTONOMA_DE_BUENOS_AIRES("Ciudad Autónoma de Buenos Aires"),
    CATAMARCA("Catamarca"),
    CHACO("Chaco"),
    CHUBUT("Chubut"),
    CORDOBA("Córdoba"),
    CORRIENTES("Corrientes"),
    ENTRE_RIOS("Entre Ríos"),
    FORMOSA("Formosa"),
    JUJUY("Jujuy"),
    LA_PAMPA("La Pampa"),
    LA_RIOJA("La Rioja"),
    MENDOZA("Mendoza"),
    MISIONES("Misiones"),
    NEUQUEN("Neuquén"),
    RIO_NEGRO("Río Negro"),
    SALTA("Salta"),
    SAN_JUAN("San Juan"),
    SAN_LUIS("San Luis"),
    SANTA_CRUZ("Santa Cruz"),
    SANTA_FE("Santa Fe"),
    SANTIAGO_DEL_ESTERO("Santiago del Estero"),
    TIERRA_DEL_FUEGO("Tierra del Fuego"),
    TUCUMAN("Tucumán");

    private final String nombre;

    Provincia(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public static Provincia fromString(String provincia) {
        if (provincia == null) return null;
        // Normalizar el string: quita acentos, pasa a mayúsculas, reemplaza espacios y guiones
        String normalizada = Normalizer.normalize(provincia, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                .replace(" ", "_")
                .replaceAll("-", "_")
                .toUpperCase();
        // Mapeo especial para valores atípicos
        if (normalizada.equals("CABA")) normalizada = "CIUDAD_AUTONOMA_DE_BUENOS_AIRES";
        for (Provincia p : Provincia.values()) {
            if (p.name().equals(normalizada)) {
                return p;
            }
        }
        return null;
    }

}
