package cargadorDinamica.repository;

import cargadorDinamica.model.Fuente;
import cargadorDinamica.model.StrategyDinamica;
import cargadorDinamica.model.StrategyTipoConexion;

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

    StrategyTipoConexion StrategyDinamica = new StrategyDinamica();

    Fuente fuente1 = new Fuente(1,"Hechos Reportados", null,  StrategyDinamica, "DINAMICA");

    public void cargarRepos() {
        repositoryFuentes.save(fuente1);
    }

}