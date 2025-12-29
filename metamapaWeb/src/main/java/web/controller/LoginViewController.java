package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginViewController {
    @GetMapping("/api/auth/login")
    public String loginView() {
        return "login/login";
    }
}