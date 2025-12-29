package core.models.repository.seeders;


import core.models.entities.hecho.Hecho;
import core.models.entities.solicitud.SolicitudDeEliminacion;
import core.models.repository.HechosRepository;
import core.models.repository.SolicitudEliminacionRepository;

public class SolicitudEliminacioRepositorySeeder {

    private static volatile SolicitudEliminacioRepositorySeeder instance;

    public static SolicitudEliminacioRepositorySeeder getInstance() {
        if (instance == null) {
            synchronized (SolicitudEliminacioRepositorySeeder.class) {
                if (instance == null) {
                    instance = new SolicitudEliminacioRepositorySeeder();
                }
            }
        }
        return instance;
    }

    HechosRepository hechosRepository = HechosRepository.getInstance();

    Hecho hecho1 = hechosRepository.findById(1);
    Hecho hecho2 = hechosRepository.findById(2);
    Hecho hecho4 = hechosRepository.findById(4);

    SolicitudEliminacionRepository solicitudDeEliminacion = SolicitudEliminacionRepository.getInstance();

    SolicitudDeEliminacion solicitudDeEliminacion1 = new SolicitudDeEliminacion(1, hecho1,"Cualquier cosa dice, es mi casa. No hubo ningun incendio", null, null);
    SolicitudDeEliminacion solicitudDeEliminacion3 = new SolicitudDeEliminacion(3, hecho4,"espero que la persona este bien", null, null);

    public void cargarSolicitudDeEliminacionSeeder()
    {solicitudDeEliminacion.add(solicitudDeEliminacion1);
        solicitudDeEliminacion.add(solicitudDeEliminacion3);
    }
}
