package servicioEstadisticas.csv;

import seeders.RepositoryServicioEstadisticasSeeder;

import java.io.IOException;

public class DEMOGenerarCSV {

    public static void main(String[] args) throws IOException {
        GeneradorDeEstadisticas generadorDeEstadisticas = new GeneradorDeEstadisticas();
        RepositoryServicioEstadisticasSeeder repoSeeder = RepositoryServicioEstadisticasSeeder.getInstance();
        repoSeeder.cargarHechos();
        generadorDeEstadisticas.generarTodasEstadisticas("Inundación" );

    }
}
