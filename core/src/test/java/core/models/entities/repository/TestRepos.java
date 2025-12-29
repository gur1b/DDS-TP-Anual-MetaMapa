package core.models.entities.repository;

import core.models.repository.HechosRepository;
import core.models.repository.seeders.ColeccionesRepositorySeeder;
import core.models.repository.seeders.FuentesRepositorySeeder;
import core.models.repository.seeders.HechosRepositorySeeder;

public class TestRepos{
public static void main(String[] args){


    FuentesRepositorySeeder fuentesRepositorySeeder = FuentesRepositorySeeder.getInstance();
    fuentesRepositorySeeder.cargarFuentesSeeder();

    HechosRepositorySeeder hechosRepository = HechosRepositorySeeder.getInstance();
    hechosRepository.cargarHechosSeeder();

    ColeccionesRepositorySeeder coleccionesRepositorySeeder = ColeccionesRepositorySeeder.getInstance();
    coleccionesRepositorySeeder.cargarColeccionesRepositorySeeder();
}
}