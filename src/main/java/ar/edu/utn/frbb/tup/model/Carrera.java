package ar.edu.utn.frbb.tup.model;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Carrera {

    private int id;
    private String nombre;
    private String codigo;
    private int cantidadCuatrimestres;
    private int idDepartamento;
    private Set<Materia> materias = new HashSet<>();

    public Carrera() {}

    public Carrera(String nombre, int cantidadCuatrimestres, int idDepartamento) {
        this.nombre = nombre;
        this.cantidadCuatrimestres = cantidadCuatrimestres;
        this.idDepartamento = idDepartamento;
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
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public int getCantidadCuatrimestres() {
        return cantidadCuatrimestres;
    }
    public void setCantidadCuatrimestres(int cantidadCuatrimestres) {
        this.cantidadCuatrimestres = cantidadCuatrimestres;
    }
    public int getIdDepartamento() {
        return idDepartamento;
    }
    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }
    public Set<Materia> getMaterias() {
        return materias;
    }
    public void setMaterias(Set<Materia> materias) {
        this.materias = materias;
    }
    public void agregarMateria(Materia materia) {
        this.materias.add(materia);
        materia.setCarrera(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carrera carrera = (Carrera) o;
        return cantidadCuatrimestres == carrera.cantidadCuatrimestres && idDepartamento == carrera.idDepartamento
         && Objects.equals(nombre, carrera.nombre)  && Objects.equals(materias, carrera.materias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, cantidadCuatrimestres, idDepartamento, materias);
    }

    @Override
    public String toString() {
        return "Carrera{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", codigo='" + codigo + '\'' +
                ", cantidadCuatrimestres=" + cantidadCuatrimestres +
                ", idDepartamento=" + idDepartamento +
                ", materiasList=" + materias +
                '}';
    }
}
