package core.api.DTO.criterio;

import core.models.entities.colecciones.criterios.CriterioNombre;
import core.models.repository.CriteriosRepository;

public class CriterioNombreDTO extends CriterioDTO {
    private String palabraClave;

    public CriterioNombreDTO() {}

    public CriterioNombreDTO(Integer id, String descripcion) {
        super(id);
        this.palabraClave = descripcion;
    }

    @Override
    public CriterioNombre toEntity(){
        if (palabraClave == null || palabraClave.isBlank()) {
            throw new IllegalArgumentException("palabraClave vacía para CriterioNombreDTO");
        }

        CriteriosRepository repo = CriteriosRepository.getInstance();

        CriterioNombre existente = repo.buscarNombre(palabraClave);
        if (existente != null) {
            return existente;
        }

        CriterioNombre nuevo = new CriterioNombre(palabraClave);
        repo.add(nuevo);
        return nuevo;
    }

    public String getPalabraClave() {
        return palabraClave;
    }
    public void setPalabraClave(String clave) {
        this.palabraClave = clave;
    }
}
