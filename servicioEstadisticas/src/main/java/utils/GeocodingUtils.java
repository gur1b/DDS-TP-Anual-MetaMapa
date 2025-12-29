package utils;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.format.DecimalStyle;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GeocodingUtils {
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/reverse";
    // Cache para acelerar la consulta de provincias por coordenadas
    private static final Map<String, String> coordenadaAProvinciaCache = new ConcurrentHashMap<>();

    public static String obtenerProvincia(double lat, double lon) {
        // Clave con decimales fijos para asegurar igualdad de claves
        String key = String.format(Locale.US, "%.6f,%.6f", lat, lon);
        if (coordenadaAProvinciaCache.containsKey(key)) {
            return coordenadaAProvinciaCache.get(key);
        }

        try {
            Thread.sleep(50);

            try (CloseableHttpClient client = HttpClients.createDefault()) {
                String url = String.format(Locale.US, "%s?format=json&lat=%f&lon=%f", NOMINATIM_URL, lat, lon);
                HttpGet request = new HttpGet(url);
                request.setHeader("User-Agent", "MetamapaApp/1.0");

                String response = EntityUtils.toString(client.execute(request).getEntity());

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response);

                JsonNode address = root.get("address");
                if (address != null) {
                    String[] posiblesCampos = {"state", "state_district", "province", "county"};

                    for (String campo : posiblesCampos) {
                        JsonNode province = address.get(campo);
                        if (province != null && !province.asText().isEmpty()) {
                            String provinciaObtenida = province.asText();
                            // Poner en cache antes de retornar
                            coordenadaAProvinciaCache.put(key, provinciaObtenida);
                            return provinciaObtenida;
                        }
                    }
                } else {
                    System.out.println("No se encontró el objeto 'address' en la respuesta");
                }
                coordenadaAProvinciaCache.put(key, "Desconocida");
                return "Desconocida";
            }
        } catch (Exception e) {
            e.printStackTrace();
            coordenadaAProvinciaCache.put(key, "Desconocida");
            return "Desconocida";
        }
    }
}