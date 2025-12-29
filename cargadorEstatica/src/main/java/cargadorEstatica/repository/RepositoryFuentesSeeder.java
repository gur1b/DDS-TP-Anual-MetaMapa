package cargadorEstatica.repository;

import cargadorEstatica.model.Fuente;
import cargadorEstatica.model.StrategyCSV;
import cargadorEstatica.model.StrategyTipoConexion;

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
        StrategyTipoConexion StrategyCSV = new StrategyCSV();
        Fuente fuente4 = new Fuente(4,"Comunidad ARG", "alertasurbanas.csv",  StrategyCSV, "CSV");
        repositoryFuentes.save(fuente4);
    }

}