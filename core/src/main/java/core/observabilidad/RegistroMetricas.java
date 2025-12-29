package core.observabilidad;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class RegistroMetricas {

    private static final AtomicLong peticionesTotales = new AtomicLong(0);
    private static final AtomicLong erroresTotales   = new AtomicLong(0);
    private static final AtomicLong hechosProcesadosTotales = new AtomicLong(0);
    private static final AtomicLong tiempoMsPeticionesTotal = new AtomicLong(0);

    private static final AtomicLong reqsCargadorDinamico  = new AtomicLong(0);
    private static final AtomicLong errsCargadorDinamico  = new AtomicLong(0);
    private static final AtomicLong timeCargadorDinamicoMs = new AtomicLong(0);

    private static final AtomicLong reqsCargadorProxy  = new AtomicLong(0);
    private static final AtomicLong errsCargadorProxy  = new AtomicLong(0);
    private static final AtomicLong timeCargadorProxyMs = new AtomicLong(0);

    private static final AtomicLong reqsCargadorEstatico  = new AtomicLong(0);
    private static final AtomicLong errsCargadorEstatico  = new AtomicLong(0);
    private static final AtomicLong timeCargadorEstaticoMs = new AtomicLong(0);


    private RegistroMetricas() {
    }

    public static void sumPeticiones() {
        peticionesTotales.incrementAndGet();
    }

    public static void sumErrores() {
        erroresTotales.incrementAndGet();
    }

    public static void sumHechosCreados() {
        hechosProcesadosTotales.incrementAndGet();
    }

    public static void addHechosCreados(long cantidad) {
        if (cantidad <= 0) return;
        hechosProcesadosTotales.addAndGet(cantidad);
    }


    public static void sumTiempoPeticion(long msegundos) {
        if (msegundos >= 0) {
            tiempoMsPeticionesTotal.addAndGet(msegundos);
        }
    }

    public static void incReqDinamico() { reqsCargadorDinamico.incrementAndGet(); }
    public static void incErrDinamico() { errsCargadorDinamico.incrementAndGet(); }
    public static void addTimeDinamico(long ms) {
        if (ms >= 0) timeCargadorDinamicoMs.addAndGet(ms);
    }

    public static void incReqProxy() { reqsCargadorProxy.incrementAndGet(); }
    public static void incErrProxy() { errsCargadorProxy.incrementAndGet(); }
    public static void addTimeProxy(long ms) {
        if (ms >= 0) timeCargadorProxyMs.addAndGet(ms);
    }

    public static void incReqEstatico() { reqsCargadorEstatico.incrementAndGet(); }
    public static void incErrEstatico() { errsCargadorEstatico.incrementAndGet(); }
    public static void addTimeEstatico(long ms) {
        if (ms >= 0) timeCargadorEstaticoMs.addAndGet(ms);
    }

    public static Map<String, Object> snapshot() {
        Map<String, Object> m = new HashMap<>();

        long reqs = peticionesTotales.get();
        long totalTime = tiempoMsPeticionesTotal.get();
        long avg = (reqs > 0) ? (totalTime / reqs) : 0;
        long errors = erroresTotales.get();
        double errorRate = (reqs > 0) ? (errors * 1.0 / reqs) : 0.0;
        double porcentajeReqsDinamico = (reqsCargadorDinamico.get() * 1.0 / reqs);
        double porcentajeReqsProxy = (reqsCargadorProxy.get() * 1.0 / reqs);
        double porcentajeReqsEstatico = (reqsCargadorEstatico.get() * 1.0 / reqs);

        m.put("totalRequests", reqs);
        m.put("totalErrors", errors);
        m.put("totalHechosProcesados", hechosProcesadosTotales.get());
        m.put("totalRequestTimeMs", totalTime);
        m.put("avgResponseTimeMs", avg);
        m.put("errorRate", errorRate);
        m.put("successRate", 1.0 - errorRate);

        // métricas por cargador
        m.put("reqsHechosCargadorDinamico",  reqsCargadorDinamico.get());
        m.put("errsCargadorDinamico",  errsCargadorDinamico.get());
        m.put("timeCargadorDinamicoMs", timeCargadorDinamicoMs.get());
        m.put("porcentajeReqsDinamico", porcentajeReqsDinamico);

        m.put("reqsHechosCargadorProxy",  reqsCargadorProxy.get());
        m.put("errsCargadorProxy",  errsCargadorProxy.get());
        m.put("timeCargadorProxyMs", timeCargadorProxyMs.get());
        m.put("porcentajeReqsProxy", porcentajeReqsProxy);

        m.put("reqsHechosCargadorEstatico",  reqsCargadorEstatico.get());
        m.put("errsCargadorEstatico",  errsCargadorEstatico.get());
        m.put("timeCargadorEstaticoMs", timeCargadorEstaticoMs.get());
        m.put("porcentajeReqsEstatico", porcentajeReqsEstatico);

        return m;
    }
}