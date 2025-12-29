package web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Sirve archivos desde src/main/resources/static/uploads/hechos/
        String hechosStaticPath = Paths.get(System.getProperty("user.dir"),
                "metamapaWeb", "src", "main", "resources", "static", "uploads", "hechos").toAbsolutePath().toString() + "/";
        registry.addResourceHandler("/uploads/hechos/**")
                .addResourceLocations("file:" + hechosStaticPath);
    }

}
