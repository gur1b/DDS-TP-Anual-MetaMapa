package core.models.agregador.normalizador;

import core.models.entities.hecho.Contribuyente;
import core.models.repository.ContribuyentesRepository;

import java.util.Optional;

public class NormalizadorContribuyente {

    private static NormalizadorContribuyente instance;
    private static ContribuyentesRepository contribuyenteRepository = ContribuyentesRepository.getInstance();

    public static NormalizadorContribuyente getInstance() {
        if (instance == null) instance = new NormalizadorContribuyente();

        return instance;
    }
    //NOMBRE CSV O DE API
    //DINAMICO LE LLEGA UN ID
    public Contribuyente obtenerContribuyente(String raw){
        if (raw == null || raw.isBlank()) return null;

        String trimmed = raw.trim();

        if(esId(raw)){
            Integer id = Integer.parseInt(trimmed);

            // Buscar en la base de datos el contribuyente con ese ID
            Optional<Contribuyente> opt = Optional.ofNullable(contribuyenteRepository.findById(id));

            // Si existe, lo devolvemos; si no, devolvemos null
            return opt.orElse(null);
        }

        Contribuyente c = new Contribuyente();

        if (trimmed.contains("@")) {
            // Es un mail
            c.setMail(trimmed);
        } else {
            // Es un nombre
            c.setNombre(trimmed);
        }

        return c;
    }

    public boolean esId(String s) {
        return s != null && s.matches("\\d+");
    }


}
