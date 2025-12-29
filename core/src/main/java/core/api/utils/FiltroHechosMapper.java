package core.api.utils;

import core.api.DTO.FiltroHechoDTO;
import io.javalin.http.Context;

import java.time.LocalDate;

public class FiltroHechosMapper {

    private static volatile FiltroHechosMapper instance;

    public static FiltroHechosMapper getInstance() {
        if (instance == null) {
            synchronized (FiltroHechosMapper.class) {
                if (instance == null) {
                    instance = new FiltroHechosMapper();
                }
            }
        }
        return instance;
    }

    public static FiltroHechoDTO extraerFiltroDeContext(Context context) {
        FiltroHechoDTO filtro = new FiltroHechoDTO();

        filtro.setTitulo(context.queryParam("titulo"));
        filtro.setDescripcion(context.queryParam("descripcion"));
        filtro.setEtiqueta(context.queryParam("etiqueta"));
        filtro.setCategoria(context.queryParam("categoria"));
        filtro.setProvincia(context.queryParam("provincia"));
        filtro.setSoloMultimedia("true".equalsIgnoreCase(context.queryParam("soloMultimedia")));

        // Fechas (suceso)
        String fechaDesdeSuceso = context.queryParam("fechaDesdeSuceso");
        String fechaHastaSuceso = context.queryParam("fechaHastaSuceso");
        if (fechaDesdeSuceso != null && !fechaDesdeSuceso.isEmpty())
            filtro.setFechaDesdeSuceso(LocalDate.parse(fechaDesdeSuceso));
        if (fechaHastaSuceso != null && !fechaHastaSuceso.isEmpty())
            filtro.setFechaHastaSuceso(LocalDate.parse(fechaHastaSuceso));

        // Fechas (carga)
        String fechaDesdeCarga = context.queryParam("fechaDesdeCarga");
        String fechaHastaCarga = context.queryParam("fechaHastaCarga");
        if (fechaDesdeCarga != null && !fechaDesdeCarga.isEmpty())
            filtro.setFechaDesdeCarga(LocalDate.parse(fechaDesdeCarga));
        if (fechaHastaCarga != null && !fechaHastaCarga.isEmpty())
            filtro.setFechaHastaCarga(LocalDate.parse(fechaHastaCarga));

        return filtro;
    }
}