package ar.edu.utn.frbb.tup.Business;
import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.business.impl.AlumnoServiceImpl;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.exception.*;
import ar.edu.utn.frbb.tup.persistence.AlumnoDao;
import ar.edu.utn.frbb.tup.persistence.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AlumnoServiceimplTest {

    @InjectMocks
    private AlumnoServiceImpl alumnoService;

    @Mock
    private AlumnoDao alumnoDao;

    @Mock
    private AsignaturaService asignaturaService;

    private Alumno alumno;
    private Materia materia;
    private Asignatura asignatura;

    @BeforeEach
    void setUp() {
        alumno = new Alumno("Juan", "Perez", 12345678L);
        alumno.setId(1);

        materia = new Materia();
        materia.setId(1);
        materia.setNombre("Matemáticas");
        materia.setCorrelatividades(new ArrayList<>());

        asignatura = new Asignatura(materia);
        asignatura.setEstado(EstadoAsignatura.CURSADA);

        alumno.getAsignaturas().add(asignatura);
    }

    // Caso feliz: Crear un alumno
@Test
public void testCrearAlumno_Success() {
    AlumnoDto dto = new AlumnoDto("Juan", "Perez", 12345678L);

    doAnswer(invocation -> {
        Alumno alumno = invocation.getArgument(0);
        assertEquals("Juan", alumno.getNombre());
        assertEquals("Perez", alumno.getApellido());
        assertEquals(12345678L, alumno.getDni());
        return null;
    }).when(alumnoDao).saveAlumno(any(Alumno.class));

    Alumno nuevoAlumno = alumnoService.crearAlumno(dto);
    assertNotNull(nuevoAlumno);

    verify(alumnoDao, times(1)).saveAlumno(any(Alumno.class));
}

    // Caso feliz: Buscar alumno por ID
    @Test
    public void testBuscarPorId_Success() throws ALumnoNotFoundException {
        when(alumnoDao.buscarAlumnoPorId(1)).thenReturn(alumno);

        Alumno resultado = alumnoService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        verify(alumnoDao, times(1)).buscarAlumnoPorId(1);
    }

    // Caso de error: Buscar alumno por ID inexistente
    @Test
    public void testBuscarPorId_NotFound() throws ALumnoNotFoundException {
        when(alumnoDao.buscarAlumnoPorId(99)).thenReturn(null);

        assertThrows(ALumnoNotFoundException.class, () -> alumnoService.buscarPorId(99));
        verify(alumnoDao, times(1)).buscarAlumnoPorId(99);
    }

    // Caso feliz: Editar alumno
    @Test
    public void testEditarAlumno_Success() {
        Map<String, Object> nuevosDatos = new HashMap<>();
        nuevosDatos.put("nombre", "Carlos");

        Alumno alumnoEditado = new Alumno("Carlos", "Perez", 12345678L);
        alumnoEditado.setId(1);

        when(alumnoDao.updateAlumno(eq(1), anyMap())).thenReturn(alumnoEditado);

        Alumno resultado = alumnoService.editarAlumno(1, nuevosDatos);

        assertNotNull(resultado);
        assertEquals("Carlos", resultado.getNombre());
        verify(alumnoDao, times(1)).updateAlumno(eq(1), anyMap());
    }

    // Caso feliz: Aprobar asignatura con nota válida
    @Test
    public void testAprobarAsignatura_Success() throws Exception, CorrelatividadException, EstadoIncorrectoException, CorrelatividadesNoAprobadasException {
        when(alumnoDao.buscarAlumnoPorId(1)).thenReturn(alumno);
        when(alumnoDao.buscarAsignatura(1, alumno)).thenReturn(asignatura);
        
        // Simulamos que, tras aprobar la asignatura, su estado cambia a APROBADA.
        asignatura.setEstado(EstadoAsignatura.APROBADA);
        
        when(alumnoDao.aprobarAsignatura(alumno, 1, 7)).thenReturn(asignatura);
    
        Asignatura resultado = alumnoService.aprobarAsignatura(1, 7, 1);
    
        assertNotNull(resultado);
        assertEquals(EstadoAsignatura.APROBADA, resultado.getEstado());
        verify(alumnoDao, times(1)).aprobarAsignatura(alumno, 1, 7);
    }
    

    // Caso de error: Aprobar asignatura con nota inválida
    @Test
    public void testAprobarAsignatura_Failure_InvalidNota() throws ALumnoNotFoundException {
        assertThrows(AsignaturaBadRequestException.class, () -> alumnoService.aprobarAsignatura(1, 5, 1));
    }

    // Caso feliz: Cursar una asignatura
    @Test
    public void testCursarAsignatura_Success() throws Exception {
        when(alumnoDao.buscarAlumnoPorId(1)).thenReturn(alumno);
        when(alumnoDao.cursarAsignatura(alumno, 1)).thenReturn(asignatura);

        Asignatura resultado = alumnoService.cursarAsignatura(1, 1);

        assertNotNull(resultado);
        assertEquals(EstadoAsignatura.CURSADA, resultado.getEstado());
        verify(alumnoDao, times(1)).cursarAsignatura(alumno, 1);
    }

    // Caso de error: Intentar cursar una asignatura inexistente
    @Test
    public void testCursarAsignatura_NotFound() throws ALumnoNotFoundException, AsignaturaNotFoundException {
        when(alumnoDao.buscarAlumnoPorId(1)).thenReturn(alumno);
        when(alumnoDao.cursarAsignatura(alumno, 99)).thenThrow(new AsignaturaNotFoundException("Asignatura no encontrada"));

        assertThrows(AsignaturaNotFoundException.class, () -> alumnoService.cursarAsignatura(1, 99));
    }

    // Caso feliz: Recursar una asignatura
    @Test
    public void testRecursarAsignatura_Success() throws Exception {
        when(alumnoDao.buscarAlumnoPorId(1)).thenReturn(alumno);
        asignatura.setEstado(EstadoAsignatura.NO_CURSADA);
        when(alumnoDao.perderRegularidad(alumno, 1)).thenReturn(asignatura);

        Asignatura resultado = alumnoService.recursarAsignatura(1, 1);

        assertNotNull(resultado);
        assertEquals(EstadoAsignatura.NO_CURSADA, resultado.getEstado());
        verify(alumnoDao, times(1)).perderRegularidad(alumno, 1);
    }

    // Caso de error: Intentar recursar una asignatura inexistente
    @Test
    public void testRecursarAsignatura_NotFound() throws AsignaturaBadRequestException, AsignaturaNotFoundException, ALumnoNotFoundException {
        when(alumnoDao.buscarAlumnoPorId(1)).thenReturn(alumno);
        when(alumnoDao.perderRegularidad(alumno, 99)).thenThrow(new AsignaturaNotFoundException("Asignatura no encontrada"));

        assertThrows(AsignaturaNotFoundException.class, () -> alumnoService.recursarAsignatura(1, 99));
    }
}
