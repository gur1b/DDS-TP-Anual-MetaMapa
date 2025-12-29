package servicioEstadisticas.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "coordenadas")
public class Coordenadas {

    @Id
    private Integer id_ubicacion;

    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "longitud")
    private Double longitud;


    public Coordenadas() {}

    public Coordenadas(Integer id_ubicacion, Double latitud, Double longitud) {
        this.id_ubicacion = id_ubicacion;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Integer getId_ubicacion() {
        return id_ubicacion;
    }

    public void setId_ubicacion(Integer id_ubicacion) {
        this.id_ubicacion = id_ubicacion;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}
