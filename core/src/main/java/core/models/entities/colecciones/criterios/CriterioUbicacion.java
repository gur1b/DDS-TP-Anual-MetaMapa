package core.models.entities.colecciones.criterios;

import core.models.entities.hecho.Coordenadas;
import core.models.entities.hecho.Hecho;
import javax.persistence.*;
@Entity
@DiscriminatorValue("UBICACION")
public class CriterioUbicacion extends Criterio{
    @OneToOne
    @JoinColumn(name = "coordenadas_id_coordenadas")
    private Coordenadas coordenadas;

    public CriterioUbicacion(Coordenadas coordenadas){
        this.coordenadas = coordenadas;
    }

    public CriterioUbicacion(){}
    //TENER EN CUENTA QUE TIENE QUE SER EL MISMO OBJETO EN ESPECIFICO!!
    @Override
    public boolean cumpleCriterio(Hecho hecho) {
        return hecho.getUbicacion().getLatitud().equals(coordenadas.getLatitud())  && hecho.getUbicacion().getLongitud().equals(coordenadas.getLongitud())  ;
    }

    public Double getLatitud(){
        return coordenadas.getLatitud();
    }

    public Double getLongitud(){
        return coordenadas.getLongitud();
    }
    //TO DO gigante
}
