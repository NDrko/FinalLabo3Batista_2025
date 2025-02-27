package ar.edu.utn.frbb.tup.persistence; 
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura; 
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadException; 
import ar.edu.utn.frbb.tup.persistence.exception.ALumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaBadRequestException; 
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException; 
import ar.edu.utn.frbb.tup.persistence.exception.MateriaBadRequestException; 
import java.util.Map;

public interface AlumnoDao { 
void saveAlumno(Alumno alumno);
void deleteAlumno(Alumno alumno);
Alumno cargarAlumnoPorDni(long dni);
Alumno buscarAlumnoApellido(String apellido);
Alumno buscarAlumnoPorId(int id) throws ALumnoNotFoundException;
Alumno updateAlumno(int id, Map<String, Object> nuevosDatos); 
Asignatura cursarAsignatura(Alumno a, int materiaId) throws AsignaturaNotFoundException; 
Asignatura aprobarAsignatura(Alumno alumno, int idAsignatura, int nota) throws CorrelatividadException, MateriaBadRequestException, ALumnoNotFoundException,
AsignaturaNotFoundException; 
Asignatura perderRegularidad(Alumno alumno, int idAsignatura) throws AsignaturaBadRequestException, AsignaturaNotFoundException; 
Asignatura buscarAsignatura(int idAsignatura, Alumno alumno) throws AsignaturaNotFoundException; }