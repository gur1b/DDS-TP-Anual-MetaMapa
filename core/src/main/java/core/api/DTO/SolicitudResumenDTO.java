package core.api.DTO;

import core.models.entities.solicitud.SolicitudDeEliminacion;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;

public class SolicitudResumenDTO {
    public Integer id;
    public String descripcion;
    HechoResumenDTO hechoResumenDTO;


    public SolicitudResumenDTO(Integer id, String descripcion, HechoResumenDTO hechoResumenDTO) {
    this.id = id;
    this.descripcion = descripcion;
    this.hechoResumenDTO = hechoResumenDTO;
    }

    public static SolicitudResumenDTO from(SolicitudDeEliminacion s) {
    return new SolicitudResumenDTO(
            s.getId(),
            s.getDescripcion(),
            s.getHecho() != null ? HechoResumenDTO.from(s.getHecho()) : null
    );}

    public Integer getId() {
        return id;
    }

    public HechoResumenDTO getHechoResumenDTO() {
        return hechoResumenDTO;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setHechoResumenDTO(HechoResumenDTO hechoResumenDTO) {
        this.hechoResumenDTO = hechoResumenDTO;
    }
}