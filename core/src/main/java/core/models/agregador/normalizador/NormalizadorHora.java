package core.models.agregador.normalizador;

import java.time.LocalTime;

public class NormalizadorHora {

    private static NormalizadorHora instance;

    public static NormalizadorHora getInstance() {
        if (instance == null) instance = new NormalizadorHora();

        return instance;
    }

    public LocalTime normalizarHora(String horaString) {
        if (horaString == null) return null;

        String s = horaString.trim();
        if (s.isEmpty()) return null;

        try {
            return LocalTime.parse(s); // HH:mm o HH:mm:ss
        } catch (Exception ignored) {
            // ejemplo tolerante: "H:mm"
            return LocalTime.parse(s, java.time.format.DateTimeFormatter.ofPattern("H:mm"));
        }
    }
}
