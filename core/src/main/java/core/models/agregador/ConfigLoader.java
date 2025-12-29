package core.models.agregador;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
        private static Properties properties = new Properties();

        static {
            try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (input == null) {
                    throw new RuntimeException("No se encontró el archivo config.properties en resources/");
                }
                properties.load(input);
            } catch (Exception e) {
                throw new RuntimeException("Error cargando configuración: " + e.getMessage(), e);
            }
        }

    public static String getProperty(String key) {
        // Prioridad: variable de entorno (Docker)
        String envValue = System.getenv(key);
        if (envValue != null && !envValue.isBlank()) {
            return envValue.trim();
        }

        // Fallback: config.properties
        return properties.getProperty(key);
    }

    }
