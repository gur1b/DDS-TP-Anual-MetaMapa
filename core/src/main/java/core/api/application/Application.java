package core.api.application;

import core.api.graphql.GraphQLProvider;
import core.models.repository.ColeccionesRepository;
import core.models.repository.FuentesRepository;
import core.models.repository.HechosRepository;
import core.models.repository.seeders.ColeccionesRepositorySeeder;
import core.models.repository.seeders.FuentesRepositorySeeder;
import core.models.repository.seeders.HechosRepositorySeeder;
import core.models.repository.seeders.SolicitudEliminacioRepositorySeeder;
import core.observabilidad.PrometheusExporter;
import core.observabilidad.RegistroMetricas;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private static final String HDR_TRACE = "X-Trace-Id";
    private static final String HDR_CORR  = "X-Correlation-Id"; // compat opcional
    private static final String ATTR_TRACE = "traceId";
    private static final String ATTR_START_NS = "startTimeNs";

    public static void main(String[] args) {
        HechosRepository hechosRepo = HechosRepository.getInstance();
        ColeccionesRepository colRepo = ColeccionesRepository.getInstance();
        FuentesRepository fuentesRepo = FuentesRepository.getInstance();

        GraphQLProvider graphQLProvider = new GraphQLProvider(hechosRepo, colRepo, fuentesRepo);
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public"); // carpeta en el classpath: src/main/resources/public
        });

        // BEFORE: traceId + startTime + log request + métrica request
        app.before(ctx -> {
            RegistroMetricas.sumPeticiones();

            String traceId = resolveTraceId(ctx);
            ctx.attribute(ATTR_TRACE, traceId);
            MDC.put(ATTR_TRACE, traceId);

            // devolverlo al cliente SIEMPRE
            ctx.header(HDR_TRACE, traceId);

            // start time
            ctx.attribute(ATTR_START_NS, System.nanoTime());

            log.info(
                    "http_req traceId={} method={} path={} ip={} ua=\"{}\" query=\"{}\"",
                    traceId,
                    ctx.method(),
                    ctx.path(),
                    extractClientIp(ctx),
                    safe(ctx.userAgent()),
                    safe(ctx.queryString())
            );
        });

        // AFTER: corre incluso si hubo exception handler.
        app.after(ctx -> {
            String traceId = (String) ctx.attribute(ATTR_TRACE);
            if (traceId == null) traceId = MDC.get(ATTR_TRACE);

            long durationMs = durationMs(ctx);
            if (durationMs >= 0) RegistroMetricas.sumTiempoPeticion(durationMs);

            String contentType = safe(ctx.res().getContentType());

            int bufSize = ctx.res().getBufferSize();

            log.info(
                    "http_res traceId={} method={} path={} status={} durationMs={} contentType={} resBufferSize={}",
                    traceId,
                    ctx.method(),
                    ctx.path(),
                    ctx.status(),
                    durationMs,
                    contentType,
                    bufSize
            );

            MDC.clear();
        });

        // EXCEPTIONS: log + métrica de error + traceId en response
        app.exception(Exception.class, (e, ctx) -> {
            RegistroMetricas.sumErrores();

            String traceId = (String) ctx.attribute(ATTR_TRACE);
            if (traceId == null || traceId.isBlank()) traceId = MDC.get(ATTR_TRACE);
            if (traceId == null || traceId.isBlank()) traceId = UUID.randomUUID().toString();

            ctx.header(HDR_TRACE, traceId);

            log.error(
                    "http_error traceId={} method={} path={} ip={} msg={}",
                    traceId,
                    ctx.method(),
                    ctx.path(),
                    extractClientIp(ctx),
                    safeErrMsg(e),
                    e
            );

            ctx.status(500).result("Error interno");
        });

        // Raíz
        app.get("/", ctx -> ctx.result("MetaMapa core API ACTIVA"));

        // Métricas
        app.get("/metricas", ctx -> ctx.json(RegistroMetricas.snapshot()));
        app.get("/metrics", ctx -> {
            String body = PrometheusExporter.export(RegistroMetricas.snapshot());
            ctx.contentType("text/plain; charset=utf-8");
            ctx.result(body);
        });

        // GraphQL
        app.post("/graphql", ctx -> {
            Map<String, Object> body = ctx.bodyAsClass(Map.class);
            String query = (String) body.get("query");
            Map<String, Object> variables = (Map<String, Object>) body.getOrDefault("variables", Map.of());

            log.debug("graphql_req traceId={} queryLen={} varsKeys={}",
                    MDC.get(ATTR_TRACE),
                    (query != null ? query.length() : 0),
                    variables.keySet()
            );

            Map<String, Object> result = graphQLProvider.execute(query, variables);
            ctx.json(result);
        });

        app.get("/playground", ctx -> ctx.redirect("/graphiql.html"));

        ApiMetaMapa.configurar(app);
        ApiAdminMetaMapa.configurar(app, graphQLProvider);

        FuentesRepositorySeeder.getInstance().cargarFuentesSeeder();
        ColeccionesRepositorySeeder.getInstance().cargarColeccionesRepositorySeeder();

        app.start("0.0.0.0", 8081);
    }

    // ===== helpers =====

    private static String resolveTraceId(io.javalin.http.Context ctx) {
        // Preferimos X-Trace-Id. Si no viene, aceptamos X-Correlation-Id por compat.
        String traceId = ctx.header(HDR_TRACE);
        if (traceId == null || traceId.isBlank()) {
            traceId = ctx.header(HDR_CORR);
        }
        if (traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString();
        }
        return traceId.trim();
    }
  
    private static long durationMs(io.javalin.http.Context ctx) {
        Long start = ctx.attribute(ATTR_START_NS);
        if (start == null) return -1;
        return (System.nanoTime() - start) / 1_000_000;
    }

    private static String extractClientIp(io.javalin.http.Context ctx) {
        String xff = ctx.header("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        return ctx.req().getRemoteAddr();
    }
      

    private static String safe(String s) {
        if (s == null || s.isBlank()) return "-";
        return s.length() > 200 ? s.substring(0, 200) + "..." : s;
    }

    private static String safeErrMsg(Throwable t) {
        String m = t.getMessage();
        if (m == null || m.isBlank()) return t.getClass().getSimpleName();
        return m.length() > 300 ? m.substring(0, 300) + "..." : m;
    }
}
