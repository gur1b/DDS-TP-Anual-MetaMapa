package core.api.DTO.criterio;

import core.models.entities.colecciones.criterios.CriterioFechaSuceso;
import core.models.repository.CriteriosRepository;

import java.time.LocalDate;

public class CriterioFechaSucesoDTO extends CriterioDTO{
    private LocalDate desde;
    private LocalDate hasta;

    public CriterioFechaSucesoDTO() {}

    public CriterioFechaSucesoDTO(Integer id, LocalDate fechaInicio, LocalDate fechaFin) {
        super(id);
        this.desde = fechaInicio;
        this.hasta = fechaFin;
    }

    @Override
    public CriterioFechaSuceso toEntity(){
        if (desde == null || hasta == null) {
            throw new IllegalArgumentException("rango de fecha suceso inválido en CriterioFechaSucesoDTO");
        }

        CriteriosRepository repo = CriteriosRepository.getInstance();

        CriterioFechaSuceso existente = repo.buscarFechaSuceso(desde, hasta);
        if (existente != null) {
            return existente;
        }

        CriterioFechaSuceso nuevo = new CriterioFechaSuceso(desde, hasta);
        repo.add(nuevo);
        return nuevo;
    }

    public LocalDate getDesde() { return desde; }
    public LocalDate getHasta() { return hasta; }
    public void setDesde(LocalDate desde) { this.desde = desde; }
    public void setHasta(LocalDate hasta) { this.hasta = hasta; }

}
