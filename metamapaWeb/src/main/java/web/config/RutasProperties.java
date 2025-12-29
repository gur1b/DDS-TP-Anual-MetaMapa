package web.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "metamapa.api")
@Getter
@Setter
public class RutasProperties {
    private static final Logger log =
            LoggerFactory.getLogger(RutasProperties.class);
    private String baseUrl;
    private String adminBaseUrl;
    private String estadisticasBaseUrl;

    @PostConstruct
    public void init() {
        log.info("BASE CORE URL = " + baseUrl);
        log.info("ADMIN URL = " + adminBaseUrl);
        log.info("ESTADISTICAS URL = " + estadisticasBaseUrl);
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}