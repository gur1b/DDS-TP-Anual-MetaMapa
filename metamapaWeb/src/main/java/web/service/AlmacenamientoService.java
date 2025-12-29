package web.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class AlmacenamientoService {
    private final Path rootHechos;

    public AlmacenamientoService(@Value("${app.upload.hechos-dir:uploads/hechos}") String hechosDir) {
        this.rootHechos = Paths.get(hechosDir).toAbsolutePath().normalize();
        System.out.println("Directorio de hechos: " + this.rootHechos);
    }

    public String guardarArchivoHecho(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            System.out.println("No se subió archivo");
            return null;
        }

        try {
            Files.createDirectories(rootHechos);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de uploads: " + rootHechos, e);
        }

        String extension = Optional.ofNullable(archivo.getOriginalFilename())
                .filter(fn -> fn.contains("."))
                .map(fn -> fn.substring(fn.lastIndexOf(".")))
                .orElse("");

        String nombreArchivo = UUID.randomUUID() + extension;
        Path destino = rootHechos.resolve(nombreArchivo);

        System.out.println("Guardando archivo en: " + destino.toAbsolutePath());

        try {
            archivo.transferTo(destino.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Error guardando archivo " + destino.toAbsolutePath(), e);
        }

        // "link" que vas a mandar al core
        return "/media/hechos/" + nombreArchivo;
    }
}
