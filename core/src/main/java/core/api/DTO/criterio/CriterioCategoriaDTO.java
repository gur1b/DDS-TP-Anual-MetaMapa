package core.api.DTO.criterio;

import core.models.entities.colecciones.criterios.Criterio;
import core.models.entities.colecciones.criterios.CriterioCategoria;
import core.models.entities.hecho.Categoria;
import core.models.repository.CategoriaRepository;
import core.models.repository.CriteriosRepository;

public class CriterioCategoriaDTO extends CriterioDTO {
    private String categoria;

    public CriterioCategoriaDTO() {}

    public CriterioCategoriaDTO(Integer id, String categoria) {
        super(id);
        this.categoria = categoria;
    }

    @Override
    public Criterio toEntity(){
        if (categoria == null || categoria.isBlank()) {
            throw new IllegalArgumentException("categoría vacía en CriterioCategoriaDTO");
        }

        CategoriaRepository catRepo     = CategoriaRepository.getInstance();
        CriteriosRepository criteriosRepo = CriteriosRepository.getInstance();

        // 1) Buscar o crear la Categoria
        Categoria cat = catRepo.buscarPorNombre(categoria);
        if (cat == null) {
            cat = catRepo.add(new Categoria(categoria));
        }

        // 2) Buscar si ya existe un criterio para esa categoría
        CriterioCategoria existente = criteriosRepo.buscarCategoria(cat);
        if (existente != null) {
            return existente;
        }

        // 3) Crear y persistir criterio nuevo
        CriterioCategoria nuevo = new CriterioCategoria(cat);
        criteriosRepo.add(nuevo);
        return nuevo;
    }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}
