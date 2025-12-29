package core.models.entities.hecho;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "etiqueta")
public class Etiqueta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_etiqueta")
    private Integer id;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "nombre")
    private String nombre;

    @ManyToMany(mappedBy = "etiquetas")
    private List<Hecho> hechos = new ArrayList<>();

    public  Etiqueta(){}

    public Etiqueta(String categoria) {
        this.nombre = categoria;
    }
    public String getTipo() {
        return nombre;
    }
    public void setTipo(String tipo) {
        this.nombre = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nuevoNombre) {
        nombre = nuevoNombre;
    }
}
