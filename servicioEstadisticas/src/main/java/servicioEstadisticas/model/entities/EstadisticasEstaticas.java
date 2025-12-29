package servicioEstadisticas.model.entities;

import servicioEstadisticas.model.repository.RepositoryServicioEstadisticas;

import java.util.List;
import java.util.Map;

public class EstadisticasEstaticas {

    private List<Map<String, Object>> provinciaConMasHechos;
    private List<Map<String, Object>> categoriaMasReportada;
    private Map<String, Object> cantSolicitudesEliminacion;

    public static volatile EstadisticasEstaticas instance;

    public static EstadisticasEstaticas getInstance() {
        if (instance == null) {
            synchronized (EstadisticasEstaticas.class) {
                if (instance == null) {
                    instance = new EstadisticasEstaticas();
                }
            }
        }
        return instance;
    }

    public void actualizarEstadisticas() {
        this.provinciaConMasHechos = RepositoryServicioEstadisticas.provinciaConMasHechos();
        this.categoriaMasReportada = RepositoryServicioEstadisticas.categoriaMasReportada();
        this.cantSolicitudesEliminacion = RepositoryServicioEstadisticas.cantidadSolicitudesEliminacion();
    }

    public List<Map<String, Object>> getProvinciaConMasHechos() {
        return provinciaConMasHechos;
    }

    public List<Map<String, Object>> getCategoriaMasReportada() {
        return categoriaMasReportada;
    }

    public Map<String, Object> getCantSolicitudesEliminacion() {
        return cantSolicitudesEliminacion;
    }

}