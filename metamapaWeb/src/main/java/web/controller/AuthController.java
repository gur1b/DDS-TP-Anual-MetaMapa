package web.controller;

import web.config.RutasProperties;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final WebClient metamapaApi;

    public AuthController(RutasProperties props) {
        // props.getBaseUrl() debe ser: http://localhost:8081/core/api/public
        this.metamapaApi = WebClient.create(props.getBaseUrl());
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<?>> login(@RequestParam("correo") String correo,
                                         @RequestParam("contrasena") String contrasena) {

        return metamapaApi.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/usuarios/buscar")
                        .queryParam("correo", correo)
                        .build())
                .retrieve()
                .bodyToMono(UsuarioDTO.class)
                .map(usuario -> {
                    if (usuario.getContrasena() != null && BCrypt.checkpw(contrasena, usuario.getContrasena())) {
                        return ResponseEntity.ok(usuario);
                    } else {
                        return ResponseEntity.status(401).body("Contraseña incorrecta");
                    }
                })
                .onErrorResume(err -> Mono.just(ResponseEntity.status(404).body("Usuario no encontrado")));
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<String>> register(@RequestParam("nombre") String nombre,
                                                 @RequestParam("correo") String correo,
                                                 @RequestParam("contrasena") String contrasena,
                                                 @RequestParam("fechaNacimiento")
                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaNacimiento
    ) {

        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();

        // 1) Verificar si existe
        return metamapaApi.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/usuarios/buscar")
                        .queryParam("correo", correo)
                        .build())
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return Mono.just(ResponseEntity.status(409).body("El correo ya está registrado"));
                    }

                    //No existe -> registrar
                    UsuarioDTO nuevo = new UsuarioDTO(nombre, correo, "USER", contrasena, fechaNacimiento);

                    return metamapaApi.post()
                            .uri("/usuarios/registrar")
                            .bodyValue(nuevo)
                            .exchangeToMono(resp -> {
                                if (resp.statusCode().is2xxSuccessful()) {
                                    return Mono.just(ResponseEntity.status(201).body("OK"));
                                }
                                return resp.bodyToMono(String.class)
                                        .defaultIfEmpty("Error registrando en CORE")
                                        .map(msg -> ResponseEntity.status(resp.statusCode()).body(msg));
                            });

                });
    }


    public static class UsuarioDTO {
        private Integer id;
        private String nombre;
        private String correo;
        private String rol;
        private String contrasena;
        private LocalDate fechaNacimiento; // ISO yyyy-MM-dd
        private String foto;

        public UsuarioDTO() {}

        public UsuarioDTO(String nombre, String correo, String rol, String contrasena, LocalDate fechaNacimiento) {
            this.nombre = nombre;
            this.correo = correo;
            this.rol = rol;
            this.contrasena = contrasena;
            this.fechaNacimiento = fechaNacimiento;
        }

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }

        public String getRol() { return rol; }
        public void setRol(String rol) { this.rol = rol; }

        public String getContrasena() { return contrasena; }
        public void setContrasena(String contrasena) { this.contrasena = contrasena; }

        public LocalDate getFechaNacimiento() { return fechaNacimiento; }
        public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

        public String getFoto() { return foto; }
        public void setFoto(String foto) { this.foto = foto; }
    }
}
