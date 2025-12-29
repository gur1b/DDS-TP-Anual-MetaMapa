package web.controller;

import web.dto.FuenteDTO;
import web.http.MultipartInputStreamFileResource;
import web.service.FuenteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import web.service.AdminService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PanelDeControlFuente {
    private final FuenteService fuenteService;
    private final AdminService adminService;
    private final RestTemplate restTemplate;

    @Value("${metamapa.api.admin-base-url}")
    private String coreBaseUrl;

    @Autowired
    public PanelDeControlFuente(FuenteService fuenteService, AdminService adminService, RestTemplate restTemplate) {
        this.fuenteService = fuenteService;
        this.adminService = adminService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/admin/fuentes")
    public String home(Model model, Authentication authentication, RedirectAttributes ra) {
        if (!adminService.isAdmin(authentication)) {
            ra.addFlashAttribute("toastError", "No podes ingresar porque no sos admin :v");
            return "redirect:/";
        }
        model.addAttribute("listaDeFuentes", fuenteService.getAll());
        return "panelDeControl/panelDeControlFUENTES";
    }

    @PostMapping("/admin/fuentes")
    public String crearFuente(
            @RequestParam("nombre") String nombre,
            @RequestParam("strategyTipoConexion") String strategyTipoConexion,
            @RequestParam(value = "link", required = false) String link,
            @RequestParam(value = "archivoCsv", required = false) MultipartFile archivoCsv,
            RedirectAttributes ra
    ) {
        try {
            // 1) Crear fuente en el core (que a su vez llama al cargador)
            Map<String, Object> body = new HashMap<>();
            body.put("nombre", nombre);
            // CSV no necesita link; API REST y BIBLIOTECA sí
            if ("CSV".equalsIgnoreCase(strategyTipoConexion)) {
                body.put("link", null);
            } else {
                body.put("link", link);
            }
            body.put("strategyTipoConexion", strategyTipoConexion);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> requestEntity =
                    new HttpEntity<>(body, headers);

            ResponseEntity<FuenteDTO> respCore =
                    restTemplate.postForEntity(
                            coreBaseUrl + "/fuentes",   // ojo: coincide con la ruta que registres en Javalin
                            requestEntity,
                            FuenteDTO.class
                    );

            if (!respCore.getStatusCode().is2xxSuccessful() || respCore.getBody() == null) {
                ra.addFlashAttribute("popupError", "No se pudo crear la fuente en el core");
                return "redirect:/admin/fuentes";
            }

            // ESTE ES EL ID DEL CARGADOR, QUE DEVOLVIÓ EL CORE
            Integer idRemoto = respCore.getBody().id();

            // 2) Si es CSV y vino archivo, subir CSV al core para que lo reenvíe al cargadorEstatica
            if ("CSV".equalsIgnoreCase(strategyTipoConexion)
                    && archivoCsv != null && !archivoCsv.isEmpty()) {

                HttpHeaders csvHeaders = new HttpHeaders();
                csvHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

                MultiValueMap<String, Object> csvBody = new LinkedMultiValueMap<>();
                csvBody.add("archivoCsv", new MultipartInputStreamFileResource(
                        archivoCsv.getInputStream(), archivoCsv.getOriginalFilename()));

                HttpEntity<MultiValueMap<String, Object>> csvRequest =
                        new HttpEntity<>(csvBody, csvHeaders);

                ResponseEntity<String> respCsv = restTemplate.postForEntity(
                        coreBaseUrl + "/fuentes/" + idRemoto + "/csv",  // llama al PostFuenteCSVHandler
                        csvRequest,
                        String.class
                );

                if (!respCsv.getStatusCode().is2xxSuccessful()) {
                    ra.addFlashAttribute("popupError",
                            "La fuente se creó en el cargador, pero falló la subida del CSV");
                    return "redirect:/admin/fuentes";
                }
            }

            ra.addFlashAttribute("popupOk", "Fuente creada correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("popupError", "Error al crear la fuente");
        }

        return "redirect:/admin/fuentes?ok";
    }

    @PostMapping("/admin/fuentes/{id}/eliminar")
    @ResponseBody
    public ResponseEntity<Void> eliminarFuente(@PathVariable("id") Integer id) {
        try {
            fuenteService.eliminarFuente(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
