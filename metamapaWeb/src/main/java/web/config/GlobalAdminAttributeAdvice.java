package web.config;

import web.service.AdminService;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalAdminAttributeAdvice {
    private final AdminService adminService;

    public GlobalAdminAttributeAdvice(AdminService adminService) {
        this.adminService = adminService;
    }

    @ModelAttribute
    public void addAdminAttribute(Model model, Authentication authentication) {
        boolean esAdmin = adminService.isAdmin(authentication);
        model.addAttribute("esAdmin", esAdmin);
    }
}
