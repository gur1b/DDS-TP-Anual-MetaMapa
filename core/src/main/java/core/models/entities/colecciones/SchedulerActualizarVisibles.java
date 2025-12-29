package core.models.entities.colecciones;

import core.models.entities.fuentes.Fuente;
import core.models.entities.hecho.Hecho;
import core.models.repository.ColeccionesRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import utils.DBUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class SchedulerActualizarVisibles {
    private final ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(SchedulerActualizarVisibles.class);

    public boolean hayBajaCargaEnSistema() {
        // Lógica de horario, etc.
        return true;
    }

    public void ejecutar() {
        if (hayBajaCargaEnSistema()) {
            logger.info("Iniciando actualización de visibles...");

            // 1. Pedimos al repo solo los IDs
            List<Integer> idsColecciones = coleccionesRepository.obtenerTodosLosIds();

            // 2. Delegamos la lógica pesada al repo, una por una
            int procesadas = 0;
            for (Integer id : idsColecciones) {
                coleccionesRepository.recalcularHechosVisibles(id);
                procesadas++;
            }

            logger.info("Finalizado. Colecciones actualizadas: {}", procesadas);
        }
    }
}
