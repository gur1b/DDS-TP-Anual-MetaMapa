package web.controller;
import web.dto.CriterioDTO;
import web.service.FuenteService;
import web.service.ColeccionService;
import web.service.ReportarService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import web.service.AdminService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

@Controller
public class PanelDeControlController {
    private final ColeccionService coleccionService;
    private final FuenteService fuenteService;
    private final ReportarService reportarService;
    private final ObjectMapper objectMapper;  
    private final AdminService adminService;

    @Autowired
    public PanelDeControlController(ColeccionService coleccionService, FuenteService fuenteService, AdminService adminService, ObjectMapper objectMapper, ReportarService reportarService) {
        this.coleccionService = coleccionService;
        this.fuenteService = fuenteService;
        this.adminService = adminService;
        this.objectMapper = objectMapper;
        this.reportarService = reportarService;
    }

    // 1. Inyectamos los valores desde el properties
    @Value("${app.url.graphql-playground}")
    private String graphqlUrl;

    @Value("${app.url.grafana-dashboard}")
    private String grafanaUrl;

    @GetMapping("/admin/colecciones")
    public String home(Model model, Authentication authentication, RedirectAttributes ra) throws JsonProcessingException {
        if (!adminService.isAdmin(authentication)) {
            ra.addFlashAttribute("toastError", "No podes ingresar porque no sos admin :v");
            return "redirect:/";
        }
        model.addAttribute("listaDeColecciones", coleccionService.getAll());
        model.addAttribute("listaDeFuentes", fuenteService.getAll());
        // 2. Agregamos las URLs al modelo existente
        model.addAttribute("graphqlUrl", graphqlUrl);
        model.addAttribute("grafanaUrl", grafanaUrl);

        String categoriasJson = reportarService.getCategorias();
        // lo parseamos a List<String>
        List<String> categorias = objectMapper.readValue(
                categoriasJson,
                new TypeReference<List<String>>() {}
        );
        model.addAttribute("categorias", categorias);
        return "panelDeControl/panelDeControl";
    }

    @PostMapping("/admin/colecciones/{id}/eliminar")
    public String eliminar(@PathVariable("id") Integer id, RedirectAttributes ra) {
        boolean ok = coleccionService.deleteById(id);
        if (ok) {
            ra.addFlashAttribute("toastOk", "Colección eliminada.");
        } else {
            ra.addFlashAttribute("toastError", "No se pudo eliminar la colección.");
        }
        return "redirect:/admin";
    }

    //VER
    @PatchMapping("/admin/colecciones/{id}/modificar")
    @ResponseBody
    public ResponseEntity<?> modificarColeccionPatch(
            @PathVariable("id") Integer id,
            @RequestBody Map<String, Object> req
    ) {
        String titulo = req.get("titulo") != null ? req.get("titulo").toString() : null;
        String desc = req.get("descripcionColeccion") != null ? req.get("descripcionColeccion").toString() : null;

        List<Integer> fuentes = req.get("fuentes") instanceof List<?> list
                ? ((List<?>) list).stream().map(o -> Integer.parseInt(o.toString())).toList()
                : null; // Mejor usar null si no viene, para que el servicio decida qué hacer (o List.of() si quieres vaciarlo)

        String algoritmoConsenso = req.get("algoritmoConsenso") != null ? req.get("algoritmoConsenso").toString() : null;
        String modoDeNavegacion = req.get("modoDeNavegacion") != null ? req.get("modoDeNavegacion").toString() : null;
        List<CriterioDTO> criterios = (List<CriterioDTO>) req.get("criterioDePertenencia");

        // Llamamos al servicio pasando el nuevo parámetro
        boolean ok = coleccionService.patchColeccion(id, titulo, desc, fuentes, algoritmoConsenso, modoDeNavegacion, criterios);

        if (ok) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo actualizar");
    }

    @PostMapping("/admin/colecciones/crear")
    @ResponseBody
    public ResponseEntity<?> crearColeccion(@RequestBody Map<String, Object> payload) {

        // Extraemos los datos del JSON que mandó el JavaScript
        String titulo = (String) payload.get("titulo");
        String descripcion = (String) payload.get("descripcionColeccion");

        boolean ok = coleccionService.crearColeccion(payload);

        if (ok) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo crear la colección en el servicio core.");
        }
    }

    @PostMapping("/ejecutar-agregacion")
    public String ejecutarAgregacion(RedirectAttributes ra) {

        try {
            coleccionService.ejecutarAgregacion();
            ra.addFlashAttribute("popupSuccess", "Servicio de agregación ejecutado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("popupError", "Error al ejecutar el servicio de agregación.");
        }
        return "redirect:/admin/colecciones";
    }
}

