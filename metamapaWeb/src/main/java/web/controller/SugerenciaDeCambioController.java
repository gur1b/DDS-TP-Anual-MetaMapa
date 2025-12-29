package web.controller;

import web.service.ReportarService;
import web.service.SugerenciaDeCambioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SugerenciaDeCambioController {

    private final SugerenciaDeCambioService sugerenciaService;
    private final ReportarService reportarService;
    private final ObjectMapper objectMapper;

    public SugerenciaDeCambioController(SugerenciaDeCambioService service, ReportarService reportarService, ObjectMapper objectMapper) {
        this.sugerenciaService = service;
        this.reportarService = reportarService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/misHechos/{idHecho}/sugerencias")
    @ResponseBody
    public ResponseEntity<?> obtener(@PathVariable Integer idHecho) throws JsonProcessingException {
        String categoriasJson = reportarService.getCategorias();

        // lo parseamos a List<String>
        List<String> categorias = objectMapper.readValue(
                categoriasJson,
                new TypeReference<List<String>>() {
                }
        );

        //model.addAttribute("categorias", categorias);
        return ResponseEntity.ok().build();
    }
}
