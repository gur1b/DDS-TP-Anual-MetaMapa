package core.api.handlers.sugerenciasDeCambio;

import core.api.DTO.SolicitudDeEliminacionDTO;
import core.api.DTO.SugerenciaConHechoDTO;
import core.api.DTO.SugerenciaDTO;
import core.api.handlers.solicitudesDeEliminacion.PostSolicitudHandler;
import core.models.entities.hecho.Categoria;
import core.models.entities.hecho.Etiqueta;
import core.models.entities.hecho.Hecho;
import core.models.entities.hecho.SugerenciaDeCambio;
import core.models.entities.solicitud.SolicitudDeEliminacion;
import core.models.repository.CategoriaRepository;
import core.models.repository.EtiquetasRepository;
import core.models.repository.HechosRepository;
import core.models.repository.SugerenciasDeCambioRepository;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PostSugerenciaHandler implements Handler {

    private static final Logger log = LoggerFactory.getLogger(PostSugerenciaHandler.class);

    private final SugerenciasDeCambioRepository sugerenciasRepository = SugerenciasDeCambioRepository.getInstance();
    private final CategoriaRepository categoriaRepo = CategoriaRepository.getInstance();
    private final EtiquetasRepository etiquetasRepo = EtiquetasRepository.getInstance();

    @Override
    public void handle(io.javalin.http.Context context) {
        SugerenciaDTO dto  = context.bodyAsClass(SugerenciaDTO.class);
        log.info("Creando sugerencia de cambio hechoID={}",
                (dto != null ? dto.getIdHecho() : "null")
        );

        assert dto != null;
        Hecho hecho = HechosRepository.getInstance().getHecho(dto.getIdHecho());

        if (hecho == null) {
            log.warn("Hecho no encontrado para solicitud hechoHash={}", dto.getIdHecho());
            context.status(404).result("Hecho con ID " + dto.getIdHecho() + " no encontrado");
            return;
        }

        // Categoria sugerida -> Categoria entity (o null)
        Categoria categoria = null;
        if (dto.getCategoriaSugerencia() != null && !dto.getCategoriaSugerencia().isBlank()) {
            categoria = categoriaRepo.buscarOCrearPorNombre(dto.getCategoriaSugerencia());
        }

        // Etiquetas sugeridas -> List<Etiqueta>
        List<Etiqueta> etiquetas = new ArrayList<>();
        if (dto.getEtiquetasSugerencia() != null) {
            for (String nombreEt : dto.getEtiquetasSugerencia()) {
                Etiqueta e = etiquetasRepo.buscarOCrearPorNombre(nombreEt);
                if (e != null) etiquetas.add(e);
            }
        }

        SugerenciaDeCambio solicitud = new SugerenciaDeCambio(
                dto.getFechaSugerencia(),
                dto.getDescripcionSolicitud(),
                hecho,
                dto.getTituloSugerencia(),
                etiquetas,
                dto.getFechaSucesoSugerencia(),
                dto.getHoraSucesoSugerencia(),
                categoria,
                dto.getDescripcionSugerencia()
        );

        log.info("Sugerencia creada ok hechoId={} hechoTitulo=\"{}\"",
                dto.getIdHecho(), safe(hecho.getTitulo()));

        sugerenciasRepository.add(solicitud);
        context.status(201);
    }

    private String safe(String s) {
        if (s == null) return "-";
        return s.length() > 120 ? s.substring(0, 120) + "..." : s;
    }

}
