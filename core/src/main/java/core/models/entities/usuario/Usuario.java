package core.models.entities.usuario;

import javax.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private String rol; // "ADMIN" o "USER"

    @Column(nullable = true)
    private String foto;

    @Column(nullable = true)
    private String contrasena;

    @Column(name = "fecha_nacimiento", nullable = true)
    private LocalDate fechaNacimiento;

    // ===== Getters & Setters =====

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    // Constructor vacío (JPA)
    public Usuario() {}

    // Constructor útil actualizado
    public Usuario(String nombre, String correo, String rol,
                   String foto, String contrasena, LocalDate fechaNacimiento) {
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
        this.foto = foto;
        this.contrasena = contrasena;
        this.fechaNacimiento = fechaNacimiento;
    }
}

