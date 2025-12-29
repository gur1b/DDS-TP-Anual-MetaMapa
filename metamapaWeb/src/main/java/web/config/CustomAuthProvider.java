package web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthProvider.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private final String metamapaApi;

    public CustomAuthProvider(RutasProperties props) {
        this.metamapaApi = props.getBaseUrl() + "/usuarios";
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String rawCorreo = authentication.getName();
        String contrasena = authentication.getCredentials().toString();

        if (rawCorreo == null || rawCorreo.trim().isEmpty()) {
            log.warn("Intento de autenticación con correo inválido");
            throw new BadCredentialsException("Correo inválido");
        }

        String correo = rawCorreo.trim().toLowerCase();
        log.info("Intentando autenticar usuario correo={}", correo);

        UsuarioDTO usuario = null;

        // ============ 1) BUSCAR USUARIO EN EL CORE ============
        try {
            String url = metamapaApi + "/buscar?correo={correo}";
            ResponseEntity<UsuarioDTO> resp =
                    restTemplate.getForEntity(url, UsuarioDTO.class, correo);

            if (resp.getStatusCode().is2xxSuccessful()) {
                usuario = resp.getBody();
            }
        } catch (HttpClientErrorException.NotFound ex) {
            // 404 -> usuario no existe en el CORE
            log.warn("Usuario no encontrado en CORE correo={}", correo);
            throw new BadCredentialsException("Usuario no encontrado");
        } catch (RestClientResponseException ex) {
            log.error("Error HTTP consultando CORE correo={} status={}",
                    correo, ex.getStatusText(), ex);
            throw new AuthenticationServiceException("Error al comunicarse con el CORE", ex);
        } catch (Exception ex) {
            log.error("Error inesperado consultando CORE correo={}", correo, ex);
            throw new AuthenticationServiceException("Error al autenticarse contra el CORE", ex);
        }

        if (usuario == null) {
            log.warn("Usuario nulo devuelto por CORE correo={}", correo);
            throw new BadCredentialsException("Usuario no encontrado");
        }

        // ============ 2) VALIDAR CONTRASEÑA ============
        if (usuario.getContrasena() == null || !BCrypt.checkpw(contrasena, usuario.getContrasena())) {
            log.warn("Contraseña incorrecta correo={}", correo);
            throw new BadCredentialsException("Contraseña incorrecta");
        }

        // ============ 3) ARMAR AUTHORITIES Y ATRIBUTOS ============
        String rol = (usuario.getRol() != null) ? usuario.getRol() : "USER";
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase());

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", usuario.getNombre());
        attributes.put("email", usuario.getCorreo());
        attributes.put("picture", usuario.getFoto());

        log.info("Autenticación exitosa correo={} rol={}", correo, rol);

        return new UsernamePasswordAuthenticationToken(
                attributes,
                contrasena,
                Collections.singletonList(authority)
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    // DTO interno para mapear lo que devuelve el CORE
    public static class UsuarioDTO {
        private Integer id;
        private String nombre;
        private String correo;
        private String rol;
        private String foto;
        private String contrasena;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
        }

        public String getRol() {
            return rol;
        }

        public void setRol(String rol) {
            this.rol = rol;
        }

        public String getFoto() {
            return foto;
        }

        public void setFoto(String foto) {
            this.foto = foto;
        }

        public String getContrasena() {
            return contrasena;
        }

        public void setContrasena(String contrasena) {
            this.contrasena = contrasena;
        }
    }

}

