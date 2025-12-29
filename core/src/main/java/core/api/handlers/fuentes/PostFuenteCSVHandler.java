package core.api.handlers.fuentes;

import core.api.utils.MultipartBodyPublisher;
import core.models.agregador.ConfigLoader;
import core.models.entities.fuentes.Fuente;
import core.models.repository.FuentesRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UploadedFile;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;

public class PostFuenteCSVHandler implements Handler{

    private final FuentesRepository fuentesRepository = FuentesRepository.getInstance();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private static final String CARGADOR_ESTATICO_BASE_URL = ConfigLoader.getProperty("CargadorEstatico");;

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        // OJO: este id es el id del cargadorEstatica que vino desde el front
        Integer idCore = Integer.valueOf(ctx.pathParam("id"));

        UploadedFile archivo = ctx.uploadedFile("archivoCsv");
        if (archivo == null) {
            ctx.status(400).result("Falta archivoCsv");
            return;
        }

        InputStream is = archivo.content();      // InputStream del archivo
        byte[] contenido = is.readAllBytes();    // bytes del CSV
        String nombreArchivo = archivo.filename();

        if (nombreArchivo == null || nombreArchivo.isBlank()) {
            nombreArchivo = "fuente_" + idCore + ".csv";
        }

        // si id core == id en cargadorEstatica, usamos ese. Si no, acá usarías el id remoto.
        Integer idFuenteCargador = idCore;

        // construimos body multipart
        MultipartBodyPublisher multipart = new MultipartBodyPublisher()
                .addFilePart("archivoCsv", nombreArchivo, "text/csv", contenido);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CARGADOR_ESTATICO_BASE_URL + "/fuentes/" + idFuenteCargador + "/csv"))
                .header("Content-Type", "multipart/form-data; boundary=" + multipart.getBoundary())
                .timeout(java.time.Duration.ofSeconds(10))
                .POST(multipart.build())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() / 100 == 2) {
            Fuente fuente = fuentesRepository.findById(idFuenteCargador);
            fuente.setLink(nombreArchivo);
            fuentesRepository.update(fuente);
            ctx.status(201).result("CSV enviado y guardado en cargadorEstatica");
        } else {
            System.out.println("[Core] Error al mandar CSV al cargadorEstatica: "
                    + response.statusCode() + " body=" + response.body());
            ctx.status(502).result("Error al mandar CSV al cargadorEstatica");
        }
    }
}
