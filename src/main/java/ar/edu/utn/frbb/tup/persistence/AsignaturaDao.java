package ar.edu.utn.frbb.tup.persistence;
import ar.edu.utn.frbb.tup.model.Asignatura;

public interface AsignaturaDao {
    Asignatura buscarAsignatura(int materiaId, long dni);
    void updateAsignatura(Asignatura asignatura);
}
