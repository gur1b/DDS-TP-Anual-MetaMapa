package cargadorProxy;


import cargadorProxy.model.StrategyAPIREST;
import cargadorProxy.model.StrategyTipoConexion;
import cargadorProxy.repository.RepositoryFuentes;
import cargadorProxy.model.Fuente;
import cargadorProxy.model.TipoConexion;

public class RepositoryFuentesSeeder {

    private static RepositoryFuentesSeeder instance;
    private static RepositoryFuentes repositoryFuentes = RepositoryFuentes.getInstance();

    public static RepositoryFuentesSeeder getInstance() {
        if (instance == null) {
            synchronized (RepositoryFuentesSeeder.class) {
                if (instance == null) {
                    instance = new RepositoryFuentesSeeder();
                }
            }
        }
        return instance;
    }


    public void cargarRepos() {
        StrategyTipoConexion strategyAPIREST = new StrategyAPIREST();
        Fuente fuente2 = new Fuente(2,"Seguridad Ciudadana", "https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/SeguridadCiudadana",  strategyAPIREST, "API REST");
        Fuente fuente3 = new Fuente(3, "Reportes Vecinales", "https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales", strategyAPIREST, "API REST");

        repositoryFuentes.save(fuente2);
        repositoryFuentes.save(fuente3);
    }
}
