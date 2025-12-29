package core.models.entities.fuentes;

import core.models.entities.fuentes.Fuente;
import core.models.entities.fuentes.FuenteFactory;
import core.models.entities.fuentes.TipoConexion;
import core.models.entities.fuentes.TipoFuente;
import core.models.repository.HechosRepository;

public class ConsumirAPITest {
    public static void main(String[] args) throws Exception {

        Fuente fuente = FuenteFactory.crearFuente("API de ejemplo","https://684b1942165d05c5d35b843b.mockapi.io/metamapa/hechos", TipoFuente.PROXY, TipoConexion.APIREST);
        HechosRepository hechosRepository = HechosRepository.getInstance();

        System.out.println("Hay " + hechosRepository.obtenerTodas().size() + " hechos en el repositorio");
        System.out.println("Extrayendo hechos de API...");

        fuente.extraerHechos();
        System.out.println("Hay " + hechosRepository.obtenerTodas().size() + " hechos nuevos en el repositorio");

    }
}
