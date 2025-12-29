package web.controller;

import web.dto.HechoDTO;
import web.service.ColeccionService;
import web.service.HechoService;
import web.service.AdminService;
import web.service.ReportarService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class PanelDeControlHechosController {
    @Autowired
    private ReportarService reportarService;

    private final ColeccionService coleccionService;
    private final HechoService hechoService;
    private final AdminService adminService;

    @Autowired
    public PanelDeControlHechosController(ColeccionService coleccionService, HechoService hechoService, AdminService adminService) {
        this.coleccionService = coleccionService;
        this.hechoService = hechoService;
        this.adminService = adminService;
    }

    // Renderiza Admin Hechos tomando los hechos de la colecciÃ³n global (criterio == null)
    @GetMapping("/admin/hechos")
    public String administrarHechos(Model model, Authentication authentication, RedirectAttributes ra) {
        if (!adminService.isAdmin(authentication)) {
            ra.addFlashAttribute("toastError", "No podes ingresar porque no sos admin");
            return "redirect:/";
        }
        List<HechoDTO> hechosGlobales = hechoService.getAll();
        try {
            String categoriasJson = reportarService.getCategorias();
            com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            List<String> categorias = objectMapper.readValue(
                    categoriasJson,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
            model.addAttribute("categorias", categorias);
        } catch (Exception e) {
            model.addAttribute("categorias", List.of());
        }
        model.addAttribute("listaDeHechos", Optional.ofNullable(hechosGlobales).orElse(List.of()));
        return "panelDeControl/panelDeControlHECHOS";
    }

    // Eliminar por hash (admin API)
    @PostMapping("/admin/hechos/{hash}/eliminar")
    public String eliminarHecho(@PathVariable("hash") String hash, RedirectAttributes ra) {
        boolean ok = hechoService.deleteByHash(hash);
        ra.addFlashAttribute(ok ? "toastOk" : "toastError",
                ok ? "Hecho eliminado." : "No se pudo eliminar el hecho.");
        return "redirect:/admin/hechos";
    }

    // Editar por hash (admin API)
    @PatchMapping("/admin/hechos/{hash}/modificar")
    @ResponseBody
    public ResponseEntity<?> modificarHecho(@PathVariable("hash") String hash,
                                            @RequestBody Map<String, Object> req) {
        String nombre = req.get("nombre") != null ? req.get("nombre").toString() : null;
        String descripcion = req.get("descripcion") != null ? req.get("descripcion").toString() : null;
        String latitud = req.get("latitud") != null ? req.get("latitud").toString() : null;
        String longitud = req.get("longitud") != null ? req.get("longitud").toString() : null;
        String fechaSuceso = req.get("fecha_suceso") != null ? req.get("fecha_suceso").toString() : null;
        String categoria = req.get("categoria") != null ? req.get("categoria").toString() : null;

        List<String> etiquetas = null;
        Object et = req.get("etiquetas");
        if (et instanceof List<?> list) {
            etiquetas = list.stream().map(String::valueOf).toList();
        }

        boolean ok = hechoService.patchByHash(hash, nombre, descripcion, etiquetas, latitud, longitud, fechaSuceso, categoria);
        return ok ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo actualizar el hecho");
    }
}