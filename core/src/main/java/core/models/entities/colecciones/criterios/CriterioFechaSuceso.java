package core.models.entities.colecciones.criterios;

import core.models.entities.hecho.Hecho;

import java.time.LocalDate;
import javax.persistence.*;
@Entity
@DiscriminatorValue("FECHA_SUCESO")
public class CriterioFechaSuceso extends Criterio {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public CriterioFechaSuceso(LocalDate fechaInicio, LocalDate fechaFin){
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public CriterioFechaSuceso(){}

    @Override
    public boolean cumpleCriterio(Hecho hecho){
        return hecho.getFechaSuceso().isBefore(fechaFin) && hecho.getFechaSuceso().isAfter(fechaInicio);
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }
}
