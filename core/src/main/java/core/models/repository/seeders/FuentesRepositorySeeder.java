package core.models.repository.seeders;

import core.models.entities.fuentes.Fuente;
import core.models.entities.fuentes.FuenteFactory;
import core.models.entities.fuentes.TipoConexion;
import core.models.entities.fuentes.TipoFuente;
import core.models.repository.FuentesRepository;

public class FuentesRepositorySeeder {

    private static volatile FuentesRepositorySeeder instance;

    public FuentesRepositorySeeder() {
    }


    public static FuentesRepositorySeeder getInstance() {
        if (instance == null) {
            synchronized (FuentesRepositorySeeder.class) {
                if (instance == null) {
                    instance = new FuentesRepositorySeeder();
                }
            }
        }
        return instance;
    }

    FuentesRepository fuentesRepository = FuentesRepository.getInstance();

    public void cargarFuentesSeeder()
    {
        Fuente fuenteCore = FuenteFactory.crearFuente("Hechos Reportados", null,  TipoFuente.DINAMICA, TipoConexion.DINAMICA);
        Fuente fuente2 = FuenteFactory.crearFuente("Seguridad Ciudadana", "https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/SeguridadCiudadana", TipoFuente.PROXY, TipoConexion.APIREST);
        Fuente fuente3 = FuenteFactory.crearFuente("Reportes Vecinales", "https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales", TipoFuente.PROXY, TipoConexion.APIREST);
        Fuente fuente4 = FuenteFactory.crearFuente("Comunidad ARG", "alertasurbanas.csv", TipoFuente.ESTATICA, TipoConexion.CSV);
        fuentesRepository.add(fuenteCore);
        fuentesRepository.add(fuente2);
        fuentesRepository.add(fuente3);
        fuentesRepository.add(fuente4);
    }


}
