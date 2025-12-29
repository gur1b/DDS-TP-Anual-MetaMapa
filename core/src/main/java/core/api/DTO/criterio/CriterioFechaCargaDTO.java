package core.api.DTO.criterio;

import core.models.entities.colecciones.criterios.CriterioFechaCarga;
import core.models.repository.CriteriosRepository;

import java.time.LocalDate;

public class CriterioFechaCargaDTO extends CriterioDTO {
    private LocalDate desde;
    private LocalDate hasta;

    public CriterioFechaCargaDTO() {}

    public CriterioFechaCargaDTO(Integer id, LocalDate fechaInicio, LocalDate fechaFin) {
        super(id);
        this.desde = fechaInicio;
        this.hasta = fechaFin;
    }

    @Override
    public CriterioFechaCarga toEntity(){
        if (desde == null || hasta == null) {
            throw new IllegalArgumentException("rango de fecha carga inválido en CriterioFechaCargaDTO");
        }

        CriteriosRepository repo = CriteriosRepository.getInstance();

        CriterioFechaCarga existente = repo.buscarFechaCarga(desde, hasta);
        if (existente != null) {
            return existente;
        }

        CriterioFechaCarga nuevo = new CriterioFechaCarga(desde, hasta);
        repo.add(nuevo);
        return nuevo;
    }

    public LocalDate getDesde() { return desde; }
    public LocalDate getHasta() { return hasta; }
    public void setDesde(LocalDate desde) { this.desde = desde; }
    public void setHasta(LocalDate hasta) { this.hasta = hasta; }


}
