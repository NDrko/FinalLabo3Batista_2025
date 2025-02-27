package ar.edu.utn.frbb.tup.model;
import ar.edu.utn.frbb.tup.model.exception.AsignaturaInexistenteException;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Alumno {
    private long id;
    private String nombre;
    private String apellido;
    private long dni;
    private List<Asignatura> asignaturas = new ArrayList<>();

    public Alumno() {}

    public Alumno(String nombre, String apellido, long dni) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        
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
    public long getDni() {
        return dni;
    }
    public void setDni(long dni) {
        this.dni = dni;
    }
    public List<Asignatura> getAsignaturas() {
        return asignaturas;
    }
    public void setAsignaturas(List<Asignatura> asignaturas) {
        this.asignaturas = asignaturas;
    }
   
    public void agregarAsignatura(Asignatura asignatura) {
        this.asignaturas.add(asignatura);
    }

    public void actualizarAsignatura(Asignatura asignaturaActualizada) {
        asignaturas.replaceAll(a -> 
            a.getNombreAsignatura().equals(asignaturaActualizada.getNombreAsignatura()) ? 
            asignaturaActualizada : a);
    }

    public void aprobarAsignatura(Materia materia, int nota)
            throws EstadoIncorrectoException, CorrelatividadException, AsignaturaInexistenteException {
        Asignatura asignaturaAAprobar = getAsignaturaPorMateria(materia);
        
        for (Materia correlativa : materia.getCorrelatividades()) {
            if (!estaAprobada(correlativa)) {
                throw new CorrelatividadException("La asignatura correlativa " + correlativa.getNombre() + " no está aprobada");
            }
        }
        asignaturaAAprobar.aprobarAsignatura(nota);
    }

    private boolean estaAprobada(Materia materia) {
        return asignaturas.stream()
                .filter(a -> a.getNombreAsignatura().equals(materia.getNombre()))
                .anyMatch(a -> a.getEstado() == EstadoAsignatura.APROBADA);
    }

    private Asignatura getAsignaturaPorMateria(Materia materia) throws AsignaturaInexistenteException {
        return asignaturas.stream()
                .filter(a -> a.getNombreAsignatura().equals(materia.getNombre()))
                .findFirst()
                .orElseThrow(() -> new AsignaturaInexistenteException("No se encontró la asignatura para la materia: " + materia.getNombre()));
    }

        @Override
    public String toString() {
        return String.format("Alumno{id=%d, nombre='%s', apellido='%s', dni=%d, asignaturas=%s}",
        id, nombre, apellido, dni, asignaturas);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Alumno alumno = (Alumno) obj;
        return Long.compare(id, alumno.id) == 0 &&
            Long.compare(dni, alumno.dni) == 0 &&
            Objects.equals(nombre, alumno.nombre) &&
            Objects.equals(apellido, alumno.apellido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, apellido, dni);
    }

}
