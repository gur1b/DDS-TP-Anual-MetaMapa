package web.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private static final Logger log =
            LoggerFactory.getLogger(OAuth2LoginSuccessHandler.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private final String metamapaApi;

    public OAuth2LoginSuccessHandler(RutasProperties props) {
        this.metamapaApi = props.getBaseUrl() + "/usuarios";
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        try {
            if (authentication.getPrincipal() instanceof DefaultOAuth2User oAuth2User) {

                String email = (String) oAuth2User.getAttributes().get("email");
                String name = (String) oAuth2User.getAttributes().get("name");
                String picture = (String) oAuth2User.getAttributes().get("picture");

                log.info("Login SSO exitoso con Google email={}", email);

                if (email != null) {

                    // ============ 1) BUSCAR USUARIO EN EL CORE ============
                    UsuarioDTO existing = null;
                    try {
                        String url = metamapaApi + "/buscar?correo={correo}";
                        ResponseEntity<UsuarioDTO> resp =
                                restTemplate.getForEntity(url, UsuarioDTO.class, email);

                        if (resp.getStatusCode().is2xxSuccessful()) {
                            existing = resp.getBody();
                        }
                    } catch (HttpClientErrorException.NotFound ex) {
                        // 404 -> usuario no existe en el CORE (esto es lo esperado a veces)
                        log.info("Usuario SSO no encontrado en CORE, se creará email={}", email);
                    } catch (RestClientResponseException ex) {
                        log.error("Error HTTP consultando CORE email={} status={}",
                                email, ex.getStatusText(), ex);
                    } catch (Exception ex) {
                        log.error("Error inesperado consultando CORE email={}", email, ex);
                    }

                    // ============ 2) SI NO EXISTE, LO CREAMOS EN EL CORE ============
                    if (existing == null) {
                        log.info("Creando usuario en CORE vía SSO email={}", email);

                        UsuarioDTO nuevo = new UsuarioDTO(
                                name,
                                email,
                                "USER",
                                null
                        );

                        try {
                            String url = metamapaApi + "/registrar";
                            ResponseEntity<UsuarioDTO> resp =
                                    restTemplate.postForEntity(url, nuevo, UsuarioDTO.class);

                            if (resp.getStatusCode() == HttpStatus.CREATED
                                    || resp.getStatusCode().is2xxSuccessful()) {
                                log.info("Usuario creado en CORE vía SSO email={}", email);
                            } else {
                                log.warn("CORE devolvió estado no exitoso al registrar email={} status={}",
                                        email, resp.getStatusCode());
                            }
                        } catch (RestClientResponseException ex) {
                            log.error("Error HTTP al crear usuario en CORE email={} status={}",
                                    email, ex.getStatusText(), ex);
                        } catch (Exception ex) {
                            log.error("Error inesperado al crear usuario en CORE email={}", email, ex);
                        }
                    }
                } else {
                    log.info("Usuario SSO ya existía en CORE email={}", email);
                }
            }
        } catch (Exception e) {
            log.error("Error en successHandler SSO (no corta el login)", e);
        }

        response.sendRedirect("/mapa");
    }

    // DTO interno solo para hablar con el CORE
    public static class UsuarioDTO {
        private String nombre;
        private String correo;
        private String rol;
        private String contrasena;
        private LocalDate localDate;

        public UsuarioDTO() {}

        public UsuarioDTO(String nombre, String correo, String rol, String contrasena) {
            this.nombre = nombre;
            this.correo = correo;
            this.rol = rol;
            this.contrasena = contrasena;
            localDate = null;
        }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }

        public String getRol() { return rol; }
        public void setRol(String rol) { this.rol = rol; }

        public String getContrasena() { return contrasena; }
        public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    }
}
