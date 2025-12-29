package web.controller;

import web.dto.HechoDTO;
import web.service.ColeccionService;
import web.service.HechoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class VerMapaController {

    private final ColeccionService coleccionService;
    private final HechoService hechoService;

    public VerMapaController(ColeccionService coleccionService, HechoService hechoService) {
        this.coleccionService = coleccionService;
        this.hechoService = hechoService;
    }

    @GetMapping("/mapa")
    public String verMapaGlobal(Model model) {
        try {
            List<HechoDTO> hechosGlobales = hechoService.getAll();
            model.addAttribute("hechos", Optional.ofNullable(hechosGlobales).orElse(List.of()));
        } catch (Exception e) {
            System.err.println("Error en verMapaGlobal: " + e.getMessage());
            model.addAttribute("hechos", List.of());
        }
        return "verSuceso/verSuceso";
    }
}