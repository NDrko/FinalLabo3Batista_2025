package ar.edu.utn.frbb.tup.business;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadException;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadesNoAprobadasException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.exception.ALumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoBadRequestException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaBadRequestException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaBadRequestException;

import java.util.Map;
public interface AlumnoService {
    Alumno crearAlumno(AlumnoDto alumno);
    Alumno buscarAlumno(String apellidoAlumno);
    Alumno buscarPorId(int id) throws ALumnoNotFoundException;
    Alumno borrarAlumno(int id) throws ALumnoNotFoundException;
    Alumno editarAlumno(int id, Map<String, Object> nuevosDatos);
    Asignatura aprobarAsignatura(int materiaId, int nota, int idAlumno) throws EstadoIncorrectoException, CorrelatividadesNoAprobadasException,
     ALumnoNotFoundException, CorrelatividadException, MateriaBadRequestException, AsignaturaNotFoundException, AlumnoBadRequestException, AsignaturaBadRequestException;
    Asignatura recursarAsignatura(int idAlumno, int idAsignatura) throws AlumnoBadRequestException,ALumnoNotFoundException,AsignaturaNotFoundException, AsignaturaBadRequestException;
    Asignatura buscarAsignatura(int idAsignatura, Alumno a) throws AsignaturaNotFoundException;
    Asignatura cursarAsignatura(int idAlumno, int idAsignatura) throws ALumnoNotFoundException, AlumnoBadRequestException, AsignaturaNotFoundException;
}