package web.controller;

import web.dto.HechoDTO;
import web.dto.SugerenciaDTO;
import web.service.HechoService;
import web.service.ReportarService;
import web.service.SugerenciaDeCambioService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class MisHechosMapaController {
    private final HechoService hechoService;
    private final ReportarService reportarService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SugerenciaDeCambioService sugerenciaService;

    public MisHechosMapaController(HechoService hechoService, ReportarService reportarService, SugerenciaDeCambioService sugerenciaService) {
        this.hechoService = hechoService;
        this.reportarService = reportarService;
        this.sugerenciaService = sugerenciaService;
    }

    @GetMapping("/mis-hechos/mapa")
    public String verMisHechosEnMapa(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/";
        }

        Object principal = authentication.getPrincipal();

        String email = null;
        String nombreCompleto = null;

        if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User oAuth2User) {
            nombreCompleto = oAuth2User.getAttribute("name");
            email = oAuth2User.getAttribute("email");
        } else if (principal instanceof java.util.Map attributes) {
            nombreCompleto = (String) attributes.get("name");
            email = (String) attributes.get("email");
        }

        // Si no tenemos mail no podemos pedir los hechos filtrados por contribuyente!!!
        if (email == null || email.isBlank()) {
            System.out.println("[MIS-HECHOS] No se encontró email del usuario autenticado");
            model.addAttribute("hechos", java.util.List.of());
        } else {
            List<HechoDTO> hechos = hechoService.getByContribuyente(email);
            model.addAttribute("hechos", hechos);
        }

        model.addAttribute("nombreUsuario", nombreCompleto);

        try {
            String categoriasJson = reportarService.getCategorias();
            java.util.List<String> categorias = objectMapper.readValue(
                    categoriasJson,
                    new TypeReference<java.util.List<String>>() {}
            );
            model.addAttribute("categorias", categorias);
        } catch (Exception e) {
            model.addAttribute("categorias", java.util.List.of());
        }

        return "misHechos/misHechosMapa";
    }

    // POST /misHechos/{idHecho}/sugerencias
    @PostMapping(path = "/misHechos/{idHecho}/sugerencias", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<?> crearSugerencia(
            @RequestBody SugerenciaDTO req) {
        sugerenciaService.crear(req);
        return ResponseEntity.ok().build();
    }
}