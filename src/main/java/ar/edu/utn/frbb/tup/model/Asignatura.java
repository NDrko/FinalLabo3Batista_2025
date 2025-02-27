package ar.edu.utn.frbb.tup.model;
import java.util.Objects;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;

public class Asignatura {

    private Materia materia;
    private EstadoAsignatura estado;
    private Integer nota; 

    public Asignatura() {}

    public Asignatura(Materia materia) {
        this.materia = materia;
        this.estado = EstadoAsignatura.NO_CURSADA;
        this.nota = 0;
    }

    // Getters y Setters
    public Materia getMateria() {
        return materia;
    }
    public void setMateria(Materia materia) {
        this.materia = materia;
    }
    public EstadoAsignatura getEstado() {
        return estado;
    }
    public void setEstado(EstadoAsignatura estado) {
        this.estado = estado;
    }
    public Integer getNota() {
        return nota;
    }
    public void setNota(Integer nota) {
        this.nota = nota;
    }


    public String getNombreAsignatura(){
        return this.materia != null ? this.materia.getNombre() : null;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asignatura that = (Asignatura) o;
        return Objects.equals(materia, that.materia) && estado == that.estado && Objects.equals(nota, that.nota);
    }

    @Override
    public int hashCode() {
        return Objects.hash(materia, estado, nota);

    }

    public void cursarAsignatura(){
        this.estado = EstadoAsignatura.CURSADA;
    }

    public void aprobarAsignatura(int nota) throws EstadoIncorrectoException {
        if (this.estado != EstadoAsignatura.CURSADA) {
            throw new EstadoIncorrectoException("La asignatura debe estar cursada para poder aprobarla");
        }
        if (nota >= 4) {
            this.estado = EstadoAsignatura.APROBADA;
            this.nota = nota;
        }
    }

    @Override
    public String toString() {
        return "Asignatura{" +
                "materia=" + materia +
                ", estado=" + estado +
                ", nota=" + nota +
                '}';
    }

}
