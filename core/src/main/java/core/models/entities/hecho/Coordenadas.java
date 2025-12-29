package core.models.entities.hecho;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import utils.GeocodingUtils;

import javax.persistence.*;

@Entity(name = "coordenadas")
public class Coordenadas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ubicacion")
    private Integer id;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "latitud")
    private Double latitud;
    public Double getLatitud() {
        return latitud;
    }
    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }


    @Column(name = "longitud")
    private Double longitud;
    public Double getLongitud() {
        return longitud;
    }
    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Coordenadas(Double latitud, Double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }
    public Coordenadas() {}

    public void cambiarUbicacion(Double latitud, Double longitud){
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String toString(){
        return "(Latitud: " + latitud + "° Longitud: " + longitud + "°)";
    }

    public String getProvincia() {
        System.out.println("Provincia: " + GeocodingUtils.obtenerProvincia(latitud, longitud));
        return GeocodingUtils.obtenerProvincia(latitud, longitud);
    }
}
