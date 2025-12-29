package web.controller;

import web.dto.ColeccionDTO;
import web.dto.HechoDTO;
import web.dto.Provincia;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import web.service.ColeccionService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class ColeccionesController {
    private final ColeccionService coleccionService;


    public ColeccionesController(ColeccionService coleccionService) {
        this.coleccionService = coleccionService;
    }

    @GetMapping("/colecciones") // Esta es la URL que usará el botón
    public String navegarColecciones(Model model) {

        // (Opcional) Aquí podés buscar datos en tu 'core' y pasarlos a la vista
        model.addAttribute("listaDeColecciones", coleccionService.getAll());

        // Esto le dice a Thymeleaf que busque el archivo:
        // "src/main/resources/templates/navegarColecciones/navegarColecciones.html"
        return "navegarColecciones/navegarColecciones";
    }

    @GetMapping("/colecciones/{id}")
    public String detalle(
            @PathVariable("id") Integer id,
            @RequestParam(value = "modo", required = false) String modoParam,
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam(value = "etiqueta", required = false) String etiqueta,
            @RequestParam(value = "categoria", required = false) String categoria,
            @RequestParam(value = "provincia", required = false) String provinciaParam,
            @RequestParam(value = "soloMultimedia", required = false) Boolean soloMultimedia,
            @RequestParam(value = "fechaDesdeSuceso", required = false) String fechaDesdeSuceso,
            @RequestParam(value = "fechaHastaSuceso", required = false) String fechaHastaSuceso,
            @RequestParam(value = "fechaDesdeCarga", required = false) String fechaDesdeCarga,
            @RequestParam(value = "fechaHastaCarga", required = false) String fechaHastaCarga,
            @RequestParam(value = "horaDesdeSuceso", required = false) String horaDesdeSuceso,
            @RequestParam(value = "horaHastaSuceso", required = false) String horaHastaSuceso,
            Model model
    ) {
        ColeccionDTO coleccion = coleccionService.getById(id);

        String modoPredeterminado = coleccion.modoDeNavegacion();
        String modoActual = (modoParam != null) ? modoParam : modoPredeterminado;

        if (modoParam != null) {
            if (Objects.equals(modoPredeterminado, "IRRESTRICTA") && Objects.equals(modoParam, "CURADA")) {
                modoActual = "IRRESTRICTA";
            }
            if (!(modoParam.equals("CURADA") || modoParam.equals("IRRESTRICTA"))) {
                modoActual = modoPredeterminado;
            }
        }

        // Aquí conviertes el string de la provincia al enum Provincia
        Provincia provincia = Provincia.fromString(provinciaParam);

        // IMPORTANTE: pasas provincia.name() si en el service espera un String, o directo el enum si acepta Provincia
        List<HechoDTO> hechos = coleccionService.getHechosFiltradosDeColeccion(
                id, modoActual, titulo, descripcion, etiqueta, categoria,
                provincia != null ? provincia.name() : null,
                soloMultimedia, fechaDesdeSuceso, fechaHastaSuceso, fechaDesdeCarga, fechaHastaCarga, horaDesdeSuceso, horaHastaSuceso
        );

        Map<String, Object> param = new HashMap<>();
        param.put("titulo", titulo);
        param.put("descripcion", descripcion);
        param.put("etiqueta", etiqueta);
        param.put("categoria", categoria);
        param.put("provincia", provinciaParam);
        param.put("soloMultimedia", soloMultimedia);
        param.put("fechaDesdeSuceso", fechaDesdeSuceso);
        param.put("fechaHastaSuceso", fechaHastaSuceso);
        param.put("fechaDesdeCarga", fechaDesdeCarga);
        param.put("fechaHastaCarga", fechaHastaCarga);
        param.put("horaDesdeSuceso", horaDesdeSuceso);
        param.put("horaHastaSuceso", horaHastaSuceso);
        param.put("modo", modoActual);

        model.addAttribute("coleccion", coleccion);
        model.addAttribute("hechos", hechos);
        model.addAttribute("modoActual", modoActual);
        model.addAttribute("param", param);

        model.addAttribute("categorias", coleccionService.getCategorias());
        model.addAttribute("etiquetas", coleccionService.getEtiquetas());
        model.addAttribute("provincias", Provincia.values());

        return "verColeccion/verColeccion";
    }


}
