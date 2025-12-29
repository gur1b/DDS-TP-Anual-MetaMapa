package core.api.DTO;

import core.models.entities.colecciones.Coleccion;

import java.util.List;

public class ColeccionConFuentesDTO {
    public Integer id;
    public String titulo;
    public String descripcion;
    public List<FuenteDTO> fuentes;

    public ColeccionConFuentesDTO(Integer id, String titulo, String descripcion, List<FuenteDTO> fuentes) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fuentes = fuentes;
    }

    public static ColeccionConFuentesDTO from(Coleccion c) {
        List<FuenteDTO> fuentesDTO = (c.getFuentes() != null)
                ? c.getFuentes().stream().map(FuenteDTO::from).toList()
                : java.util.Collections.emptyList();

        return new ColeccionConFuentesDTO(
                c.getId(),
                c.getTitulo(),
                c.getDescripcionColeccion(),
                fuentesDTO
        );
    }


    public Integer getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public List<FuenteDTO> getFuentes() { return fuentes; }
}
