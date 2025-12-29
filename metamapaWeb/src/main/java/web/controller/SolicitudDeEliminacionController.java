package web.controller;

import web.dto.ColeccionDTO;
import web.dto.HechoDTO;
import web.service.ColeccionService;
import web.service.HechoService;
import web.service.SolicitudesEliminacionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class SolicitudDeEliminacionController {

    private final ColeccionService coleccionService;
    private final HechoService hechoService;
    private  final SolicitudesEliminacionService solicitudesEliminacionService;

    public SolicitudDeEliminacionController(ColeccionService coleccionService, HechoService hechoService, SolicitudesEliminacionService solicitudesEliminacionService) {
        this.coleccionService = coleccionService;
        this.hechoService = hechoService;
        this.solicitudesEliminacionService = solicitudesEliminacionService;
    }

    // 1. Cambia el GetMapping para que acepte un 'hash'
    @GetMapping("/solicitudEliminacion/{id}")
    public String home(@PathVariable("id") Integer id, Model model) { // Recibe el ID del hecho

        try {
            // 3. Busca la colección global (esto sigue igual)
            List<ColeccionDTO> todasLasColecciones = coleccionService.getAll();

            Optional<Integer> idGlobalOpt = todasLasColecciones.stream()
                    .filter(coleccion -> coleccion.criterioDePertenencia() == null)
                    .map(ColeccionDTO::id)
                    .findFirst();

            // Buscar el hecho por ID en la colección global o en todos los hechos
            HechoDTO hechoBuscado = null;
            if (idGlobalOpt.isEmpty()) {
                List<HechoDTO> todosLosHechos = hechoService.getAll();
                if (todosLosHechos != null) {
                    hechoBuscado = todosLosHechos.stream()
                            .filter(hecho -> hecho.id() != null && hecho.id().equals(id))
                            .findFirst().orElse(null);
                }
            } else {
                List<HechoDTO> hechosGlobales = coleccionService.getHechosDeColeccion(idGlobalOpt.get());
                hechoBuscado = hechosGlobales.stream()
                        .filter(hecho -> hecho.id() != null && hecho.id().equals(id))
                        .findFirst().orElse(null);
            }
            if (hechoBuscado != null) {
                model.addAttribute("hecho", hechoBuscado);
            } else {
                System.err.println("Error: El hecho con ID " + id + " no se encontró.");
                return "redirect:/mapa";
            }

        } catch (Exception e) {
            System.err.println("Error en SolicitudDeEliminacionController: " + e.getMessage());
            return "redirect:/mapa";
        }

        return "solicitudEliminacion/solicitudEliminacion";
    }

    // POST que recibe el form y llama a la API NORMAL (8081)
    @PostMapping("/solicitudes/eliminacion")
    public String procesarSolicitudDeEliminacion(
            @RequestParam("idHecho") Integer idHecho,    // hidden en el form
            @RequestParam("descripcion") String descripcion
    ) {
        boolean ok = solicitudesEliminacionService.crear(idHecho, descripcion);
        if (ok) {
            return "redirect:/mapa?solicitudEliminacion=ok";
        } else {
            return "redirect:/mapa?solicitudEliminacion=error";
        }
    }

}