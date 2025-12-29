package core.observabilidad;

import java.util.Map;

public class PrometheusExporter {

    public static String export(Map<String, Object> snapshot) {
        StringBuilder sb = new StringBuilder();

        sb.append("# HELP metamapa_metrics Métricas de MetaMapa\n");
        sb.append("# TYPE metamapa_metrics untyped\n");

        snapshot.forEach((key, value) -> {
            if (value instanceof Number) {
                String metric = "metamapa_" + key
                        .replaceAll("([a-z])([A-Z])", "$1_$2")
                        .toLowerCase();
                sb.append(metric).append(" ").append(value).append("\n");
            }
        });

        return sb.toString();
    }
}
