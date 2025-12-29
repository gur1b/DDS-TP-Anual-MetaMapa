package core.models.entities.colecciones.criterios;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import core.models.entities.hecho.Categoria;
import core.models.entities.hecho.Hecho;
import javax.persistence.*;
@Entity
@DiscriminatorValue("CATEGORIA")
public class CriterioCategoria extends Criterio {
    @ManyToOne
    @JoinColumn(name = "categoria_id_categoria")
    private Categoria categoria;

    public CriterioCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public CriterioCategoria() {

    }

    //TENER EN CUENTA QUE TIENE QUE SER EL MISMO OBJETO EN ESPECIFICO!!
    //POR AHORA: ES CON COMPARACIÓN DE STRING :)
    @JsonSerialize(as = String.class)
    public boolean cumpleCriterio(Hecho hecho) {
        if (hecho.getCategoria() == null || hecho.getCategoria().getNombre() == null) {
            throw new IllegalStateException("El hecho tiene una categoría nula o sin nombre");
        }
        if (categoria == null || categoria.getNombre() == null) {
            throw new IllegalStateException("El criterio tiene una categoría nula o sin nombre");
        }

        return hecho.getCategoria().getNombre().toLowerCase().contains(categoria.getNombre().toLowerCase());

    }

    public String getCategoriaString() {
        return categoria.toString();
    }
}
