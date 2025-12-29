package core.api.DTO.criterio;

import core.models.entities.colecciones.criterios.CriterioHoraSuceso;
import core.models.repository.CriteriosRepository;

import java.time.LocalTime;

public class CriterioHoraSucesoDTO extends CriterioDTO {

    private LocalTime horaDesde;
    private LocalTime horaHasta;

    public CriterioHoraSucesoDTO() {}

    public CriterioHoraSucesoDTO(Integer id, LocalTime horaInicio, LocalTime horaFin) {
        super(id);
        this.horaDesde = horaInicio;
        this.horaHasta = horaFin;
    }

    @Override
    public CriterioHoraSuceso toEntity() {
        if (horaDesde == null || horaHasta == null) {
            throw new IllegalArgumentException(
                    "rango de hora suceso inválido en CriterioHoraSucesoDTO"
            );
        }

        CriteriosRepository repo = CriteriosRepository.getInstance();

        CriterioHoraSuceso existente = repo.buscarHoraSuceso(horaDesde, horaHasta);
        if (existente != null) {
            return existente;
        }

        CriterioHoraSuceso nuevo = new CriterioHoraSuceso(horaDesde, horaHasta);
        repo.add(nuevo);
        return nuevo;
    }

    public LocalTime getHoraDesde() {
        return horaDesde;
    }

    public LocalTime getHoraHasta() {
        return horaHasta;
    }

    public void setHoraDesde(LocalTime desde) {
        this.horaDesde = desde;
    }

    public void setHoraHasta(LocalTime hasta) {
        this.horaHasta = hasta;
    }
}
