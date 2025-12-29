package core.api.application;

import core.api.configs.ApiMetaMapaConfig;
import io.javalin.Javalin;
import core.models.repository.seeders.ColeccionesRepositorySeeder;
import core.models.repository.seeders.FuentesRepositorySeeder;
import core.models.repository.seeders.HechosRepositorySeeder;
import core.models.repository.seeders.SolicitudEliminacioRepositorySeeder;
import utils.DBUtils;

import javax.persistence.EntityManager;

public class ApiMetaMapa {

    public static void configurar(Javalin app) {

        app.get("/public", ctx -> ctx.result("API MetaMapa PÚBLICA ACTIVA"));

        ApiMetaMapaConfig.configurarEndpoints(app);
    }
}
