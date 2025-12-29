package web.service;

import web.config.AdminEmailsConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final AdminEmailsConfig adminEmailsConfig;

    public AdminService(AdminEmailsConfig adminEmailsConfig) {
        this.adminEmailsConfig = adminEmailsConfig;
    }

    public boolean isAdmin(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        Object principal = authentication.getPrincipal();
        String email = null;
        if (principal instanceof OAuth2User oAuth2User) {
            email = (String) oAuth2User.getAttributes().get("email");
        } else if (principal instanceof org.springframework.security.core.userdetails.User user) {
            email = user.getUsername();
        } else if (principal instanceof String str) {
            email = str;
        } else if (principal instanceof java.util.Map<?,?> map) {
            Object mailObj = map.get("email");
            if (mailObj instanceof String) {
                email = (String) mailObj;
            }
        }
        return email != null && adminEmailsConfig.getAdminEmails().contains(email);
    }
}
