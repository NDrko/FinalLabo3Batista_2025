package ar.edu.utn.frbb.tup.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Materia {

    private int id;
    private String nombre;
    private int anio;
    private int cuatrimestre;
    private String codigo;
    private Profesor profesor;
    private List<Materia> correlatividades = new ArrayList<>();
    private Carrera carrera;
    
    public Materia() {}

    public Materia(String nombre, int anio, int cuatrimestre, Profesor profesor) {
        this.nombre = nombre;
        this.anio = anio;
        this.cuatrimestre = cuatrimestre;
        this.profesor = profesor;
    }
    // Getters y Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public int getAnio() {
        return anio;
    }
    public void setAnio(int anio) {
        this.anio = anio;
    }
    public int getCuatrimestre() {
        return cuatrimestre;
    }
    public void setCuatrimestre(int cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public Profesor getProfesor() {
        return profesor;
    }
    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }
    public List<Materia> getCorrelatividades() {
        return correlatividades;
    }
    public void setCorrelatividades(List<Materia> correlatividades) {
        this.correlatividades = correlatividades;
    }
    public Carrera getCarrera() {
        return carrera;
    }
    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public void agregarCorrelatividad(Materia materia) {
        if (!this.correlatividades.contains(materia)) {
            this.correlatividades.add(materia);
        }
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, anio, cuatrimestre);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Materia)) return false;
        Materia materia = (Materia) o;
        return id == materia.id &&
               anio == materia.anio &&
               cuatrimestre == materia.cuatrimestre &&
               Objects.equals(nombre, materia.nombre);
    }

    @Override
    public String toString() {
        return "Materia{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", anio=" + anio +
                ", cuatrimestre=" + cuatrimestre +
                ", profesor=" + profesor +
                ", codigo='" + codigo + '\'' +
                ", correlatividades=" + correlatividades +
                ", carrera=" + carrera +
                '}';
    }
}
