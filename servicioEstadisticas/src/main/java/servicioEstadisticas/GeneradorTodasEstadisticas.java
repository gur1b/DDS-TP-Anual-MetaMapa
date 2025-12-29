package servicioEstadisticas;
import servicioEstadisticas.model.entities.EstadisticasEstaticas;
import servicioEstadisticas.model.repository.RepositoryServicioEstadisticas;

import java.util.List;
import java.util.Map;

public class GeneradorTodasEstadisticas {

    public static volatile GeneradorTodasEstadisticas instance;

    private EstadisticasEstaticas estadisticasEstaticas = EstadisticasEstaticas.getInstance();

    public static GeneradorTodasEstadisticas getInstance() {
        if (instance == null) {
            synchronized (GeneradorTodasEstadisticas.class) {
                if (instance == null) {
                    instance = new GeneradorTodasEstadisticas();
                }
            }
        }
        return instance;
    }

    public void actualizarEstadisticas(){ estadisticasEstaticas.actualizarEstadisticas();}

    public List<Map<String, Object>> getProvinciaConMasHechos() {return estadisticasEstaticas.getProvinciaConMasHechos();}

     public List<Map<String, Object>> getCategoriaMasReportada() { return estadisticasEstaticas.getCategoriaMasReportada();}

    public Map<String, Object> getCantSolicitudesEliminacion() {return estadisticasEstaticas.getCantSolicitudesEliminacion();}


    public List<Map<String, Object>> horarioxCategoria(String categoria) {
        return RepositoryServicioEstadisticas.horarioxCategoria(categoria);
    }

    public List<Map<String, Object>> provinciaConMasHechosEnCategoria(String categoria) {
        return RepositoryServicioEstadisticas.provinciaConMasHechosEnCategoria(categoria);
    }
}