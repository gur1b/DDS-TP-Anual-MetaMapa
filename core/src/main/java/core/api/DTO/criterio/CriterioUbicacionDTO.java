package core.api.DTO.criterio;

import core.models.entities.colecciones.criterios.CriterioUbicacion;
import core.models.entities.hecho.Coordenadas;
import core.models.repository.CoordenadasRepository;
import core.models.repository.CriteriosRepository;

public class CriterioUbicacionDTO extends CriterioDTO{
    private Double latitud;
    private Double longitud;

    public CriterioUbicacionDTO() {}

    public CriterioUbicacionDTO(Integer id, Double latitud, Double longitud) {
        super(id);
        this.latitud = latitud;
        this.longitud = longitud;
    }

    @Override
    public CriterioUbicacion toEntity(){
        if (latitud == null || longitud == null) {
            throw new IllegalArgumentException("latitud/longitud nulas en CriterioUbicacionDTO");
        }

        CoordenadasRepository coordsRepo = CoordenadasRepository.getInstance();
        CriteriosRepository criteriosRepo = CriteriosRepository.getInstance();

        // 1) Buscar o crear las coordenadas
        Coordenadas ejemplo = new Coordenadas(latitud, longitud);
        Coordenadas coords = coordsRepo.buscarPorCoordenadas(ejemplo);
        if (coords == null) {
            coords = coordsRepo.add(ejemplo);
        }

        // 2) Buscar si ya existe un criterio con esas coords
        CriterioUbicacion existente = criteriosRepo.buscarUbicacion(coords);
        if (existente != null) {
            return existente;
        }

        // 3) Crear y persistir criterio nuevo
        CriterioUbicacion nuevo = new CriterioUbicacion(coords);
        criteriosRepo.add(nuevo);
        return nuevo;
    }

    public Double getLatitud() { return latitud; }
    public Double getLongitud() { return longitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }
}
