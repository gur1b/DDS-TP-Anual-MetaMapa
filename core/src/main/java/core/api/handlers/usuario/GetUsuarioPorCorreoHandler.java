package core.api.handlers.usuario;

import core.models.entities.usuario.Usuario;
import core.models.repository.UsuarioRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetUsuarioPorCorreoHandler implements Handler {
    UsuarioRepository usuarioRepository = UsuarioRepository.getInstance();
    private static final Logger log =
            LoggerFactory.getLogger(GetUsuarioPorCorreoHandler.class);
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
            String correo = ctx.queryParam("correo");

         log.info("Consulta usuario por correo correo={}", correo);
            if (correo == null) {
                log.warn("Parámetro requerido faltante: correo");
                ctx.status(400).result("Falta el parámetro correo");
                return;
            }

            Usuario usuario = usuarioRepository.findByCorreo(correo).orElse(null);

            if (usuario == null) {
                log.warn("Usuario no encontrado correo={}", correo);
                ctx.status(404).result("Usuario no encontrado");
            } else {
                log.info("Usuario encontrado correo={}", correo);
                ctx.status(200).json(usuario);
            }
        }
    }

