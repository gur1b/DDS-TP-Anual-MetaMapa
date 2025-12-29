package servicioEstadisticas.model.entities;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "hecho")
public class Hecho {

    @Id
    private String hash;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @Column(name= "fecha_suceso")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fecha_suceso;

    @Column(name = "hora_suceso")
    private LocalTime hora_suceso;

    @Column(name= "provincia")
    private String provincia;

    @OneToOne
    @JoinColumn(name = "id_ubicacion")
    private Coordenadas coordenadas;


    public Hecho(String id, Categoria categoria, LocalDateTime fecha_suceso, LocalTime hora_suceso,String provincia, Coordenadas coordenadas) {
        this.hash = id;
        this.categoria = categoria;
        this.fecha_suceso = fecha_suceso;
        this.hora_suceso = hora_suceso;
        this.provincia = provincia;
        this.coordenadas = coordenadas;
    }

    public Hecho() {}

    public String getHash() {
        return hash;
    }

    public void setHash(String id_hecho) {
        this.hash = hash;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public LocalDateTime getFecha_suceso() {
        return fecha_suceso;
    }

    public void setFecha_suceso(LocalDateTime fechaSuceso) {
        this.fecha_suceso = fechaSuceso;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public Coordenadas getCoordenadas() {return coordenadas;}

    public void setCoordenadas(Coordenadas coordenadas) {this.coordenadas = coordenadas;}

    public LocalTime getHora_suceso() {return hora_suceso;}

    public void setHora_suceso(LocalTime hora_suceso) {this.hora_suceso = hora_suceso;}
}
