package web.dto;

public record FuenteDTO( Integer id,
                         String nombre,
                         String link,
                         String tipoFuente,
                         String strategyTipoConexion) {}
