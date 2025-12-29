package core.models.repository.seeders;

import core.models.agregador.normalizador.NormalizadorCategoria;
import core.models.entities.colecciones.criterios.CriterioNombre;
import core.models.entities.fuentes.TipoFuente;
import core.models.entities.hecho.*;
import core.models.repository.CategoriaRepository;
import core.models.repository.ContribuyentesRepository;
import core.models.repository.CoordenadasRepository;
import core.models.repository.HechosRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HexFormat;
import java.util.List;

public class HechosRepositorySeeder {

    private static volatile HechosRepositorySeeder instance;

    public static HechosRepositorySeeder getInstance() {
        if (instance == null) {
            synchronized (HechosRepositorySeeder.class) {
                if (instance == null) {
                    instance = new HechosRepositorySeeder();
                }
            }
        }
        return instance;
    }

    Coordenadas coordenadas1 = new Coordenadas(123.0, 456.0);
    Coordenadas coordenadas2 = new Coordenadas(893.0, 016.0);
    Coordenadas coordenadas3 = new Coordenadas(973.0, 656.0);
    Coordenadas coordenadas4 = new Coordenadas(223.0, 033.0);

    HechosRepository hechosRepository = HechosRepository.getInstance();
    CategoriaRepository categoriaRepository = CategoriaRepository.getInstance();
    CoordenadasRepository coordenadasRepository = CoordenadasRepository.getInstance();
    ContribuyentesRepository contribuyentesRepository = ContribuyentesRepository.getInstance();

    public static String generarHash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(input.getBytes());
            return HexFormat.of().formatHex(hashBytes); // devuelve un string en hex
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void cargarHechosSeeder() {

        Categoria categoriaIncendio = categoriaRepository.buscarPorNombre("Incendio");
        if (categoriaIncendio == null) {
            categoriaIncendio = new Categoria("Incendio");
            categoriaRepository.add(categoriaIncendio);
        }
        Categoria categoriaChoque = categoriaRepository.buscarPorNombre("Choque");
        if (categoriaChoque == null) {
            categoriaChoque = new Categoria("Choque");
            categoriaRepository.add(categoriaChoque);
        }
        Categoria categoriaRobo = categoriaRepository.buscarPorNombre("Robo");
        if (categoriaRobo == null) {
            categoriaRobo = new Categoria("Robo");
            categoriaRepository.add(categoriaRobo);
        }

        Coordenadas coordenadas1 = new Coordenadas(-34.61969316128249, -58.4494697301956);
        Coordenadas coordenadas2 = new Coordenadas(-34.55289162117985, -58.697023577203126);
        Coordenadas coordenadas3 = new Coordenadas(-34.63254438302316, -58.36621806522921);
        Coordenadas coordenadas4 = new Coordenadas(-34.71314181885889, -58.40695807398642);
        Coordenadas coordenadas5 = new Coordenadas(-34.71314181885889, -58.40695807398643);


        //CORDOBA

        //Coordenadas
        Coordenadas coordenadas10 = new Coordenadas(-31.424164939481255, -64.51253034145859);
        Coordenadas coordenadas11 = new Coordenadas(-31.42081334780942, -64.49981512580771);
        Coordenadas coordenadas12 = new Coordenadas(-31.420218194636984, -64.18860387845267);
        Coordenadas coordenadas13 = new Coordenadas(-31.97776455880997, -64.55950663508438);
        Coordenadas coordenadas14 = new Coordenadas(-33.12311719110758, -64.3500117112537);
        Coordenadas coordenadas15 = new Coordenadas(-33.927945800754834, -64.39990066896273);

        coordenadasRepository.add(coordenadas10);
        coordenadasRepository.add(coordenadas11);
        coordenadasRepository.add(coordenadas12);
        coordenadasRepository.add(coordenadas13);
        coordenadasRepository.add(coordenadas14);
        coordenadasRepository.add(coordenadas15);

        //Categorias
        Categoria categoriaEstafa = categoriaRepository.buscarPorNombre("Estafa");
        if (categoriaEstafa == null) {
            categoriaEstafa = new Categoria("Estafa");
            categoriaRepository.add(categoriaEstafa);
        }
        Categoria categoriaVial = categoriaRepository.buscarPorNombre("Accidente vial");
        if (categoriaVial == null) {
            categoriaVial = new Categoria("Accidente vial");
            categoriaRepository.add(categoriaVial);
        }
        Categoria categoriaAmenaza = categoriaRepository.buscarPorNombre("Amenaza");
        if (categoriaAmenaza == null) {
            categoriaAmenaza = new Categoria("Amenaza");
            categoriaRepository.add(categoriaAmenaza);
        }

        //Contribuyentes

        Contribuyente contribuyente10 = new Contribuyente("Carlos", "Pérez", LocalDate.now().minusDays(15));
        Contribuyente contribuyente11 = new Contribuyente("Ana", "García", LocalDate.now().minusDays(10));
        Contribuyente contribuyente12 = new Contribuyente("Sofía", "Martínez", LocalDate.now().minusDays(8));
        Contribuyente contribuyente13 = new Contribuyente("Martín", "López", LocalDate.now().minusDays(31));
        Contribuyente contribuyente14 = new Contribuyente("Valeria", "Sánchez", LocalDate.now().minusDays(20));
        Contribuyente contribuyente15 = new Contribuyente("Nicolás", "Figueroa", LocalDate.now().minusDays(12));


        contribuyentesRepository.add(contribuyente10);
        contribuyentesRepository.add(contribuyente11);
        contribuyentesRepository.add(contribuyente12);
        contribuyentesRepository.add(contribuyente13);
        contribuyentesRepository.add(contribuyente14);
        contribuyentesRepository.add(contribuyente15);

        //Hechos

        Hecho hecho10 = new Hecho(
                coordenadas10, categoriaEstafa, null, null, null, Estado.ACEPTADO, contribuyente11,
                LocalDate.now().minusDays(4), LocalDate.now().minusDays(5),
                TipoFuente.ESTATICA, null,
                "El vendedor prometió un producto que nunca llegó luego de recibir el pago.",
                "Ciberestafa con pago anticipado", "C10", 1, LocalTime.of(10, 30));
        String hash10 = generarHash(hecho10.getTitulo()+hecho10.getDescripcion()+hecho10.getCategoria()+hecho10.getUbicacion().getLatitud()+hecho10.getUbicacion().getLongitud());
        hecho10.setHash(hash10);

        Hecho hecho11 = new Hecho(
                coordenadas11, categoriaVial, null, null, null, Estado.ACEPTADO, contribuyente12,
                LocalDate.now().minusDays(2), LocalDate.now().minusDays(2),
                TipoFuente.ESTATICA, null,
                "Una bicicleta fue embestida por un automóvil que giró sin mirar.",
                "Accidente auto y bicicleta", "C11", 1, LocalTime.of(8, 45));
        String hash11 = generarHash(hecho11.getTitulo()+hecho11.getDescripcion()+hecho11.getCategoria()+hecho11.getUbicacion().getLatitud()+hecho11.getUbicacion().getLongitud());
        hecho11.setHash(hash11);

        Hecho hecho12 = new Hecho(
                coordenadas12, categoriaAmenaza, null, null, null, Estado.ACEPTADO, contribuyente10,
                LocalDate.now().minusDays(3), LocalDate.now().minusDays(4),
                TipoFuente.ESTATICA, null,
                "Un vecino amenazó con dañar el auto si lo seguían aparcando frente a su casa.",
                "Amenaza por estacionamiento", "C12", 1, LocalTime.of(22, 0));
        String hash12 = generarHash(hecho12.getTitulo()+hecho12.getDescripcion()+hecho12.getCategoria()+hecho12.getUbicacion().getLatitud()+hecho12.getUbicacion().getLongitud());
        hecho12.setHash(hash12);

        Hecho hecho13 = new Hecho(
                coordenadas13, categoriaVial, null, null, null, Estado.ACEPTADO, contribuyente14,
                LocalDate.now().minusDays(1), LocalDate.now().minusDays(1),
                TipoFuente.ESTATICA, null,
                "Choque leve entre dos camionetas en la ruta, sin heridos.",
                "Colisión en la ruta", "C13", 1, LocalTime.of(13, 10));
        String hash13 = generarHash(hecho13.getTitulo()+hecho13.getDescripcion()+hecho13.getCategoria()+hecho13.getUbicacion().getLatitud()+hecho13.getUbicacion().getLongitud());
        hecho13.setHash(hash13);

        Hecho hecho14 = new Hecho(
                coordenadas14, categoriaEstafa, null, null, null, Estado.ACEPTADO, contribuyente13,
                LocalDate.now().minusDays(6), LocalDate.now().minusDays(8),
                TipoFuente.ESTATICA, null,
                "Alguien usó cheques falsos para pagar una compra importante.",
                "Venta fraudulenta con cheques", "C14", 1, LocalTime.of(16, 55));
        String hash14 = generarHash(hecho14.getTitulo()+hecho14.getDescripcion()+hecho14.getCategoria()+hecho14.getUbicacion().getLatitud()+hecho14.getUbicacion().getLongitud());
        hecho14.setHash(hash14);

        Hecho hecho15 = new Hecho(
                coordenadas15, categoriaAmenaza, null, null, null, Estado.ACEPTADO, contribuyente15,
                LocalDate.now().minusDays(7), LocalDate.now().minusDays(9),
                TipoFuente.ESTATICA, null,
                "Hubo un llamado anónimo donde amenazaban a un comercio.",
                "Amenaza a local comercial", "C15", 1, LocalTime.of(19, 20));
        String hash15 = generarHash(hecho15.getTitulo()+hecho15.getDescripcion()+hecho15.getCategoria()+hecho15.getUbicacion().getLatitud()+hecho15.getUbicacion().getLongitud());
        hecho15.setHash(hash15);

        hechosRepository.add(hecho10);
        hechosRepository.add(hecho11);
        hechosRepository.add(hecho12);
        hechosRepository.add(hecho13);
        hechosRepository.add(hecho14);
        hechosRepository.add(hecho15);


        // SANTA FE

        //Coordenadas
        Coordenadas coordenadas20 = new Coordenadas(-33.75348684460431, -61.93034229480898);
        Coordenadas coordenadas21 = new Coordenadas(-32.95638173243072, -60.69951830380286);
        Coordenadas coordenadas22 = new Coordenadas(-31.434858597950566, -61.258164722731635);
        Coordenadas coordenadas23 = new Coordenadas(-28.773476303139006, -60.73555955706228);
        Coordenadas coordenadas24 = new Coordenadas(-34.19294767533878, -62.044978836970174);
        Coordenadas coordenadas25 = new Coordenadas(-31.67666959110424, -61.75155961574081);

        coordenadasRepository.add(coordenadas20);
        coordenadasRepository.add(coordenadas21);
        coordenadasRepository.add(coordenadas22);
        coordenadasRepository.add(coordenadas23);
        coordenadasRepository.add(coordenadas24);
        coordenadasRepository.add(coordenadas25);

        //contribuyentes
        Contribuyente contribuyente20 = new Contribuyente("Federico", "Ramirez", LocalDate.now().minusDays(17));
        Contribuyente contribuyente21 = new Contribuyente("Julieta", "Ortega", LocalDate.now().minusDays(11));
        Contribuyente contribuyente22 = new Contribuyente("Emilia", "Castro", LocalDate.now().minusDays(24));
        Contribuyente contribuyente23 = new Contribuyente("Lucas", "Gómez", LocalDate.now().minusDays(5));
        Contribuyente contribuyente24 = new Contribuyente("Gustavo", "Zalazar", LocalDate.now().minusDays(27));

        contribuyentesRepository.add(contribuyente20);
        contribuyentesRepository.add(contribuyente21);
        contribuyentesRepository.add(contribuyente22);
        contribuyentesRepository.add(contribuyente23);
        contribuyentesRepository.add(contribuyente24);


        //Hechos
        Hecho hecho20 = new Hecho(
                coordenadas20, categoriaIncendio, null, null, null, Estado.ACEPTADO, contribuyente20,
                LocalDate.now().minusDays(3), LocalDate.now().minusDays(3),
                TipoFuente.ESTATICA, null,
                "Un cortocircuito en un galpón generó un incendio. Solo hubo daños materiales.",
                "Incendio en depósito agrícola", "S20", 1, LocalTime.of(2, 30));
        String hash20 = generarHash(hecho20.getTitulo()+hecho20.getDescripcion()+hecho20.getCategoria()+hecho20.getUbicacion().getLatitud()+hecho20.getUbicacion().getLongitud());
        hecho20.setHash(hash20);

        Hecho hecho21 = new Hecho(
                coordenadas21, categoriaChoque, null, null, null, Estado.ACEPTADO, contribuyente21,
                LocalDate.now().minusDays(2), LocalDate.now().minusDays(2),
                TipoFuente.ESTATICA, null,
                "Dos autos chocaron en la intersección, uno de los conductores no respetó el semáforo.",
                "Choque en semáforo", "S21", 1, LocalTime.of(7, 10));
        String hash21 = generarHash(hecho21.getTitulo()+hecho21.getDescripcion()+hecho21.getCategoria()+hecho21.getUbicacion().getLatitud()+hecho21.getUbicacion().getLongitud());
        hecho21.setHash(hash21);

        Hecho hecho22 = new Hecho(
                coordenadas22, categoriaRobo, null, null, null, Estado.ACEPTADO, contribuyente22,
                LocalDate.now().minusDays(5), LocalDate.now().minusDays(5),
                TipoFuente.ESTATICA, null,
                "Robo de celulares a mano armada en la salida de una escuela.",
                "Asalto a estudiantes", "S22", 1, LocalTime.of(13, 55));
        String hash22 = generarHash(hecho22.getTitulo()+hecho22.getDescripcion()+hecho22.getCategoria()+hecho22.getUbicacion().getLatitud()+hecho22.getUbicacion().getLongitud());
        hecho22.setHash(hash22);

        Hecho hecho23 = new Hecho(
                coordenadas23, categoriaVial, null, null, null, Estado.ACEPTADO, contribuyente23,
                LocalDate.now().minusDays(1), LocalDate.now().minusDays(1),
                TipoFuente.ESTATICA, null,
                "Camión quedó atascado en la ruta por exceso de carga. Hubo demoras en el tránsito.",
                "Demora vial por camión", "S23", 1, LocalTime.of(11, 0));
        String hash23 = generarHash(hecho23.getTitulo()+hecho23.getDescripcion()+hecho23.getCategoria()+hecho23.getUbicacion().getLatitud()+hecho23.getUbicacion().getLongitud());
        hecho23.setHash(hash23);

        Hecho hecho24 = new Hecho(
                coordenadas24, categoriaEstafa, null, null, null, Estado.ACEPTADO, contribuyente24,
                LocalDate.now().minusDays(10), LocalDate.now().minusDays(11),
                TipoFuente.ESTATICA, null,
                "Intento de estafa telefónica fingiendo ser del banco para obtener claves.",
                "Llamado falso de banco", "S24", 1, LocalTime.of(18, 40));
        String hash24 = generarHash(hecho24.getTitulo()+hecho24.getDescripcion()+hecho24.getCategoria()+hecho24.getUbicacion().getLatitud()+hecho24.getUbicacion().getLongitud());
        hecho24.setHash(hash24);

        hechosRepository.add(hecho20);
        hechosRepository.add(hecho21);
        hechosRepository.add(hecho22);
        hechosRepository.add(hecho23);
        hechosRepository.add(hecho24);


        //TIERRA DEL FUEGO

        //Coordenadas
        Coordenadas coordenadas30 = new Coordenadas(-54.82743465704266, -65.60292262000861);
        Coordenadas coordenadas31 = new Coordenadas(-54.666363791933556, -68.1621271405244);
        Coordenadas coordenadas32 = new Coordenadas(-53.665871187885365, -68.52336272249616);
        Coordenadas coordenadas33 = new Coordenadas(-53.43278885018243, -68.20096970099226);
        Coordenadas coordenadas34 = new Coordenadas(-54.81703556259818, -68.3345503752549);
        Coordenadas coordenadas35 = new Coordenadas(-54.72197258999909, -68.02575582570971);

        coordenadasRepository.add(coordenadas30);
        coordenadasRepository.add(coordenadas31);
        coordenadasRepository.add(coordenadas32);
        coordenadasRepository.add(coordenadas33);
        coordenadasRepository.add(coordenadas34);
        coordenadasRepository.add(coordenadas35);


        Contribuyente contribuyente30 = new Contribuyente("Camila", "Prieto", LocalDate.now().minusDays(9));
        Contribuyente contribuyente31 = new Contribuyente("Ramiro", "Fernández", LocalDate.now().minusDays(14));
        Contribuyente contribuyente32 = new Contribuyente("Ailén", "Paz", LocalDate.now().minusDays(22));
        Contribuyente contribuyente33 = new Contribuyente("Tomás", "Salinas", LocalDate.now().minusDays(3));
        Contribuyente contribuyente34 = new Contribuyente("Verónica", "Luna", LocalDate.now().minusDays(19));

        contribuyentesRepository.add(contribuyente30);
        contribuyentesRepository.add(contribuyente31);
        contribuyentesRepository.add(contribuyente32);
        contribuyentesRepository.add(contribuyente33);
        contribuyentesRepository.add(contribuyente34);

        Hecho hecho30 = new Hecho(
                coordenadas30, categoriaIncendio, null, null, null, Estado.ACEPTADO, contribuyente30,
                LocalDate.now().minusDays(3), LocalDate.now().minusDays(3),
                TipoFuente.ESTATICA, null,
                "Incendio en vivienda por estufa eléctrica en mal estado. Sin víctimas.",
                "Incendio en Ushuaia", "T30", 1, LocalTime.of(23, 15));
        String hash30 = generarHash(hecho30.getTitulo()+hecho30.getDescripcion()+hecho30.getCategoria()+hecho30.getUbicacion().getLatitud()+hecho30.getUbicacion().getLongitud());
        hecho30.setHash(hash30);

        Hecho hecho31 = new Hecho(
                coordenadas31, categoriaVial, null, null, null, Estado.ACEPTADO, contribuyente31,
                LocalDate.now().minusDays(4), LocalDate.now().minusDays(4),
                TipoFuente.ESTATICA, null,
                "Caída de poste producto del hielo en la ruta. Sin heridos, gran atasco vehicular.",
                "Poste caído por hielo", "T31", 1, LocalTime.of(7, 35));
        String hash31 = generarHash(hecho31.getTitulo()+hecho31.getDescripcion()+hecho31.getCategoria()+hecho31.getUbicacion().getLatitud()+hecho31.getUbicacion().getLongitud());
        hecho31.setHash(hash31);

        Hecho hecho32 = new Hecho(
                coordenadas32, categoriaRobo, null, null, null, Estado.ACEPTADO, contribuyente32,
                LocalDate.now().minusDays(6), LocalDate.now().minusDays(7),
                TipoFuente.ESTATICA, null,
                "Robo de herramientas en obrador durante la madrugada.",
                "Robo en obra", "T32", 1, LocalTime.of(2, 20));
        String hash32 = generarHash(hecho32.getTitulo()+hecho32.getDescripcion()+hecho32.getCategoria()+hecho32.getUbicacion().getLatitud()+hecho32.getUbicacion().getLongitud());
        hecho32.setHash(hash32);

        Hecho hecho33 = new Hecho(
                coordenadas33, categoriaEstafa, null, null, null, Estado.ACEPTADO, contribuyente33,
                LocalDate.now().minusDays(2), LocalDate.now().minusDays(2),
                TipoFuente.ESTATICA, null,
                "Transferencia bancaria fraudulenta a nombre de empresa inexistente.",
                "Estafa virtual en compra", "T33", 1, LocalTime.of(14, 50));
        String hash33 = generarHash(hecho33.getTitulo()+hecho33.getDescripcion()+hecho33.getCategoria()+hecho33.getUbicacion().getLatitud()+hecho33.getUbicacion().getLongitud());
        hecho33.setHash(hash33);

        Hecho hecho34 = new Hecho(
                coordenadas34, categoriaChoque, null, null, null, Estado.ACEPTADO, contribuyente34,
                LocalDate.now().minusDays(1), LocalDate.now().minusDays(1),
                TipoFuente.ESTATICA, null,
                "Choque múltiple por niebla en curva, tres vehículos involucrados.",
                "Accidente por niebla", "T34", 1, LocalTime.of(10, 5));
        String hash34 = generarHash(hecho34.getTitulo()+hecho34.getDescripcion()+hecho34.getCategoria()+hecho34.getUbicacion().getLatitud()+hecho34.getUbicacion().getLongitud());
        hecho34.setHash(hash34);

        hechosRepository.add(hecho30);
        hechosRepository.add(hecho31);
        hechosRepository.add(hecho32);
        hechosRepository.add(hecho33);
        hechosRepository.add(hecho34);

        //##################################################################### VIEJO #########################################################
        coordenadasRepository.add(coordenadas1);
        coordenadasRepository.add(coordenadas2);
        coordenadasRepository.add(coordenadas3);
        coordenadasRepository.add(coordenadas4);
        coordenadasRepository.add(coordenadas5);

        Contribuyente contribuyente1 = new Contribuyente("Mariana","Rossi",LocalDate.now().minusDays(23));
        Contribuyente contribuyente2 = new Contribuyente("Juan","Navia",LocalDate.now().minusDays(23));
        Contribuyente contribuyente3 = new Contribuyente("Luciana","Jofre",LocalDate.now().minusDays(23));

        contribuyentesRepository.add(contribuyente2);
        contribuyentesRepository.add(contribuyente1);
        contribuyentesRepository.add(contribuyente3);

        Etiqueta etiqueta1 = new Etiqueta("COTO Lanus");
        Etiqueta etiqueta2 = new Etiqueta("Seguridad");


        Hecho hecho1 = new Hecho(coordenadas1, categoriaIncendio, null,
                null, null, Estado.ACEPTADO, contribuyente2,
                LocalDate.now().minusDays(2), LocalDate.now().minusDays(3),
                TipoFuente.ESTATICA, null, "No hubo heridos, fue por una sartén", "Incendio en casa", null,1);
        String hash1 = generarHash(hecho1.getTitulo()+hecho1.getDescripcion()+hecho1.getCategoria()+hecho1.getUbicacion().getLatitud()+hecho1.getUbicacion().getLongitud());
        hecho1.setHash(hash1);
        Hecho hecho2 = new Hecho(coordenadas2, categoriaChoque, null,
                null, null, Estado.ACEPTADO, contribuyente3,
                LocalDate.now().minusDays(2), LocalDate.now().minusDays(3),
                TipoFuente.ESTATICA, null, "Un perro cruzo por la calle y frenó de golpe, todos a salvo.", "Choque entre moto y gol", null,1);
        String hash2 = generarHash(hecho2.getTitulo()+hecho2.getDescripcion()+hecho2.getCategoria()+hecho2.getUbicacion().getLatitud()+hecho2.getUbicacion().getLongitud());
        hecho2.setHash(hash2);
        Hecho hecho3 = new Hecho(coordenadas5, categoriaIncendio, null,
                null, null, Estado.ACEPTADO, null,
                LocalDate.now().minusDays(1), LocalDate.now().minusDays(2),
                TipoFuente.PROXY, null, "Causa desconocida", "Departamento en un edicio", null,2);
        String hash3 = generarHash(hecho3.getTitulo()+hecho3.getDescripcion()+hecho3.getCategoria()+hecho3.getUbicacion().getLatitud()+hecho3.getUbicacion().getLongitud());
        hecho3.setHash(hash3);
        Hecho hecho4 = new Hecho(coordenadas3, categoriaChoque, null,
                null, null, Estado.ACEPTADO, contribuyente1,
                LocalDate.now().minusDays(1), LocalDate.now().minusDays(2),
                TipoFuente.PROXY, List.of(etiqueta1), "Parecía que el conductor iba borracho, se llevó puesto una maceta que estaba en la calle", "Choque con maceta", null,2);
        String hash4 = generarHash(hecho4.getTitulo()+hecho4.getDescripcion()+hecho4.getCategoria()+hecho4.getUbicacion().getLatitud()+hecho4.getUbicacion().getLongitud());
        hecho4.setHash(hash4);
        Hecho hecho5 = new Hecho( coordenadas4, categoriaRobo, null,
                null, null, Estado.ACEPTADO, null,
                LocalDate.now().minusDays(5), LocalDate.now().minusDays(6),
                TipoFuente.ESTATICA, List.of(etiqueta2), "Se robó unas manzanas y bolsas", "Hurto en una verdulería", null,1);
        String hash5 = generarHash(hecho5.getTitulo()+hecho5.getDescripcion()+hecho5.getCategoria()+hecho5.getUbicacion().getLatitud()+hecho5.getUbicacion().getLongitud());
        hecho5.setHash(hash5);
        hechosRepository.add(hecho1);
        hechosRepository.add(hecho2);
        hechosRepository.add(hecho3);
        hechosRepository.add(hecho4);
        hechosRepository.add(hecho5);

        //PRUEBA CONSENSO
        Hecho hecho35 = new Hecho(
                coordenadas21, categoriaChoque, null, null, null, Estado.ACEPTADO, contribuyente21,
                LocalDate.now().minusDays(2), LocalDate.now().minusDays(2),
                TipoFuente.ESTATICA, null,
                "Dos autos chocaron en la intersección, uno de los conductores no respetó el semáforo.",
                "Choque en semáforo", "S21", 2, LocalTime.of(7, 10));
        hecho35.setHash(hash21);

        Hecho hecho36 = new Hecho(coordenadas2, categoriaChoque, null,
                null, null, Estado.ACEPTADO, contribuyente3,
                LocalDate.now().minusDays(2), LocalDate.now().minusDays(3),
                TipoFuente.ESTATICA, null, "Un perro cruzo por la calle y frenó de golpe, todos a salvo.", "Choque entre moto y gol", null,2);
        hecho36.setHash(hash2);
        hechosRepository.add(hecho35);
        hechosRepository.add(hecho36);
    }
}