package core.api.DTO.criterio;

import core.models.entities.colecciones.criterios.CriterioDescripcion;
import core.models.repository.CriteriosRepository;

public class CriterioDescripcionDTO extends CriterioDTO {
    private String palabraClave;

    public CriterioDescripcionDTO() {}

    public CriterioDescripcionDTO(Integer id, String descripcion){
        super(id);
        this.palabraClave = descripcion;
    }

    @Override
    public CriterioDescripcion toEntity(){
        if (palabraClave == null || palabraClave.isBlank()) {
            throw new IllegalArgumentException("palabraClave vacía para CriterioDescripcionDTO");
        }

        CriteriosRepository repo = CriteriosRepository.getInstance();

        // 1) Buscar si ya existe un criterio igual
        CriterioDescripcion existente = repo.buscarDescripcion(palabraClave);
        if (existente != null) {
            return existente;
        }

        // 2) Crear y persistir uno nuevo
        CriterioDescripcion nuevo = new CriterioDescripcion(palabraClave);
        repo.add(nuevo);
        return nuevo;
    }

    public String getPalabraClave() {return palabraClave;}
    public void setPalabraClave(String clave) {this.palabraClave = clave; }
}
