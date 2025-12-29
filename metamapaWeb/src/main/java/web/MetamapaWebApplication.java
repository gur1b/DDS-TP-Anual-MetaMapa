package web;

import web.config.RutasProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RutasProperties.class)
public class MetamapaWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(MetamapaWebApplication.class, args);
    }

}