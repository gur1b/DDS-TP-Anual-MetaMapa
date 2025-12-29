package cargadorProxy.observabilidad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(CorrelationIdFilter.class);
    private static final String HEADER_NAME = "X-Correlation-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        long startNs = System.nanoTime();
        MetricasCargadorProxy.incRequests();

        String uri = request.getRequestURI();
        boolean esObtenerHechos = uri != null && uri.contains("/obtenerHechos");

        String correlationId = obtenerOCrearCorrelationId(request);

        try {
            MDC.put("correlationId", correlationId);
            response.setHeader(HEADER_NAME, correlationId);

            log.info("REQ {} {}", request.getMethod(), uri);

            if (esObtenerHechos) {
                MetricasCargadorProxy.incRequestsObtenerHechos();
            }

            filterChain.doFilter(request, response);

            long durationMs = calcularDuracionMs(startNs);
            MetricasCargadorProxy.addTime(durationMs);

            log.info("RES {} {} status={} time={}ms",
                    request.getMethod(),
                    uri,
                    response.getStatus(),
                    durationMs
            );

        } catch (Exception e) {
            MetricasCargadorProxy.incErrors();

            log.error("ERROR {} {} - {}",
                    request.getMethod(),
                    uri,
                    e.getClass().getSimpleName(),
                    e
            );

            throw e;
        } finally {
            MDC.clear();
        }
    }

    private String obtenerOCrearCorrelationId(HttpServletRequest request) {
        String correlationId = request.getHeader(HEADER_NAME);
        return (correlationId == null || correlationId.isBlank())
                ? UUID.randomUUID().toString()
                : correlationId;
    }

    private long calcularDuracionMs(long startNs) {
        return (System.nanoTime() - startNs) / 1_000_000;
    }
}