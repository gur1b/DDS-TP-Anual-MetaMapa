package web.controller;

import web.service.AdminService;
import web.service.SugerenciaDeCambioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PanelDeControlSugerenciasController {
    private final SugerenciaDeCambioService sugerenciasService;
    private final AdminService adminService;

    @Autowired
    public PanelDeControlSugerenciasController(SugerenciaDeCambioService sugerenciasService, AdminService adminService) {
        this.sugerenciasService = sugerenciasService;
        this.adminService = adminService;
    }

    @GetMapping("/admin/sugerenciasDeCambio")
    public String home(Model model, Authentication authentication, HttpServletRequest request, RedirectAttributes ra) {
        if (!adminService.isAdmin(authentication)) {
            ra.addFlashAttribute("toastError", "No podes ingresar porque no sos admin :v");
            return "redirect:/";
        }
        model.addAttribute("listaDeSugerencias", sugerenciasService.getAll());
        return "panelDeControl/panelDeControlSugerencias";
    }


    @PostMapping("/admin/sugerenciasDeCambio/{id}/rechazar")
    public String rechazar(@PathVariable("id") Integer id, RedirectAttributes ra, Authentication authentication) {
        if (!adminService.isAdmin(authentication)) {
            ra.addFlashAttribute("toastError", "No podes ingresar porque no sos admin :v");
            return "redirect:/";
        }
        boolean ok = sugerenciasService.rechazar(id);
        if (ok) {
            ra.addFlashAttribute("toastOk", "Solicitud rechazada.");
        } else {
            ra.addFlashAttribute("toastError", "No se pudo realizar la operación.");
        }
        return "redirect:/admin/sugerenciasDeCambio";
    }

    @PostMapping("/admin/sugerenciasDeCambio/{id}/aceptar")
    public String aceptar(@PathVariable("id") Integer id, RedirectAttributes ra, Authentication authentication) {
        if (!adminService.isAdmin(authentication)) {
            ra.addFlashAttribute("toastError", "No podes ingresar porque no sos admin :v");
            return "redirect:/";
        }
        boolean ok = sugerenciasService.aceptar(id);
        if (ok) {
            ra.addFlashAttribute("toastOk", "Solicitud aceptada.");
        } else {
            ra.addFlashAttribute("toastError", "No se pudo realizar la operación.");
        }
        return "redirect:/admin/sugerenciasDeCambio";
    }
}
