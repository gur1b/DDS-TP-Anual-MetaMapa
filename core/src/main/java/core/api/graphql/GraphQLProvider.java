package core.api.graphql;

import core.models.repository.HechosRepository;
import core.models.repository.ColeccionesRepository;
import core.models.repository.FuentesRepository;
import graphql.ExecutionInput;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

public class GraphQLProvider {

    private final GraphQL graphQL;

    public GraphQLProvider(
            HechosRepository hechosRepository,
            ColeccionesRepository coleccionesRepository,
            FuentesRepository fuentesRepository
    ) {
        GraphQLSchema schema = buildSchema(hechosRepository, coleccionesRepository, fuentesRepository);
        this.graphQL = GraphQL.newGraphQL(schema).build();
    }

    public GraphQL getGraphQL() {
        return graphQL;
    }

    public Map<String, Object> execute(String query, Map<String, Object> variables) {
        ExecutionInput input = ExecutionInput.newExecutionInput()
                .query(query)
                .variables(variables != null ? variables : Map.of())
                .build();
        return graphQL.execute(input).toSpecification();
    }

    private GraphQLSchema buildSchema(
            HechosRepository hechosRepository,
            ColeccionesRepository coleccionesRepository,
            FuentesRepository fuentesRepository
    ) {
        // 1) Leer schema.graphqls
        Reader schemaReader = new InputStreamReader(
                getClass().getResourceAsStream("/schema.graphqls")
        );
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaReader);

        // 2) Wiring: conectar fields con datafetchers
        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring
                        .dataFetcher("hechos", new HechosDataFetcher(hechosRepository, coleccionesRepository))
                        .dataFetcher("colecciones", env -> coleccionesRepository.listarColeccionesParaGraphQL())
                        .dataFetcher("coleccion", env -> {
                            Integer id = Integer.valueOf(env.getArgument("id"));
                            return coleccionesRepository.buscarColeccionParaGraphQL(id);
                        })
                        .dataFetcher("fuentes", env -> fuentesRepository.listarFuentesParaGraphQL())
                        .dataFetcher("fuente", env -> {
                                 Integer id = Integer.valueOf(env.getArgument("id"));
                                 return fuentesRepository.buscarFuenteParaGraphQL(id);
                        })
                )
                // Si necesitás datafetchers específicos para campos anidados, podés agregarlos acá
                .build();

        // 3) Construir esquema ejecutable
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }
}
