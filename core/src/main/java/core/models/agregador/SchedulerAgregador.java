package core.models.agregador;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SchedulerAgregador {

    private static final Logger log = LoggerFactory.getLogger(SchedulerAgregador.class);
    private static final String TRACE_KEY = "traceId";

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private boolean enEjecucion = false;
    private final ServicioDeAgregacion servicioDeAgregacion = ServicioDeAgregacion.getInstance();
    private final HandlerCargadores handlerCargadores = HandlerCargadores.getInstance();
    private List<HechoAIntegrarDTO> hechos = new ArrayList<>();

    public SchedulerAgregador(boolean enEjecucion) {
        this.enEjecucion = enEjecucion;
    }

    public void iniciarScheduler() {
        if (enEjecucion) {
            log.warn("El scheduler ya está en ejecución");
            return;
        }

        enEjecucion = true;
        log.info("Iniciando scheduler...");

        scheduler.scheduleAtFixedRate(this::verificarNuevosHechos, 0, 20, TimeUnit.SECONDS);
    }

    public void verificarNuevosHechos() {
        MDC.put("traceId", UUID.randomUUID().toString().substring(0, 8));
        try {
            hechos.addAll(handlerCargadores.extraerHechosAIntegrar());
            servicioDeAgregacion.actualizarColecciones(hechos);
            hechos.clear();
        } finally {
            MDC.clear();
        }
    }

    public List<HechoAIntegrarDTO> obtenerHechosAIntegrar(){
        List<HechoAIntegrarDTO> hechos = handlerCargadores.extraerHechosAIntegrar();
      return hechos;
    }


    public void detenerScheduler() {
        enEjecucion = false;
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        System.out.println("Scheduler detenido");
    }

}
