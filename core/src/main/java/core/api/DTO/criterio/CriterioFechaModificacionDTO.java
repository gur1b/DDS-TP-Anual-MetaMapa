package core.api.DTO.criterio;

import core.models.entities.colecciones.criterios.CriterioFechaModificacion;
import core.models.repository.CriteriosRepository;

import java.time.LocalDate;

public class CriterioFechaModificacionDTO extends CriterioDTO{
    private LocalDate desde;
    private LocalDate hasta;

    public CriterioFechaModificacionDTO() {}

    public CriterioFechaModificacionDTO(Integer id, LocalDate fechaInicio, LocalDate fechaFin) {
        super(id);
        this.desde = fechaInicio;
        this.hasta = fechaFin;
    }

    @Override
    public CriterioFechaModificacion toEntity(){
        if (desde == null || hasta == null) {
            throw new IllegalArgumentException("rango de fecha modificación inválido en CriterioFechaModificacionDTO");
        }

        CriteriosRepository repo = CriteriosRepository.getInstance();

        CriterioFechaModificacion existente = repo.buscarFechaModificacion(desde, hasta);
        if (existente != null) {
            return existente;
        }

        CriterioFechaModificacion nuevo = new CriterioFechaModificacion(desde, hasta);
        repo.add(nuevo);
        return nuevo;
    }

    public LocalDate getDesde() { return desde; }
    public LocalDate getHasta() { return hasta; }
    public void setDesde(LocalDate desde) { this.desde = desde; }
    public void setHasta(LocalDate hasta) { this.hasta = hasta; }
}
