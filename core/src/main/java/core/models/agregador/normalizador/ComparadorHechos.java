package core.models.agregador.normalizador;

import core.models.agregador.HechoAIntegrarDTO;
import core.models.agregador.ServicioDeAgregacion;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

public class ComparadorHechos {

    //SINGLETON
    private static volatile ComparadorHechos instance;

    private ComparadorHechos() {
        if (instance != null) throw new RuntimeException("Usa getInstance() para obtener el Singleton");
    }

    public static ComparadorHechos getInstance() {
        if (instance == null) {
            synchronized (ServicioDeAgregacion.class) {
                if (instance == null) instance = new ComparadorHechos();
            }
        }
        return instance;
    }

    //CONFIGURABLES
    //Pesos (pueden no sumar 1)
    private double wTitulo = 0.15;
    private double wDescripcion = 0.15;
    private double wCategoria = 0.05;
    private double wUbicacion = 0.45;
    private double wFecha = 0.20;

    //Umbral global
    private double threshold = 0.80;

    //Máxima distancia geográfica (m) para similitud lineal ubicación
    private double maxMeters = 500.0;

    //Máxima diferencia temporal para similitud lineal fecha
    private int maxDays = 2;

    //Stopwords para tokenizar
    private Set<String> stopwords = new HashSet<>(Arrays.asList(
            "el","la","los","las","de","del","y","o","u","en","a","un","una",
            "para","por","con","que","se","su","sus","al","lo","es","son",
            "esta","este","estas","estos"
    ));

    //Parser de fecha: String -> LocalDate (null si falla). Por defecto ISO yyyy-MM-dd.
    private Function<String, LocalDate> dateParser = s -> {
        try { return (s == null || s.trim().isEmpty()) ? null
                : LocalDate.parse(s.trim(), DateTimeFormatter.ISO_LOCAL_DATE); }
        catch (Exception e) { return null; }
    };

    //Configuración
    public double getThreshold() { return threshold; }
    public void setThreshold(double threshold) { this.threshold = threshold; }

    public void setPesos(double wTitulo, double wDescripcion, double wCategoria,
                         double wUbicacion, double wFecha) {
        this.wTitulo = wTitulo; this.wDescripcion = wDescripcion; this.wCategoria = wCategoria;
        this.wUbicacion = wUbicacion; this.wFecha = wFecha;
    }
    public void setMaxMeters(double maxMeters) { this.maxMeters = maxMeters; }
    public void setMaxDays(int maxDays) { this.maxDays = maxDays; }
    public void setStopwords(Set<String> stopwords) { this.stopwords = (stopwords == null)? new HashSet<>() : stopwords; }
    public void setDateParser(Function<String, LocalDate> dateParser) { this.dateParser = dateParser; }

    //Devuelve true si la simulitud es mayor o igual al threshold actual
    public boolean esElMismoHecho(HechoAIntegrarDTO a, HechoAIntegrarDTO b) {
        return similitudDeHechos(a, b) >= this.threshold;
    }

    public boolean hechoDuplicado(HechoAIntegrarDTO a, HechoAIntegrarDTO b)
    {
        return similitudDeHechos(a, b) >= 0.95;
    }

    //Devuelve el score
    public double similitudDeHechos(HechoAIntegrarDTO a, HechoAIntegrarDTO b) {
        return similitudDeHechos(a, b, null);
    }

    //puntajes especificos
    public double similitudDeHechos(HechoAIntegrarDTO a, HechoAIntegrarDTO b, Map<String, Double> componentScoresOut) {
        Objects.requireNonNull(a); Objects.requireNonNull(b);

        Map<String, Double> parts = new LinkedHashMap<>();
        List<Double> weights = new ArrayList<>();
        List<Double> scores  = new ArrayList<>();

        // Título
        Double sTitulo = textJaccard(a.getTitulo(), b.getTitulo());
        if (sTitulo != null) add("titulo", sTitulo, wTitulo, parts, scores, weights);

        // Descripción
        Double sDesc = textJaccard(a.getDescripcion(), b.getDescripcion());
        if (sDesc != null) add("descripcion", sDesc, wDescripcion, parts, scores, weights);

        // Categoría
        Double sCat = categorySim(a.getCategoria(), b.getCategoria());
        if (sCat != null) add("categoria", sCat, wCategoria, parts, scores, weights);

        // Ubicación
        Double sGeo = geoSim(a.getLatitud(), a.getLongitud(), b.getLatitud(), b.getLongitud());
        if (sGeo != null) add("ubicacion", sGeo, wUbicacion, parts, scores, weights);

        // Fecha
        Double sFecha = dateSim(a.getFechaSuceso(), b.getFechaSuceso());
        if (sFecha != null) add("fecha", sFecha, wFecha, parts, scores, weights);

        double totalWeight = weights.stream().mapToDouble(Double::doubleValue).sum();
        if (totalWeight == 0.0) {
            if (componentScoresOut != null) componentScoresOut.putAll(parts);
            return 0.0;
        }

        double weighted = 0.0;
        for (int i = 0; i < scores.size(); i++) {
            weighted += (weights.get(i) / totalWeight) * scores.get(i);
        }

        if (componentScoresOut != null) componentScoresOut.putAll(parts);
        return clamp01(weighted);
    }

    private void add(String name, double score, double weight,
                     Map<String, Double> parts, List<Double> scores, List<Double> weights) {
        if (weight <= 0) return;
        double s = clamp01(score);
        parts.put(name, s);
        scores.add(s);
        weights.add(weight);
    }

    //Similitud texto JACCAR de tokens
    private Double textJaccard(String t1, String t2) {
        Set<String> a = tokenize(t1, this.stopwords);
        Set<String> b = tokenize(t2, this.stopwords);
        if (a.isEmpty() && b.isEmpty()) return null;
        if (a.isEmpty() || b.isEmpty()) return 0.0;
        int inter = 0;
        for (String s : a) if (b.contains(s)) inter++;
        int union = a.size() + b.size() - inter;
        return union == 0 ? 0.0 : (double) inter / union;
    }

    //Si categorias son iguales.
    private Double categorySim(String c1, String c2) {
        if (isBlank(c1) && isBlank(c2)) return null;
        if (isBlank(c1) || isBlank(c2)) return 0.0;
        String n1 = normalize(c1);
        String n2 = normalize(c2);
        if (n1.equals(n2)) return 1.0;
        return textJaccard(c1, c2);
    }


    //Similitud geografica, null si falta coordenadas
    private Double geoSim(String lat1, String lon1, String lat2, String lon2) {
        if (this.maxMeters <= 0) return null;
        Double la1 = parseDouble(lat1), lo1 = parseDouble(lon1);
        Double la2 = parseDouble(lat2), lo2 = parseDouble(lon2);
        if (la1 == null || lo1 == null || la2 == null || lo2 == null) return null;
        double d = haversineMeters(la1, lo1, la2, lo2);
        if (d >= this.maxMeters) return 0.0;
        return 1.0 - (d / this.maxMeters);
    }

    //Similitud fecha, null si falta
    private Double dateSim(String f1, String f2) {
        if (this.maxDays <= 0) return null;
        LocalDate d1 = this.dateParser.apply(f1);
        LocalDate d2 = this.dateParser.apply(f2);
        if (d1 == null || d2 == null) return null;
        long dd = Math.abs(java.time.temporal.ChronoUnit.DAYS.between(d1, d2));
        if (dd >= this.maxDays) return 0.0;
        return 1.0 - ((double) dd / this.maxDays);
    }

    private Set<String> tokenize(String text, Set<String> stop) {
        Set<String> out = new HashSet<>();
        if (isBlank(text)) return out;
        String n = normalize(text);
        for (String tok : n.split("\\s+")) {
            if (tok.length() < 2) continue;
            if (stop.contains(tok)) continue;
            out.add(tok);
        }
        return out;
    }

    // a lowercase + sin acentos + sin signos + sin espacios
    private String normalize(String s) {
        if (s == null) return "";
        String lower = s.toLowerCase(Locale.ROOT);
        String deacc = Normalizer.normalize(lower, Normalizer.Form.NFD).replaceAll("\\p{M}+", "");
        String lettersDigits = deacc.replaceAll("[^\\p{L}\\p{Nd}]+", " ");
        return lettersDigits.trim().replaceAll("\\s+", " ");
    }

    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }

        private Double parseDouble(String s) {
        if (isBlank(s)) return null;
        try { return Double.parseDouble(s.trim()); }
        catch (Exception e) { return null; }
    }

    //Distancia Haversine en metros.
    private double haversineMeters(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000.0; // m
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2)*Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2))*
                        Math.sin(dLon/2)*Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }

    private double clamp01(double v) { return Math.max(0.0, Math.min(1.0, v)); }
}
