package core.api.handlers.usuario;

import core.models.entities.usuario.Usuario;
import core.models.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostRegistrarUsuarioHandler implements Handler {
    private static final Logger log =
            LoggerFactory.getLogger(PostRegistrarUsuarioHandler.class);
    private final UsuarioRepository usuarioRepository = UsuarioRepository.getInstance();

    @Override
    public void handle(@NotNull Context ctx) {

        Usuario usuario = ctx.bodyAsClass(Usuario.class);

        log.info("Intentando registrar usuario correo={}",
                usuario != null ? usuario.getCorreo() : "null");

        if (usuario == null
                || usuario.getCorreo() == null
                || usuario.getNombre() == null) {
            log.warn("Datos de usuario incompletos");
            ctx.status(400).result("Faltan datos obligatorios del usuario");
            return;
        }

        // Hash de la contraseña si viene presente
        if (usuario.getContrasena() != null && !usuario.getContrasena().isBlank()) {
            String hashed = BCrypt.hashpw(usuario.getContrasena(), BCrypt.gensalt());
            usuario.setContrasena(hashed);
        }

        boolean existe = usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent();
        log.debug("Chequeo de existencia usuario correo={} existe={}",
                usuario.getCorreo(), existe);

        if (existe) {
            log.warn("Intento de registro con correo ya existente correo={}",
                    usuario.getCorreo());
            ctx.status(409).result("El correo ya está registrado");
            return;
        }

        try {
            usuarioRepository.add(usuario);
            log.info("Usuario registrado correctamente correo={}",
                    usuario.getCorreo());
            ctx.status(201).json(usuario);
        } catch (Exception e) {
            log.error("Error al registrar usuario correo={}",
                    usuario.getCorreo(), e);
            ctx.status(500).result("Error al registrar usuario");
        }
    }
}
