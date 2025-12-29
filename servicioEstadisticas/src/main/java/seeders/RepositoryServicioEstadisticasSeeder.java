package seeders;

import servicioEstadisticas.model.repository.RepositoryServicioEstadisticas;

public class RepositoryServicioEstadisticasSeeder {


    private static volatile RepositoryServicioEstadisticasSeeder instance;
    private final RepositoryServicioEstadisticas repositoryServicioEstadisticas;

    public RepositoryServicioEstadisticasSeeder() {
        this.repositoryServicioEstadisticas = RepositoryServicioEstadisticas.getInstance();
    }

    public static RepositoryServicioEstadisticasSeeder getInstance() {
        if (instance == null) {
            synchronized (RepositoryServicioEstadisticasSeeder.class) {
               if (instance == null) {
                    instance = new RepositoryServicioEstadisticasSeeder();
                }
            }
        }
        return instance;
    }

    public void cargarHechos(){

        /*
        repositoryServicioEstadisticas.addHecho(new Hecho("INC001", "Incendio forestal", LocalDateTime.now().minusDays(5).withHour(14), "Misiones"));
        repositoryServicioEstadisticas.addHecho(new Hecho("INC002", "Incendio forestal", LocalDateTime.now().minusDays(3).withHour(15), "Misiones"));
        repositoryServicioEstadisticas.addHecho(new Hecho("INC003", "Incendio forestal", LocalDateTime.now().minusDays(2).withHour(14), "Córdoba"));
        repositoryServicioEstadisticas.addHecho(new Hecho("INC004", "Incendio forestal", LocalDateTime.now().minusDays(1).withHour(14), "Córdoba"));

        // Accidentes de tránsito
        repositoryServicioEstadisticas.addHecho(new Hecho("ACC001", "Accidente vial", LocalDateTime.now().minusDays(7).withHour(8), "Buenos Aires"));
        repositoryServicioEstadisticas.addHecho(new Hecho("ACC002", "Accidente vial", LocalDateTime.now().minusDays(6).withHour(8), "Buenos Aires"));
        repositoryServicioEstadisticas.addHecho(new Hecho("ACC003", "Accidente vial", LocalDateTime.now().minusDays(5).withHour(8), "Buenos Aires"));
        repositoryServicioEstadisticas.addHecho(new Hecho("ACC004", "Accidente vial", LocalDateTime.now().minusDays(4).withHour(18), "Mendoza"));

        // Inundaciones
        repositoryServicioEstadisticas.addHecho(new Hecho("INU001", "Inundación", LocalDateTime.now().minusDays(10).withHour(23), "Buenos Aires"));
        repositoryServicioEstadisticas.addHecho(new Hecho("INU002", "Inundación", LocalDateTime.now().minusDays(9).withHour(22), "Buenos Aires"));
        repositoryServicioEstadisticas.addHecho(new Hecho("INU003", "Inundación", LocalDateTime.now().minusDays(8).withHour(23), "Santa Fe"));

        // Manifestaciones
        repositoryServicioEstadisticas.addHecho(new Hecho("MAN001", "Manifestación", LocalDateTime.now().minusDays(2).withHour(12), "Buenos Aires"));
        repositoryServicioEstadisticas.addHecho(new Hecho("MAN002", "Manifestación", LocalDateTime.now().minusDays(1).withHour(12), "Buenos Aires"));
        repositoryServicioEstadisticas.addHecho(new Hecho("MAN003", "Manifestación", LocalDateTime.now().minusDays(1).withHour(12), "Córdoba"));

        // Robos
        repositoryServicioEstadisticas.addHecho(new Hecho("ROB001", "Robo", LocalDateTime.now().minusDays(3).withHour(2), "Buenos Aires"));
        repositoryServicioEstadisticas.addHecho(new Hecho("ROB002", "Robo", LocalDateTime.now().minusDays(2).withHour(3), "Buenos Aires"));
        repositoryServicioEstadisticas.addHecho(new Hecho("ROB003", "Robo", LocalDateTime.now().minusDays(1).withHour(2), "Mendoza"));

        // Cortes de energía
        repositoryServicioEstadisticas.addHecho(new Hecho("COR001", "Corte de energía", LocalDateTime.now().minusDays(4).withHour(20), "Buenos Aires"));
        repositoryServicioEstadisticas.addHecho(new Hecho("COR002", "Corte de energía", LocalDateTime.now().minusDays(3).withHour(19), "Santa Fe"));
        repositoryServicioEstadisticas.addHecho(new Hecho("COR003", "Corte de energía", LocalDateTime.now().minusDays(2).withHour(20), "Misiones"));

        repositoryServicioEstadisticas.addSolicitud(new SolicitudSpam(false));
        repositoryServicioEstadisticas.addSolicitud(new SolicitudSpam(false));
        repositoryServicioEstadisticas.addSolicitud(new SolicitudSpam(true));
        repositoryServicioEstadisticas.addSolicitud(new SolicitudSpam(true ));
        repositoryServicioEstadisticas.addSolicitud(new SolicitudSpam(true));


         */

    }



}
