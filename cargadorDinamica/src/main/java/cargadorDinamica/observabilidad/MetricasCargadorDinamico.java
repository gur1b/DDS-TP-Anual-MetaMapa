package cargadorDinamica.observabilidad;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public final class MetricasCargadorDinamico {

    private static final AtomicLong totalRequests = new AtomicLong(0);
    private static final AtomicLong totalErrors   = new AtomicLong(0);
    private static final AtomicLong totalTimeMs   = new AtomicLong(0);

    private static final AtomicLong requestsObtenerHechos = new AtomicLong(0);
    private static final AtomicLong requestsReportarHecho   = new AtomicLong(0);

    public static void incRequestsObtenerHechos() {
        requestsObtenerHechos.incrementAndGet();
    }
    public static void incRequestsReportarHecho() {
        requestsReportarHecho.incrementAndGet();
    }

    private MetricasCargadorDinamico() {}

    public static void incRequests() {
        totalRequests.incrementAndGet();
    }

    public static void incErrors() {
        totalErrors.incrementAndGet();
    }

    public static void addTime(long ms) {
        if (ms >= 0) totalTimeMs.addAndGet(ms);
    }

    public static Map<String, Object> snapshot() {
        Map<String, Object> map = new HashMap<>();

        long reqs = totalRequests.get();
        long time = totalTimeMs.get();
        long avg  = (reqs > 0) ? time / reqs : 0;

        map.put("totalRequests", reqs);
        map.put("totalErrors", totalErrors.get());
        map.put("totalTimeMs", time);
        map.put("avgTimeMs", avg);
        map.put("requestsObtenerHechos", requestsObtenerHechos.get());
        map.put("requestsReportarHecho", requestsReportarHecho.get());

        return map;
    }
}