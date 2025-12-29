package core.models.entities.colecciones;

import core.models.entities.colecciones.criterios.Criterio;
import core.models.entities.fuentes.Fuente;
import core.models.entities.hecho.Hecho;
import core.models.entities.hecho.Estado;
import core.models.repository.ColeccionesRepository;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name="coleccion")
public class Coleccion {


    public Coleccion(Integer id, String titulo, String descripcionColeccion,
                     List<Criterio> criterioDePertenencia,
                     List<Fuente> fuentes,
                     List<Hecho> hechos,
                     List<Hecho> hechosVisibles,
                     String identificadorHandle) {
        this.id = id;
        this.titulo = titulo;
        this.descripcionColeccion = descripcionColeccion;
        this.criterioDePertenencia = criterioDePertenencia != null ? new ArrayList<>(criterioDePertenencia) : new ArrayList<>();
        this.fuentes = fuentes != null ? new ArrayList<>(fuentes) : new ArrayList<>();
        this.hechos = hechos != null ? new ArrayList<>(hechos) : new ArrayList<>();
        this.hechosVisibles = hechosVisibles != null ? new ArrayList<>(hechosVisibles) : new ArrayList<>();
        this.identificadorHandle = identificadorHandle;
        this.modoDeNavegacion = ModoDeNavegacion.IRRESTRICTA;
        this.algoritmoConsenso = null;
    }

    public Coleccion(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_coleccion")
    private Integer id;
    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    @Column(name = "titulo")
    private String titulo;
    public String getTitulo() {return titulo;}
    public void setTitulo(String titulo) {this.titulo = titulo;}

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "coleccion_fuente", // nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "id_coleccion"), // FK hacia tu entidad actual
            inverseJoinColumns = @JoinColumn(name = "id_fuente") // FK hacia Hecho
    )
    @Fetch(FetchMode.SUBSELECT)
    private List<Fuente> fuentes;
    public List<Fuente> getFuentes() {return this.fuentes;}
    public void setFuentes(List<Fuente> fuentes){this.fuentes = fuentes;}

    public void agregarFuente (Fuente f) {fuentes.add(f);}
    public void agregarFuentes (List<Fuente> listaFuentes){fuentes.addAll(listaFuentes);}
    public void eliminarFuente (Fuente f) {fuentes.remove(f);}

    @ManyToMany
    @JoinTable(
            name = "hechos_visibles", // nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "id_coleccion"), // FK hacia tu entidad actual
            inverseJoinColumns = @JoinColumn(name = "id_hecho") // FK hacia Hecho
    )
    public List<Hecho> hechosVisibles;
    public List<Hecho> getHechosVisibles() {return hechosVisibles;}


    @Column(name = "descripcionColeccion")
    private String descripcionColeccion;
    public String getDescripcionColeccion() {return descripcionColeccion;}
    public void setDescripcionColeccion(String descripcionColeccion) {this.descripcionColeccion = descripcionColeccion;}

    /*@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "coleccion_id") // FK en la tabla de Criterio*/
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "coleccion_criterio",
            joinColumns = @JoinColumn(name = "coleccion_id"),
            inverseJoinColumns = @JoinColumn(name = "criterio_id")
    )
    @Fetch(FetchMode.SUBSELECT)
    private List<Criterio> criterioDePertenencia = new ArrayList<>();
    public List<Criterio> getCriterioDePertenencia() {
        return criterioDePertenencia;
    }
    public void setCriterioDePertenencia(List<Criterio> criterioDePertenencia) {this.criterioDePertenencia = criterioDePertenencia;}
    public void agregarCriterio(Criterio criterio) {criterioDePertenencia.add(criterio);};
    public void eliminarCriterio(Criterio criterio) {criterioDePertenencia.remove(criterio);};

    @Column(name = "modoDeNavegacion")
    public ModoDeNavegacion modoDeNavegacion;
    public ModoDeNavegacion getModoDeNavegacion() {return modoDeNavegacion;}
    public void setModoDeNavegacion(ModoDeNavegacion modoDeNavegacion) {this.modoDeNavegacion = modoDeNavegacion;}

    @Convert(converter = AlgoritmoConsensoConverter.class)
    @Column(name = "algoritmoConsenso")
    public AlgoritmoConsenso algoritmoConsenso = null;
    public void cambiarAlgoritmoConsenso(TipoConsenso algoritmoConsenso){
        switch (algoritmoConsenso){
            case ABSOLUTO -> this.setAlgoritmoConsenso(new StrategyAbsoluta());
            case MAYORIA_SIMPLE -> this.setAlgoritmoConsenso(new StrategyMayoriaSimple());
            case MULTIPLES_MENCIONES -> this.setAlgoritmoConsenso(new StrategyMultiplesMenciones());
            case null -> this.setAlgoritmoConsenso(null);
        }
    }
    public AlgoritmoConsenso getAlgoritmoConsenso() {return algoritmoConsenso;}
    public void setAlgoritmoConsenso(AlgoritmoConsenso algoritmoConsenso) {this.algoritmoConsenso = algoritmoConsenso;}

    public void actualizarColeccionVisible(){

        if(this.modoDeNavegacion==ModoDeNavegacion.IRRESTRICTA || this.algoritmoConsenso == null){
            // Solo hechos aceptados
            if (this.hechos != null) {
                this.hechosVisibles = this.hechos.stream()
                        .filter(h -> h.getEstado() == Estado.ACEPTADO)
                        .collect(Collectors.toCollection(ArrayList::new));
            } else {
                this.hechosVisibles = null;
            }
        }else{
            this.hechosVisibles = this.algoritmoConsenso.ejecutarAlgoritmo(this.hechos, this.fuentes);
        }

    }

    public void modificarModoNavegacion(ModoDeNavegacion modoDeNavegacion){
        this.modoDeNavegacion=modoDeNavegacion;
    }

    @ManyToMany
    @JoinTable(
            name = "coleccion_hecho", // nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "id_coleccion"), // FK hacia tu entidad actual
            inverseJoinColumns = @JoinColumn(name = "id_hecho") // FK hacia Hecho
    )
    private List<Hecho> hechos;
    public List<Hecho> getHechos() {return hechos;}
    public void setHechos(List<Hecho> hechos) {this.hechos = hechos;}
    public void agregarHecho(Hecho hecho){this.hechos.add(hecho);}


    private String identificadorHandle;
    public String getIdentificadorHandle() {return identificadorHandle;}
    public void setIdentificadorHandle(String identificadorHandle) {this.identificadorHandle = identificadorHandle;}

    public boolean hechoYaExistenteEnColeccion(String hash)
    {return this.hechos.stream().anyMatch(h -> h.getHash().equals(hash));
    }

    @Override
    public String toString() {
        return titulo;
    }
}
