package ar.edu.utn.frbb.tup.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Profesor {

    private long id;
    private String nombre;
    private String apellido;
    private String titulo;
    private List<Materia> materiasDictadas = new ArrayList<>();

    public Profesor(String nombre, String apellido, String titulo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.titulo = titulo;
    }
    // Getters y Setters
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public List<Materia> getMateriasDictadas() {
        return materiasDictadas;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, apellido, titulo, materiasDictadas);
    }
    public void setMateriasDictadas(List<Materia> materiasDictadas) {
        this.materiasDictadas = materiasDictadas;
    }
    public void agregarMateriaDictada(Materia materia) {
        this.materiasDictadas.add(materia);
    }
    @Override
    public String toString() {
        return "Profesor{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", titulo='" + titulo + '\'' +
                ", materiasDictadas=" + materiasDictadas +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profesor profesor = (Profesor) o;
        return id == profesor.id && Objects.equals(nombre, profesor.nombre) && Objects.equals(apellido, profesor.apellido) 
        && Objects.equals(titulo, profesor.titulo) && Objects.equals(materiasDictadas, profesor.materiasDictadas);
    }


}
