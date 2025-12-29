package core.api.application;

import core.api.configs.ApiAdminMetaMapaConfig;
import core.api.graphql.GraphQLProvider;
import core.models.repository.ColeccionesRepository;
import core.models.repository.FuentesRepository;
import core.models.repository.HechosRepository;
import io.javalin.Javalin;
import core.models.repository.seeders.ColeccionesRepositorySeeder;
import core.models.repository.seeders.FuentesRepositorySeeder;
import core.models.repository.seeders.HechosRepositorySeeder;
import core.models.repository.seeders.SolicitudEliminacioRepositorySeeder;

import java.util.Map;

public class ApiAdminMetaMapa {

    public static void configurar(Javalin app, GraphQLProvider graphQLProvider) {

        app.get("/admin", ctx -> ctx.result("API ADMINISTRATIVA MetaMapa ACTIVA"));

        // NUEVO: endpoint GraphQL
        app.post("/admin/graphql", ctx -> {
            Map<String, Object> body = ctx.bodyAsClass(Map.class);

            String query = (String) body.get("query");
            Map<String, Object> variables = (Map<String, Object>) body.getOrDefault("variables", Map.of());

            Map<String, Object> result = graphQLProvider.execute(query, variables);
            ctx.json(result);
        });

        ApiAdminMetaMapaConfig.configurarEndpoints(app);
    }
}
