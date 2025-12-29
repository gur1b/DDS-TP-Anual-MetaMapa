package core.models.agregador.normalizador;

import core.models.agregador.HechoAIntegrarDTO;
import core.models.entities.fuentes.TipoFuente;
import core.models.entities.hecho.Categoria;
import core.models.entities.hecho.Coordenadas;
import core.models.repository.CategoriaRepository;
import core.models.repository.CoordenadasRepository;

import java.util.List;
import java.util.Objects;

public class NormalizadorCoordenada {

    private static NormalizadorCoordenada instance;

    public static NormalizadorCoordenada getInstance() {
        if (instance == null) instance = new NormalizadorCoordenada();

        return instance;
    }

    public Coordenadas obtenerCoordenadas(String latitudString, String longitudString) {
        double latitud = Double.parseDouble(latitudString);
        double longitud = Double.parseDouble(longitudString);
        Coordenadas ubicacion = new Coordenadas(latitud, longitud);
        return ubicacion;
    }
}