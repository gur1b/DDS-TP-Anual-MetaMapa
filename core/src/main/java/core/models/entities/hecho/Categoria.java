package core.models.entities.hecho;


import javax.persistence.*;

@Entity(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Categoria(String categoria) {
        this.nombre = categoria;
    }
    public Categoria(){}

    @Override
    public String toString(){
        return nombre;
    }
}

