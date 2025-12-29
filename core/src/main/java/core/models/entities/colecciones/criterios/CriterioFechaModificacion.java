package core.models.entities.colecciones.criterios;

import core.models.entities.hecho.Hecho;

import java.time.LocalDate;
import javax.persistence.*;
@Entity
@DiscriminatorValue("FECHA_MODIFICACION")
public class CriterioFechaModificacion extends Criterio{
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public CriterioFechaModificacion(LocalDate fechaInicio, LocalDate fechaFin){
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
    public CriterioFechaModificacion()
    {}
    @Override
    public boolean cumpleCriterio(Hecho hecho){
        return hecho.getUltimaFechaModificacion().isBefore(fechaFin) && hecho.getUltimaFechaModificacion().isAfter(fechaInicio);

    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
}
