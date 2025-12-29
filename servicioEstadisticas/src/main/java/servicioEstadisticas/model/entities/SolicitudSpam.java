package servicioEstadisticas.model.entities;

import javax.persistence.*;


@Entity
@Table(name = "solicitud_de_eliminacion")
public class SolicitudSpam {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "aceptada")
    private Boolean aceptada;


    public SolicitudSpam(Boolean aceptada) {this.aceptada = aceptada;}

    public SolicitudSpam() {}


    public Integer getId() {
        return id;
    }

    public void setId(Integer id_spam) {
        this.id = id_spam;
    }

    public Boolean getFueSpam() {
        return aceptada;
    }

    public void setFueSpam(Boolean aceptada) {
        this.aceptada = aceptada;
    }
}
