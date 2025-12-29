package cargadorDinamica.model;

import cargadorDinamica.repository.DinamicaRepository;
import cargadorDinamica.repository.RepositoryFuentes;

import java.util.List;

public class CargadorDinamico {

    private static CargadorDinamico instance;
    private RepositoryFuentes repositoryFuentes = RepositoryFuentes.getInstance();

    public static CargadorDinamico getInstance() {
        if (instance == null) {
            synchronized (CargadorDinamico.class) {
                if (instance == null) {
                    instance = new CargadorDinamico();
                }
            }
        }
        return instance;
    }

    public List<HechoAIntegrarDTO> extraerHechosAIntegrar(){
        Fuente fuente = repositoryFuentes.findAll().getFirst();
        return fuente.extraerHechos();
    }
}


