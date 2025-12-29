package core.models.entities.hecho;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "sugerencia_de_cambio")
public class SugerenciaDeCambio {
    public SugerenciaDeCambio() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sugerencia_de_cambio")
    private Integer id;

    public SugerenciaDeCambio(LocalDate fechaSugerencia, String descripcionSugerencia, Hecho hecho, String tituloSugerencia, List<Etiqueta> etiquetasSugerencia, LocalDate fechaSucesoSugerencia, LocalTime horaSucesoSugerencia, Categoria categoriaSugerencia, String descripcion) {
        this.fechaSugerencia = fechaSugerencia;
        this.descripcionSugerencia = descripcionSugerencia;
        this.hecho = hecho;
        this.titulo = tituloSugerencia;
        this.descripcion = descripcion;
        this.etiquetas = etiquetasSugerencia;
        this.fechaSuceso = fechaSucesoSugerencia;
        this.horaSuceso = horaSucesoSugerencia;
        this.categoria = categoriaSugerencia;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "fecha_sugerencia")
    private LocalDate fechaSugerencia;
    public LocalDate getFechaSugerencia() {
        return fechaSugerencia;
    }
    public void setFechaSugerencia(LocalDate fechaSugerencia) {
        this.fechaSugerencia = fechaSugerencia;
    }

    @Column(name = "descripcion_sugerencia")
    private String descripcionSugerencia;
    public String getDescripcionSugerencia() {
        return descripcionSugerencia;
    }
    public void setDescripcionSugerencia(String descripcionSugerencia) {
        this.descripcionSugerencia = descripcionSugerencia;
    }

    @Column(name = "aprobada")
    private Boolean aprobada;
    public Boolean getAprobada() {
        return aprobada;
    }
    public void setAprobada(Boolean estado) {
        this.aprobada = estado;
    }

    @Column(name = "fecha_revision")
    private LocalDate fechaRevision;
    public LocalDate getFechaRevision() {
        return fechaRevision;
    }
    public void setFechaRevision(LocalDate fechaRevision) {
        this.fechaRevision = fechaRevision;
    }

    @ManyToOne
    @JoinColumn(name = "id_hecho", referencedColumnName = "id_hecho", nullable = false)

    private Hecho hecho;
    public Hecho getHecho() {
        return hecho;
    }
    public void setHecho(Hecho hecho) {
        this.hecho = hecho;
    }

    @Column(name = "titulo")
    private String titulo;
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Column(name = "descripcion")
    private String descripcion;
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "modificacion_etiqueta",
            joinColumns = @JoinColumn(name = "id_sugerencia_de_cambio"),
            inverseJoinColumns = @JoinColumn(name = "id_etiqueta")
    )
    private List<Etiqueta> etiquetas = new ArrayList<>();
    public List<Etiqueta> getEtiquetas() {
        return etiquetas;
    }
    public void setEtiquetas(List<Etiqueta> etiquetas) {
        this.etiquetas = etiquetas;
    }

    @Column(name = "fecha_suceso")
    private LocalDate fechaSuceso;
    public LocalDate getFechaSuceso() {
        return fechaSuceso;
    }
    public void setFechaSuceso(LocalDate fecha) {
        this.fechaSuceso = fecha;
    }

    @Column(name = "hora_suceso")
    private LocalTime horaSuceso;
    public LocalTime getHoraSuceso() {
        return horaSuceso;
    }
    public void setHoraSuceso(LocalTime hora) {
        this.horaSuceso = hora;
    }

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;
    public Categoria getCategoria() {
        return categoria;
    }
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Hecho aceptarSugerencia(){
        this.aprobada = true;
        this.fechaRevision = LocalDate.now();

        if (this.titulo != null) {
            this.hecho.setTitulo(this.titulo);
        }

        if (this.descripcion != null) {
            this.hecho.setDescripcion(this.descripcion);
        }

        if (this.fechaSuceso != null) {
            this.hecho.setFechaSuceso(this.fechaSuceso);
        }

        if (this.horaSuceso != null) {
            this.hecho.setHoraSuceso(this.horaSuceso);
        }

        if (this.categoria != null) {
            this.hecho.setCategoria(this.categoria);
        }

        if (this.etiquetas != null) {
            this.hecho.setEtiquetas(new ArrayList<>(this.etiquetas));
        }

        this.hecho.setUltimaFechaModificacion(LocalDate.now());

        return this.hecho;
    }

    public void rechazarSugerencia(){
        this.aprobada = false;
        this.fechaRevision = LocalDate.now();
    }
}
