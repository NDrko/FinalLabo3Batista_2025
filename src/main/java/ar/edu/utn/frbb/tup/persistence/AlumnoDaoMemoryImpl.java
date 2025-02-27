package ar.edu.utn.frbb.tup.persistence; 
import ar.edu.utn.frbb.tup.model.Alumno; 
import ar.edu.utn.frbb.tup.model.Asignatura; 
import ar.edu.utn.frbb.tup.model.EstadoAsignatura; 
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadException; 
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException; 
import ar.edu.utn.frbb.tup.model.exception.AsignaturaInexistenteException; 
import ar.edu.utn.frbb.tup.persistence.exception.ALumnoNotFoundException; 
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaBadRequestException; 
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException; 
import ar.edu.utn.frbb.tup.persistence.exception.MateriaBadRequestException; 
import java.util.HashMap; 
import java.util.Map;

public class AlumnoDaoMemoryImpl implements AlumnoDao { 
    private Map<Integer, Alumno> alumnoMap = new HashMap<>(); private int idCounter = 1;
    @Override
public void saveAlumno(Alumno alumno) {
    if (alumno.getId() == 0) {
        alumno.setId(idCounter++);
    }
    alumnoMap.put((int) alumno.getId(), alumno);
}

@Override
public Alumno cargarAlumnoPorDni(long dni) {
    return alumnoMap.values().stream()
            .filter(a -> a.getDni() == dni)
            .findFirst()
            .orElse(null);
}

@Override
public Alumno buscarAlumnoApellido(String apellido) {
    return alumnoMap.values().stream()
            .filter(a -> a.getApellido().equalsIgnoreCase(apellido))
            .findFirst()
            .orElse(null);
}

@Override
public Alumno buscarAlumnoPorId(int id) throws ALumnoNotFoundException {
    Alumno alumno = alumnoMap.get(id);
    if (alumno == null) {
        throw new ALumnoNotFoundException("Alumno con ID " + id + " no encontrado.");
    }
    return alumno;
}

@Override
public void deleteAlumno(Alumno alumno) {
    alumnoMap.remove((int) alumno.getId());
}

@Override
public Alumno updateAlumno(int id, Map<String, Object> nuevosDatos) {
    Alumno alumno = alumnoMap.get(id);
    if (alumno != null) {
        if (nuevosDatos.containsKey("nombre")) {
            alumno.setNombre((String) nuevosDatos.get("nombre"));
        }
        if (nuevosDatos.containsKey("apellido")) {
            alumno.setApellido((String) nuevosDatos.get("apellido"));
        }
        if (nuevosDatos.containsKey("dni")) {
            alumno.setDni((Long) nuevosDatos.get("dni"));
        }
        alumnoMap.put(id, alumno);
    }
    return alumno;
}

@Override
public Asignatura cursarAsignatura(Alumno alumno, int idAsignatura) throws AsignaturaNotFoundException {
    Asignatura asignatura = alumno.getAsignaturas().stream()
            .filter(a -> a.getMateria().getId() == idAsignatura)
            .findFirst()
            .orElseThrow(() -> new AsignaturaNotFoundException("Asignatura no encontrada."));
    asignatura.setEstado(EstadoAsignatura.CURSADA);
    return asignatura;
}

@Override
public Asignatura aprobarAsignatura(Alumno alumno, int idAsignatura, int nota) throws MateriaBadRequestException, ALumnoNotFoundException, AsignaturaNotFoundException, CorrelatividadException {
    if (alumno.getAsignaturas().isEmpty()) {
        throw new AsignaturaNotFoundException("El alumno no está inscripto en ninguna materia");
    }
    Asignatura asignatura = alumno.getAsignaturas().stream()
            .filter(a -> a.getMateria().getId() == idAsignatura)
            .findFirst()
            .orElseThrow(() -> new AsignaturaNotFoundException("Asignatura no encontrada"));
    try {
        alumno.aprobarAsignatura(asignatura.getMateria(), nota);
    } catch (EstadoIncorrectoException e) {
        throw new MateriaBadRequestException(e.getMessage());
    } catch (AsignaturaInexistenteException e) {
        throw new AsignaturaNotFoundException(e.getMessage());
    }
    return asignatura;
}

@Override
public Asignatura perderRegularidad(Alumno alumno, int idAsignatura) throws AsignaturaBadRequestException, AsignaturaNotFoundException {
    Asignatura asignatura = alumno.getAsignaturas().stream()
            .filter(a -> a.getMateria().getId() == idAsignatura)
            .findFirst()
            .orElseThrow(() -> new AsignaturaNotFoundException("Asignatura no encontrada."));
    if (asignatura.getEstado() == EstadoAsignatura.APROBADA) {
        throw new AsignaturaBadRequestException("La materia ya fue aprobada con " + asignatura.getNota());
    }
    asignatura.setEstado(EstadoAsignatura.NO_CURSADA);
    return asignatura;
}

@Override
public Asignatura buscarAsignatura(int idAsignatura, Alumno alumno) throws AsignaturaNotFoundException {
    return alumno.getAsignaturas().stream()
            .filter(a -> a.getMateria().getId() == idAsignatura)
            .findFirst()
            .orElseThrow(() -> new AsignaturaNotFoundException("Asignatura no encontrada."));
}
}
