package core.models.entities.hecho;

import core.models.entities.fuentes.TipoFuente;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity(name = "hecho")
public class Hecho {

    public Hecho(Integer id, Coordenadas ubicacion, Categoria categoria,
                 List<SugerenciaDeCambio> sugerenciaDeCambio,
                 LocalDate ultimaFechaModificacion, List<String> multimedia,
                 Estado estado, Contribuyente contribuyente,
                 LocalDate fechaCarga, LocalDate fechaSuceso,
                 TipoFuente fuenteDeOrigen,
                 List<Etiqueta> etiquetas, String descripcion, String titulo,String codigoDeFuente, String hash, Integer idFuente) {

        this.id = id;
        this.ubicacion = ubicacion;
        this.categoria = categoria;
        this.sugerenciaDeCambio = sugerenciaDeCambio;
        this.ultimaFechaModificacion = ultimaFechaModificacion;
        this.multimedia = multimedia;
        this.estado = estado;
        this.contribuyente = contribuyente;
        this.fechaCarga = fechaCarga;
        this.fechaSuceso = fechaSuceso;
        this.fuenteDeOrigen = fuenteDeOrigen;
        this.etiquetas = etiquetas;
        this.descripcion = descripcion;
        this.titulo = titulo;
        this.codigoDeFuente = codigoDeFuente;
        this.hash = hash;
        this.idFuente =idFuente;
    }

    public Hecho(Integer id, Coordenadas ubicacion, Categoria categoria,
                 List<SugerenciaDeCambio> sugerenciaDeCambio,
                 LocalDate ultimaFechaModificacion, List<String> multimedia,
                 Estado estado, Contribuyente contribuyente,
                 LocalDate fechaCarga, LocalDate fechaSuceso, LocalTime horaSuceso,
                 TipoFuente fuenteDeOrigen,
                 List<Etiqueta> etiquetas, String descripcion, String titulo,String codigoDeFuente, String hash, Integer idFuente, String linkFuente) {

        this.id = id;
        this.ubicacion = ubicacion;
        this.categoria = categoria;
        this.sugerenciaDeCambio = sugerenciaDeCambio;
        this.ultimaFechaModificacion = ultimaFechaModificacion;
        this.multimedia = multimedia;
        this.estado = estado;
        this.contribuyente = contribuyente;
        this.fechaCarga = fechaCarga;
        this.horaSuceso = horaSuceso;
        this.fechaSuceso = fechaSuceso;
        this.fuenteDeOrigen = fuenteDeOrigen;
        this.etiquetas = etiquetas;
        this.descripcion = descripcion;
        this.titulo = titulo;
        this.codigoDeFuente = codigoDeFuente;
        this.hash = hash;
        this.idFuente =idFuente;
        this.linkFuente = linkFuente;
    }

    public Hecho(Integer id, Coordenadas ubicacion, Categoria categoria,
                 List<SugerenciaDeCambio> sugerenciaDeCambio,
                 LocalDate ultimaFechaModificacion, List<String> multimedia,
                 Estado estado, Contribuyente contribuyente,
                 LocalDate fechaCarga, LocalDate fechaSuceso,
                 TipoFuente fuenteDeOrigen,
                 List<Etiqueta> etiquetas, String descripcion, String titulo,String codigoDeFuente, String hash, LocalTime horarioSuceso) {

        this.id = id;
        this.ubicacion = ubicacion;
        this.categoria = categoria;
        this.sugerenciaDeCambio = sugerenciaDeCambio;
        this.ultimaFechaModificacion = ultimaFechaModificacion;
        this.multimedia = multimedia;
        this.estado = estado;
        this.contribuyente = contribuyente;
        this.fechaCarga = fechaCarga;
        this.fechaSuceso = fechaSuceso;
        this.fuenteDeOrigen = fuenteDeOrigen;
        this.etiquetas = etiquetas;
        this.descripcion = descripcion;
        this.titulo = titulo;
        this.codigoDeFuente = codigoDeFuente;
        this.hash = hash;
        this.horaSuceso = horarioSuceso;
    }
    //Es para el seeder
    public Hecho(Coordenadas ubicacion, Categoria categoria,
                 List<SugerenciaDeCambio> sugerenciaDeCambio,
                 LocalDate ultimaFechaModificacion, List<String> multimedia,
                 Estado estado, Contribuyente contribuyente,
                 LocalDate fechaCarga, LocalDate fechaSuceso,
                 TipoFuente fuenteDeOrigen,
                 List<Etiqueta> etiquetas, String descripcion, String titulo,String codigoDeFuente, Integer idFuente, LocalTime horarioSuceso) {

        this.ubicacion = ubicacion;
        this.categoria = categoria;
        this.sugerenciaDeCambio = sugerenciaDeCambio;
        this.ultimaFechaModificacion = ultimaFechaModificacion;
        this.multimedia = multimedia;
        this.estado = estado;
        this.contribuyente = contribuyente;
        this.fechaCarga = fechaCarga;
        this.fechaSuceso = fechaSuceso;
        this.fuenteDeOrigen = fuenteDeOrigen;
        this.etiquetas = etiquetas;
        this.descripcion = descripcion;
        this.titulo = titulo;
        this.codigoDeFuente = codigoDeFuente;
        this.idFuente =idFuente;
        this.horaSuceso = horarioSuceso;
    }

    public Hecho(){}
    public Hecho(Coordenadas ubicacion, Categoria categoria,
                 List<SugerenciaDeCambio> sugerenciaDeCambio,
                 LocalDate ultimaFechaModificacion, List<String> multimedia,
                 Estado estado, Contribuyente contribuyente,
                 LocalDate fechaCarga, LocalDate fechaSuceso,
                 TipoFuente fuenteDeOrigen,
                 List<Etiqueta> etiquetas, String descripcion, String titulo,String codigoDeFuente, Integer idFuente) {

        this.ubicacion = ubicacion;
        this.categoria = categoria;
        this.sugerenciaDeCambio = sugerenciaDeCambio;
        this.ultimaFechaModificacion = ultimaFechaModificacion;
        this.multimedia = multimedia;
        this.estado = estado;
        this.contribuyente = contribuyente;
        this.fechaCarga = fechaCarga;
        this.fechaSuceso = fechaSuceso;
        this.fuenteDeOrigen = fuenteDeOrigen;
        this.etiquetas = etiquetas;
        this.descripcion = descripcion;
        this.titulo = titulo;
        this.codigoDeFuente = codigoDeFuente;
        this.idFuente =idFuente;
    }

    @Id
    @Column(name = "id_hecho")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "titulo")
    private String titulo;
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Lob
    @Column(name = "descripcion")
    private String descripcion;
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "hecho_etiqueta",
            joinColumns = @JoinColumn(name = "id_hecho"),
            inverseJoinColumns = @JoinColumn(name = "id_etiqueta")
    )
    private List<Etiqueta> etiquetas = new ArrayList<>();
    public List<Etiqueta> getEtiquetas() {
        return etiquetas;
    }
    public void setEtiquetas(List<Etiqueta> etiquetas) {
        this.etiquetas = etiquetas;
    }

    @Column(name = "tipo_de_fuente")
    private TipoFuente fuenteDeOrigen;
    public TipoFuente getFuenteDeOrigen() {
        return fuenteDeOrigen;
    }
    public void setFuenteDeOrigen(TipoFuente fuenteDeOrigen) {
        this.fuenteDeOrigen = fuenteDeOrigen;
    }

    @Column(name = "fecha_carga")
    private LocalDate fechaCarga;
    public LocalDate getFechaCarga() {
        return fechaCarga;
    }
    public void setFechaCarga(LocalDate fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    @Column(name = "fecha_suceso")
    private LocalDate fechaSuceso;
    public LocalDate getFechaSuceso() {
        return fechaSuceso;
    }
    public void setFechaSuceso(LocalDate fechaSuceso) {
        this.fechaSuceso = fechaSuceso;
    }

    @Column(name = "hora_suceso")
    public LocalTime horaSuceso;
    public LocalTime getHoraSuceso() {
        return horaSuceso;
    }
    public void setHoraSuceso(LocalTime horaSuceso) {
        this.horaSuceso = horaSuceso;
    }

    //@Column(name = "contribuyente")
    @ManyToOne
    @JoinColumn(name = "id_contribuyente")
    private Contribuyente contribuyente;
    public Contribuyente getContribuyente() {
        return contribuyente;
    }
    public void setContribuyente(Contribuyente contribuyente) {
        this.contribuyente = contribuyente;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private  Estado estado = Estado.ACEPTADO;
    public Estado getEstado() {
        return estado;
    }
    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Column(name = "multimedia")
    @ElementCollection
    @CollectionTable(
            name = "hecho_multimedia",
            joinColumns = @JoinColumn(name = "hecho_id",
                    foreignKey = @ForeignKey(name = "hecho_multimedia_hecho"))
    )
    private List<String> multimedia = new ArrayList<>();
    public List<String> getMultimedia() {
        return multimedia;
    }
    public void setMultimedia(List<String> multimedia) {
        this.multimedia = multimedia;
    }

    @Column(name = "ultima_fecha_modificacion")
    private LocalDate ultimaFechaModificacion;
    public LocalDate getUltimaFechaModificacion() {
        return ultimaFechaModificacion;
    }
    public void setUltimaFechaModificacion(LocalDate ultimaFechaModificacion) {
        this.ultimaFechaModificacion = ultimaFechaModificacion;
    }

    @OneToMany
    @Column(name = "sugerenciaDeCambio")
    @JoinColumn(name = "id_sugerencia_de_cambio")
    private List<SugerenciaDeCambio> sugerenciaDeCambio;
    public List<SugerenciaDeCambio> getSugerenciaDeCambio() {
        return sugerenciaDeCambio;
    }
    public void setSugerenciaDeCambio(List<SugerenciaDeCambio> sugerenciaDeCambio) {
        this.sugerenciaDeCambio = sugerenciaDeCambio;
    }

    @ManyToOne
    //@Column(name = "categoria")
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;
    public Categoria getCategoria() {
        return categoria;
    }
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    //@Column(name = "ubicacion")
    @OneToOne
    @JoinColumn(name = "id_ubicacion")
    private Coordenadas ubicacion;
    public Coordenadas getUbicacion() {
        return ubicacion;
    }
    public void setUbicacion(Coordenadas ubicacion) {
        this.ubicacion = ubicacion;
    }

    @Column(name = "codigo_fuente")
    private String codigoDeFuente;
    public String getCodigoDeFuente() {
        return codigoDeFuente;
    }

    @Column(name = "hash")
    public String hash;
    public String getHash() {
        return hash;
    }
    public void setHash(String hash) {
        this.hash = hash;
    }

    @Column(name = "id_fuente")
    public Integer idFuente;
    public Integer getIdFuente() {
        return Objects.requireNonNullElse(idFuente, -1);
    }
    public void setIdFuente(Integer idFuente) {this.idFuente = idFuente;}

    @Column(name = "link_fuente")
    public String linkFuente;
    public String getLinkFuente() {
        return linkFuente;
    }
    public void setLinkFuente(String linkFuente) {
        this.linkFuente = linkFuente;
    }


    @Override
    public String toString() {
        return "Titulo: " + titulo +
                "\n ||| ID: " + id +
                "\n ||| Descripcion:" + descripcion +
                "\n ||| Fecha Suceso: " + fechaSuceso +
                "\n ||| Fecha Carga: " + fechaCarga +
                "\n ||| Sugerencia De Cambio: " + sugerenciaDeCambio +
                "\n ||| Coordenadas: " + ubicacion.toString();
    }

    public void agregarEtiqueta(Etiqueta etiqueta)
    { etiquetas.add(etiqueta);}

    public boolean pasoUnaSemana(){
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaActualMenosUnaSemana = fechaActual.minusDays(7);
        return fechaActualMenosUnaSemana.isBefore(fechaSuceso);
    }

    public void desactivarse(){
        this.setEstado(Estado.INACTIVO);
    }
    public void activarse(){
        this.setEstado(Estado.ACEPTADO);
    }

    public boolean perteneceAFuente(List<String> listaFuentes){ //Recibe los IDs de las fuentes
        return listaFuentes.contains(this.codigoDeFuente);
    }

    public String getProvincia() {

        return this.ubicacion.getProvincia();
    }
}
