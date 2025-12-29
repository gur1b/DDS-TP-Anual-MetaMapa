package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Model model, @org.springframework.web.bind.annotation.RequestParam(value = "login", required = false) String login) {
        if ("success".equals(login)) {
            model.addAttribute("toastSuccess", "¡Bienvenido! Has iniciado sesión correctamente.");
        }
        return "home/home";
    }
}
