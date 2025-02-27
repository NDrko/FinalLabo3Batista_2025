package ar.edu.utn.frbb.tup.Business;
import ar.edu.utn.frbb.tup.business.impl.MateriaServiceImpl;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaBadRequestException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MateriaServiceimplTest {

    @InjectMocks
    private MateriaServiceImpl materiaService;

    @Mock
    private MateriaDao materiaDao;

    private MateriaDto materiaDto;
    private Materia materia;

    @BeforeEach
    public void setUp(){
        materiaDto = new MateriaDto();
        materiaDto.setNombre("Matemáticas");
        materiaDto.setAnio(2023);
        materiaDto.setCuatrimestre(1);
        materiaDto.setProfesorId(1L);
        materiaDto.setCarreraId(null);

        materia = new Materia("Matemáticas", 2023, 1, null);
        materia.setId(1);
    }

    // crear materia
    @Test
    public void testCrearMateria_Success() throws MateriaBadRequestException {
        when(materiaDao.guardarMateria(any(Materia.class))).thenReturn(materia);
        Materia result = materiaService.crearMateria(materiaDto);
        assertNotNull(result);
        assertEquals("Matemáticas", result.getNombre());
        assertEquals(2023, result.getAnio());
        assertEquals(1, result.getCuatrimestre());
        verify(materiaDao, times(1)).guardarMateria(any(Materia.class));
    }

    // obtener todas las materias
    @Test
    public void testGetAllMaterias_Success() throws MateriaNotFoundException {
        List<Materia> lista = new ArrayList<>();
        lista.add(materia);
        when(materiaDao.getAllMaterias()).thenReturn(lista);
        List<Materia> result = materiaService.getAllMaterias();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(materiaDao, times(1)).getAllMaterias();
    }

    // obtener materia por ID
    @Test
    public void testGetMateriaPorId_Success() throws MateriaNotFoundException {
        when(materiaDao.buscarMateriaPorId(1)).thenReturn(materia);
        Materia result = materiaService.getMateriaPorId(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(materiaDao, times(1)).buscarMateriaPorId(1);
    }

    // Caso de error: borrar materia inexistente
    @Test
    public void testBorrarMateria_NotFound() throws MateriaNotFoundException {
        when(materiaDao.buscarMateriaPorId(1)).thenReturn(null);
        Exception exception = assertThrows(MateriaNotFoundException.class, () -> materiaService.borrarMateria(1));
        assertEquals("Materia no encontrada", exception.getMessage());
        verify(materiaDao, times(1)).buscarMateriaPorId(1);
    }

    // borrar materia
    @Test
    public void testBorrarMateria_Success() throws MateriaNotFoundException {
        when(materiaDao.buscarMateriaPorId(1)).thenReturn(materia);
        when(materiaDao.deleteMateria(materia)).thenReturn(materia);
        Materia result = materiaService.borrarMateria(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(materiaDao, times(1)).buscarMateriaPorId(1);
        verify(materiaDao, times(1)).deleteMateria(materia);
    }

    // modificar materia
    @Test
    public void testModificarMateria_Success() throws MateriaNotFoundException, MateriaBadRequestException {
        Map<String, Object> nuevosDatos = new HashMap<>();
        nuevosDatos.put("nombre", "Física");
        Materia materiaModificada = new Materia("Física", 2023, 1, null);
        materiaModificada.setId(1);
        when(materiaDao.updateMateria(1, nuevosDatos)).thenReturn(materiaModificada);
        Materia result = materiaService.modificarMateria(nuevosDatos, 1);
        assertNotNull(result);
        assertEquals("Física", result.getNombre());
        verify(materiaDao, times(1)).updateMateria(1, nuevosDatos);
    }

    // ordenar materias
    @Test
    public void testOrdenarMaterias_Success() throws MateriaBadRequestException {
        List<Materia> listaOrdenada = Arrays.asList(materia);
        when(materiaDao.getMateriasOrdenadas("nombre")).thenReturn(listaOrdenada);
        List<Materia> result = materiaService.ordenarMaterias("nombre");
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(materiaDao, times(1)).getMateriasOrdenadas("nombre");
    }

    // filtrar materia por nombre
    @Test
    public void testFiltrarPorNombre_Success() throws MateriaNotFoundException {
        when(materiaDao.buscarMateriaPorNombre("Matemáticas")).thenReturn(materia);
        Materia result = materiaService.filtrarPorNombre("Matemáticas");
        assertNotNull(result);
        assertEquals("Matemáticas", result.getNombre());
        verify(materiaDao, times(1)).buscarMateriaPorNombre("Matemáticas");
    }

    // asignar carrera a materia
    @Test
    public void testAsignarCarrera_Success() throws MateriaBadRequestException {
        Carrera carrera = new Carrera();
        carrera.setId(1);
        carrera.setNombre("Ingeniería en Sistemas");
        when(materiaDao.asignarCarrera(materia, carrera)).thenReturn(materia);
        Materia result = materiaService.asignarCarrera(carrera, materia);
        assertNotNull(result);
        verify(materiaDao, times(1)).asignarCarrera(materia, carrera);
    }
}
