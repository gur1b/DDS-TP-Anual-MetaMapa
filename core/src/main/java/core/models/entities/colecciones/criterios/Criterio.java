package core.models.entities.colecciones.criterios;

import core.models.entities.hecho.Hecho;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.persistence.*;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CriterioCategoria.class, name = "categoria"),
        @JsonSubTypes.Type(value = CriterioDescripcion.class, name = "descripcion"),
        @JsonSubTypes.Type(value = CriterioFechaCarga.class, name = "fechaCarga"),
        @JsonSubTypes.Type(value = CriterioUbicacion.class, name = "ubicacion"),
        @JsonSubTypes.Type(value = CriterioFechaModificacion.class, name = "fechaModificacion"),
        @JsonSubTypes.Type(value = CriterioFechaSuceso.class, name = "fechaSuceso"),
        @JsonSubTypes.Type(value = CriterioNombre.class, name = "nombre"),
        @JsonSubTypes.Type(value = CriterioFechaModificacion.class, name = "fechaModificacion"),
        @JsonSubTypes.Type(value = CriterioEtiqueta.class, name = "etiqueta"),
        @JsonSubTypes.Type(value = CriterioHoraSuceso.class, name = "horaSuceso")
})

@Entity
@Table(name = "criterio")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // o JOINED
@DiscriminatorColumn(name = "tipo_criterio", length = 30)
public abstract class Criterio {
    @Id
    @GeneratedValue
    private Integer id;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    protected Criterio() {}

    public abstract boolean cumpleCriterio(Hecho hecho);

}
