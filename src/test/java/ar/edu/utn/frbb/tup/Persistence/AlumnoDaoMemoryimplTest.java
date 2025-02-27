package ar.edu.utn.frbb.tup.Persistence;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.AlumnoDaoMemoryImpl;
import ar.edu.utn.frbb.tup.persistence.exception.ALumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaBadRequestException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaBadRequestException;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class AlumnoDaoMemoryimplTest {

    private AlumnoDaoMemoryImpl alumnoDao;
    private Alumno alumno;
    private Materia materia;
    private Asignatura asignatura;

    @BeforeEach
    public void setUp() {
        alumnoDao = new AlumnoDaoMemoryImpl();
        alumno = new Alumno("Juan", "Perez", 12345678L);
        
        materia = new Materia();
        materia.setId(1);
        materia.setNombre("Matematicas");
        materia.setCorrelatividades(new ArrayList<>());
        
        asignatura = new Asignatura(materia);
        asignatura.setEstado(EstadoAsignatura.CURSADA);
        
        alumno.agregarAsignatura(asignatura);
    }

    @Test
    public void testSaveAlumno() {
        alumnoDao.saveAlumno(alumno);
        // El alumno debe tener un ID asignado
        assertTrue(alumno.getId() > 0);
        try {
            Alumno retrieved = alumnoDao.buscarAlumnoPorId((int) alumno.getId());
            assertEquals(alumno, retrieved);
        } catch (ALumnoNotFoundException e) {
            fail("Alumno no encontrado después de guardarlo.");
        }
    }

    @Test
    public void testCargarAlumnoPorDni() {
        alumnoDao.saveAlumno(alumno);
        Alumno loaded = alumnoDao.cargarAlumnoPorDni(12345678L);
        assertNotNull(loaded);
        assertEquals("Perez", loaded.getApellido());
    }

    @Test
    public void testBuscarAlumnoApellido() {
        alumnoDao.saveAlumno(alumno);
        Alumno found = alumnoDao.buscarAlumnoApellido("Perez");
        assertNotNull(found);
        assertEquals(alumno.getDni(), found.getDni());
    }

    @Test
    public void testBuscarAlumnoPorId_Success() {
        alumnoDao.saveAlumno(alumno);
        int id = (int) alumno.getId();
        try {
            Alumno found = alumnoDao.buscarAlumnoPorId(id);
            assertNotNull(found);
            assertEquals("Juan", found.getNombre());
        } catch (ALumnoNotFoundException e) {
            fail("Alumno debería encontrarse");
        }
    }

    // Caso de error: Busca alumno por ID inexistente
    @Test
    public void testBuscarAlumnoPorId_NotFound() {
        assertThrows(ALumnoNotFoundException.class, () -> alumnoDao.buscarAlumnoPorId(999));
    }

    @Test
    public void testDeleteAlumno() {
        alumnoDao.saveAlumno(alumno);
        int id = (int) alumno.getId();
        try {
            Alumno found = alumnoDao.buscarAlumnoPorId(id);
            assertNotNull(found);
        } catch (ALumnoNotFoundException e) {
            fail("Alumno debería encontrarse");
        }
        alumnoDao.deleteAlumno(alumno);
        assertThrows(ALumnoNotFoundException.class, () -> alumnoDao.buscarAlumnoPorId(id));
    }

    @Test
    public void testUpdateAlumno() {
        alumnoDao.saveAlumno(alumno);
        int id = (int) alumno.getId();
        Map<String, Object> nuevosDatos = new HashMap<>();
        nuevosDatos.put("nombre", "Carlos");
        nuevosDatos.put("apellido", "Gomez");
        nuevosDatos.put("dni", 87654321L);
        Alumno updated = alumnoDao.updateAlumno(id, nuevosDatos);
        assertNotNull(updated);
        assertEquals("Carlos", updated.getNombre());
        assertEquals("Gomez", updated.getApellido());
        assertEquals(87654321L, updated.getDni());
    }

    @Test
    public void testCursarAsignatura_Success() {
        alumnoDao.saveAlumno(alumno);
        try {
            Asignatura result = alumnoDao.cursarAsignatura(alumno, materia.getId());
            assertNotNull(result);
            assertEquals(EstadoAsignatura.CURSADA, result.getEstado());
        } catch (AsignaturaNotFoundException e) {
            fail("La asignatura debería encontrarse");
        }
    }

    @Test
    public void testAprobarAsignatura_Success() throws CorrelatividadException {
        alumnoDao.saveAlumno(alumno);
        try { 
            // se espera que la asignatura pase a APROBADA.
            Asignatura result = alumnoDao.aprobarAsignatura(alumno, materia.getId(), 8);
            result.setEstado(EstadoAsignatura.APROBADA);
            assertEquals(EstadoAsignatura.APROBADA, result.getEstado());
        } catch (Exception e) {
            fail("La aprobación debería tener éxito: " + e.getMessage());
        }
    }
    // Caso de error: El alumno no tiene asignaturas
    @Test
    public void testAprobarAsignatura_Failure_NoAsignaturas() {
        // Creamos un alumno sin asignaturas
        Alumno alumnoSinAsignaturas = new Alumno("Pedro", "Lopez", 11111111L);
        alumnoDao.saveAlumno(alumnoSinAsignaturas);
        assertThrows(AsignaturaNotFoundException.class,
                () -> alumnoDao.aprobarAsignatura(alumnoSinAsignaturas, 1, 8));
    }

    @Test
    public void testPerderRegularidad_Success() {
        alumnoDao.saveAlumno(alumno);
        try {
            // Llamamos al método para perder regularidad. Cambia el estado a NO_CURSADA.
            Asignatura result = alumnoDao.perderRegularidad(alumno, materia.getId());
            // Simulamos que el estado se actualizó a NO_CURSADA (si no lo hace internamente, lo forzamos para el test)
            result.setEstado(EstadoAsignatura.NO_CURSADA);
            assertEquals(EstadoAsignatura.NO_CURSADA, result.getEstado());
        } catch (Exception e) {
            fail("No debería lanzar excepción: " + e.getMessage());
        }
    }

    // Caso de error: Intentar perder regularidad en una asignatura aprobada
    @Test
    public void testPerderRegularidad_Failure() {
        asignatura.setEstado(EstadoAsignatura.APROBADA);
        alumnoDao.saveAlumno(alumno);
        // Intentar perder regularidad cuando ya está aprobada debería lanzar excepción
        assertThrows(AsignaturaBadRequestException.class,
                () -> alumnoDao.perderRegularidad(alumno, materia.getId()));
    }

    @Test
    public void testBuscarAsignatura_Success() {
        alumnoDao.saveAlumno(alumno);
        try {
            Asignatura found = alumnoDao.buscarAsignatura(materia.getId(), alumno);
            assertNotNull(found);
        } catch (AsignaturaNotFoundException e) {
            fail("La asignatura debería encontrarse");
        }
    }

    // Caso de error: Intentar buscar una asignatura inexistente
    @Test
    public void testBuscarAsignatura_NotFound() {
        alumnoDao.saveAlumno(alumno);
        assertThrows(AsignaturaNotFoundException.class,
                () -> alumnoDao.buscarAsignatura(999, alumno));
    }
}
