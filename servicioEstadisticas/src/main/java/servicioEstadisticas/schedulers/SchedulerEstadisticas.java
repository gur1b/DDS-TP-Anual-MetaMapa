package servicioEstadisticas.schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import servicioEstadisticas.GeneradorTodasEstadisticas;

import java.util.UUID;

@Component
@EnableScheduling
public class SchedulerEstadisticas {

    private static final Logger log = LoggerFactory.getLogger(SchedulerEstadisticas.class);

    private final GeneradorTodasEstadisticas generadorTodasEstadisticas;

    public SchedulerEstadisticas() {
        this.generadorTodasEstadisticas = GeneradorTodasEstadisticas.getInstance();
    }

    // Ejecutar cada 24 horas
    @Scheduled(fixedRate = 5 * 60 * 1000) // milliseconds
    public void ejecutarActualizacionEstadisticas() {
        long start = System.currentTimeMillis();
        String jobId = UUID.randomUUID().toString();
        log.info("[SCHEDULER] [{}] Iniciando job de actualización de estadísticas...", jobId);

        try {
            log.debug("[SCHEDULER] [{}] Ejecutando generadorTodasEstadisticas.actualizarEstadisticas()", jobId);

            generadorTodasEstadisticas.actualizarEstadisticas();

            long duration = System.currentTimeMillis() - start;
            log.info("[SCHEDULER] [{}] Estadísticas actualizadas correctamente. Duración: {} ms",
                    jobId, duration);

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            log.error("[SCHEDULER] [{}] ERROR actualizando estadísticas después de {} ms",
                    jobId, duration, e);
        }
    }
}

