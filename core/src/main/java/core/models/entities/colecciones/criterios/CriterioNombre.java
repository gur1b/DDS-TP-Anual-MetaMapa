package core.models.entities.colecciones.criterios;

import core.models.entities.hecho.Hecho;
import javax.persistence.*;
@Entity
@DiscriminatorValue("NOMBRE")
public class CriterioNombre extends Criterio{
    private String palabraClave;

    public CriterioNombre(String palabraClave){
        this.palabraClave = palabraClave;
    }

    public  CriterioNombre(){}

    @Override
    public boolean cumpleCriterio(Hecho hecho) {
        return hecho.getTitulo().toLowerCase().contains(palabraClave.toLowerCase());
    }

    public String getPalabraClave() {
        return palabraClave;
    }
}
