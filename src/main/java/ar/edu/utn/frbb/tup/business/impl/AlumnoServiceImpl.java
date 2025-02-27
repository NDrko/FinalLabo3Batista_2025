package ar.edu.utn.frbb.tup.business.impl;
import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.model.*; 
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.exception.*;
import ar.edu.utn.frbb.tup.persistence.AlumnoDao;
import ar.edu.utn.frbb.tup.persistence.exception.ALumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoBadRequestException; 
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaBadRequestException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaBadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service public class AlumnoServiceImpl implements AlumnoService {

   @Autowired
private AlumnoDao alumnoDao;
@Autowired
private AsignaturaService asignaturaService;

private static final AtomicLong idGenerator = new AtomicLong(1); // Generador de IDs únicos

@Override
public Asignatura aprobarAsignatura(int materiaId, int nota, int idAlumno)
        throws EstadoIncorrectoException, CorrelatividadesNoAprobadasException, ALumnoNotFoundException,
               CorrelatividadException, MateriaBadRequestException, AsignaturaNotFoundException, 
               AlumnoBadRequestException, AsignaturaBadRequestException {

    if (nota < 6 || nota > 10) {
        throw new AsignaturaBadRequestException("Nota incorrecta, debe estar entre 6 y 10 para aprobar");
    }

    Alumno alumno = buscarPorId(idAlumno);
    Asignatura asignatura = buscarAsignatura(materiaId, alumno);

    for (Materia correlativa : asignatura.getMateria().getCorrelatividades()) {
        chequearCorrelatividad(correlativa, alumno);
    }
    return alumnoDao.aprobarAsignatura(alumno, materiaId, nota);
}

private void chequearCorrelatividad(Materia correlativa, Alumno alumno) throws CorrelatividadesNoAprobadasException {
    boolean aprobada = alumno.getAsignaturas().stream()
            .filter(a -> correlativa.getNombre().equals(a.getNombreAsignatura()))
            .anyMatch(a -> EstadoAsignatura.APROBADA.equals(a.getEstado()));

    if (!aprobada) {
        throw new CorrelatividadesNoAprobadasException("No aprobó " + correlativa.getNombre());
    }
}

@Override
public Alumno crearAlumno(AlumnoDto alumnoDto) {
    Alumno alumno = new Alumno();
    alumno.setId(idGenerator.getAndIncrement()); // Genera un ID único secuencialmente
    alumno.setNombre(alumnoDto.getNombre());
    alumno.setApellido(alumnoDto.getApellido());
    alumno.setDni(alumnoDto.getDni());

    alumnoDao.saveAlumno(alumno);
    return alumno;
}

@Override
public Alumno buscarPorId(int id) throws ALumnoNotFoundException {
    Alumno alumno = alumnoDao.buscarAlumnoPorId(id);
    if (alumno == null) {
        throw new ALumnoNotFoundException("Alumno con ID " + id + " no encontrado");
    }
    return alumno;
}

@Override
public Alumno borrarAlumno(int id) throws ALumnoNotFoundException {
    Alumno alumno = buscarPorId(id);
    alumnoDao.deleteAlumno(alumno);
    return alumno;
}

@Override
public Alumno editarAlumno(int id, Map<String, Object> nuevosDatos) {
    return alumnoDao.updateAlumno(id, nuevosDatos);
}

@Override
public Alumno buscarAlumno(String apellidoAlumno) {
    if (apellidoAlumno == null || apellidoAlumno.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Apellido no puede ser nulo o vacío");
    }
    return alumnoDao.buscarAlumnoApellido(apellidoAlumno);
}

@Override
public Asignatura recursarAsignatura(int idAlumno, int idAsignatura)
        throws AlumnoBadRequestException, ALumnoNotFoundException, AsignaturaNotFoundException, AsignaturaBadRequestException {
    Alumno alumno = buscarPorId(idAlumno);
    return alumnoDao.perderRegularidad(alumno, idAsignatura);
}

@Override
public Asignatura buscarAsignatura(int idAsignatura, Alumno alumno) throws AsignaturaNotFoundException {
    return alumnoDao.buscarAsignatura(idAsignatura, alumno);
}

@Override
public Asignatura cursarAsignatura(int idAlumno, int idAsignatura)
        throws ALumnoNotFoundException, AlumnoBadRequestException, AsignaturaNotFoundException {
    Alumno alumno = buscarPorId(idAlumno);
    return alumnoDao.cursarAsignatura(alumno, idAsignatura);
}

}
