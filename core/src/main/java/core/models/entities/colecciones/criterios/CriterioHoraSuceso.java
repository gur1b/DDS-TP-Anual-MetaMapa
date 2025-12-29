package core.models.entities.colecciones.criterios;

import core.models.entities.hecho.Hecho;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.*;
@Entity
@DiscriminatorValue("HORA_SUCESO")
public class CriterioHoraSuceso extends Criterio {

    private LocalTime horaInicio;
    private LocalTime horaFin;

    public CriterioHoraSuceso(LocalTime horaInicio, LocalTime horaFin){
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public CriterioHoraSuceso(){}

    @Override
    public boolean cumpleCriterio(Hecho hecho){
        LocalTime hora = hecho.getHoraSuceso();
        if (hora == null) {return false;}
        if (horaInicio.isBefore(horaFin)) {
            // rango normal (ej: 08:00–18:00)
            return !hora.isBefore(horaInicio) && !hora.isAfter(horaFin);
        } else {
            // cruza medianoche (ej: 22:00–02:00)
            return !hora.isBefore(horaInicio) || !hora.isAfter(horaFin);
        }
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }
}
