package core.api.DTO.criterio;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import core.models.entities.colecciones.criterios.*;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CriterioNombreDTO.class, name = "nombre"),
        @JsonSubTypes.Type(value = CriterioCategoriaDTO.class, name = "categoria"),
        @JsonSubTypes.Type(value = CriterioDescripcionDTO.class, name = "descripcion"),
        @JsonSubTypes.Type(value = CriterioFechaCargaDTO.class, name = "fechaCarga"),
        @JsonSubTypes.Type(value = CriterioUbicacionDTO.class, name = "ubicacion"),
        @JsonSubTypes.Type(value = CriterioFechaModificacionDTO.class, name = "fechaModificacion"),
        @JsonSubTypes.Type(value = CriterioFechaSucesoDTO.class, name = "fechaSuceso"),
        @JsonSubTypes.Type(value = CriterioHoraSucesoDTO.class, name = "horaSuceso")
})
public abstract class CriterioDTO {
    private Integer id;

    public CriterioDTO(){}

    public CriterioDTO(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public abstract Criterio toEntity();

    public static CriterioDTO from(Criterio criterio) {
        if (criterio instanceof CriterioCategoria c) {
            return new CriterioCategoriaDTO(c.getId(), c.getCategoriaString());
        }
        if (criterio instanceof CriterioDescripcion c) {
            return new CriterioDescripcionDTO(c.getId(), c.getPalabraClave());
        }
        if (criterio instanceof CriterioFechaCarga c) {
            return new CriterioFechaCargaDTO(c.getId(), c.getFechaInicio(), c.getFechaFin());
        }
        if (criterio instanceof CriterioUbicacion c) {
            return new CriterioUbicacionDTO(c.getId(), c.getLatitud(), c.getLongitud());
        }
        if (criterio instanceof CriterioNombre c) {
            return new CriterioNombreDTO(c.getId(), c.getPalabraClave());
        }
        if (criterio instanceof CriterioFechaSuceso c) {
            return new CriterioFechaSucesoDTO(c.getId(), c.getFechaInicio(), c.getFechaFin());
        }
        if (criterio instanceof CriterioFechaModificacion c) {
            return new CriterioFechaModificacionDTO(c.getId(), c.getFechaInicio(), c.getFechaFin());
        }
        if(criterio instanceof CriterioHoraSuceso c){
            return new CriterioHoraSucesoDTO(c.getId(), c.getHoraInicio(), c.getHoraFin());
        }
        return new CriterioDescripcionDTO(criterio.getId(), "Criterio desconocido");
    }
}


