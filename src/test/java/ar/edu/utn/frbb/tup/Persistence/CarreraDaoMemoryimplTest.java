package ar.edu.utn.frbb.tup.Persistence;
import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.CarreraDaoMemoryImpl;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraBadRequestException;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.lang.reflect.Field;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarreraDaoMemoryimplTest {

    @Mock
    private MateriaService materiaService;

    private CarreraDaoMemoryImpl carreraDao;
    @Mock
    
    private MateriaDao materiaDaoMock;
    private Carrera carrera;
    private Materia materia1;
    private Materia materia2;

    @BeforeEach
    public void setUp() {
        // Instanciamos la implementación pasando el mock de MateriaService.
        carreraDao = new CarreraDaoMemoryImpl(materiaService);

        carrera = new Carrera();
        carrera.setNombre("Ingeniería en Sistemas");
        carrera.setCantidadCuatrimestres(10);
        carrera.setIdDepartamento(1);

        materia1 = new Materia();
        materia1.setId(1);
        materia1.setNombre("Matemáticas");

        materia2 = new Materia();
        materia2.setId(2);
        materia2.setNombre("Física");
    }

    @Test
    public void testCrearCarrera_Success() throws CarreraBadRequestException {
        Carrera creada = carreraDao.crearCarrera(carrera);
        assertNotNull(creada);
        assertTrue(creada.getId() > 0);
        assertNotNull(creada.getCodigo());
        List<Carrera> carreras = carreraDao.getAllCarreras();
        assertEquals(1, carreras.size());
    }

    @Test
    public void testCrearCarrera_Duplicate() throws CarreraBadRequestException {
        carreraDao.crearCarrera(carrera);
        Exception exception = assertThrows(CarreraBadRequestException.class, () -> carreraDao.crearCarrera(carrera));
        assertTrue(exception.getMessage().contains("Ya existe dicha carrera."));
    }

    @Test
    public void testGetCarreraPorId_Success() throws CarreraNotFoundException, CarreraBadRequestException {
        Carrera creada = carreraDao.crearCarrera(carrera);
        Carrera encontrada = carreraDao.getCarreraPorId(creada.getId());
        assertNotNull(encontrada);
        assertEquals(creada.getNombre(), encontrada.getNombre());
    }

    @Test
    public void testGetCarreraPorId_Failure() {
        Exception exception = assertThrows(CarreraNotFoundException.class, () -> carreraDao.getCarreraPorId(999));
        assertTrue(exception.getMessage().contains("No se encontró la carrera deseada"));
    }

    @Test
    public void testUpdateCarrera_Success() throws CarreraNotFoundException, CarreraBadRequestException, MateriaNotFoundException {
        Carrera creada = carreraDao.crearCarrera(carrera);
        Map<String, Object> nuevosDatos = new HashMap<>();
        nuevosDatos.put("nombre", "Ingeniería en Computación");
        nuevosDatos.put("departamentoId", 2);
        nuevosDatos.put("cantidadCuatrimestres", 8);
        Carrera actualizada = carreraDao.updateCarrera(creada.getId(), nuevosDatos);
        assertNotNull(actualizada);
        assertEquals("Ingeniería en Computación", actualizada.getNombre());
        assertEquals(2, actualizada.getIdDepartamento());
        assertEquals(8, actualizada.getCantidadCuatrimestres());
    }

    @Test
    public void testUpdateCarrera_Failure() throws CarreraNotFoundException, CarreraBadRequestException {
        Carrera creada = carreraDao.crearCarrera(carrera);
        Map<String, Object> nuevosDatos = new HashMap<>();
        nuevosDatos.put("blablabla", "valor");
        Exception exception = assertThrows(CarreraBadRequestException.class, () -> carreraDao.updateCarrera(creada.getId(), nuevosDatos));
        assertTrue(exception.getMessage().contains("El campo 'blablabla' no es válido"));
    }

    @Test
    public void testEliminarCarrera_Success() throws CarreraNotFoundException, CarreraBadRequestException {
        Carrera creada = carreraDao.crearCarrera(carrera);
        Carrera eliminada = carreraDao.eliminarCarrera(creada);
        assertNotNull(eliminada);
        assertEquals(creada.getId(), eliminada.getId());
        Exception exception = assertThrows(CarreraNotFoundException.class, () -> carreraDao.getCarreraPorId(creada.getId()));
        assertTrue(exception.getMessage().contains("No se encontró la carrera deseada"));
    }

    @Test
    public void testEliminarCarrera_Failure() {
        Carrera noExistente = new Carrera();
        noExistente.setId(999);
        Exception exception = assertThrows(CarreraNotFoundException.class, () -> carreraDao.eliminarCarrera(noExistente));
        assertTrue(exception.getMessage().contains("No se encontró la carrera que busca eliminar"));
    }

    @Test
    public void testCrearCarreraConMaterias_Success() throws CarreraBadRequestException, CarreraNotFoundException, MateriaNotFoundException, NoSuchFieldException, IllegalAccessException {
        // inyectamos un mock de MateriaDao en CarreraDaoMemoryImpl.
        List<Materia> listaMaterias = Arrays.asList(materia1, materia2);
        Field field = CarreraDaoMemoryImpl.class.getDeclaredField("materiaDao");
        field.setAccessible(true);
        field.set(carreraDao, materiaDaoMock);

        when(materiaDaoMock.buscarMateriaPorId(1)).thenReturn(materia1);
        when(materiaDaoMock.buscarMateriaPorId(2)).thenReturn(materia2);

        Carrera creada = carreraDao.crearCarreraConMaterias(listaMaterias, carrera);
        assertNotNull(creada);
        assertNotNull(creada.getMaterias());
        assertEquals(2, creada.getMaterias().size());
    }

    @Test
    public void testAgregarMateria_Success() throws CarreraNotFoundException, CarreraBadRequestException, MateriaNotFoundException, NoSuchFieldException, IllegalAccessException {
        Field field = CarreraDaoMemoryImpl.class.getDeclaredField("materiaDao");
        field.setAccessible(true);
        field.set(carreraDao, materiaDaoMock);

        carrera.setId(1);
        carreraDao.crearCarrera(carrera);
        

        when(materiaDaoMock.buscarMateriaPorId(1)).thenReturn(materia1);
        
        Carrera resultado = carreraDao.agregarMateria(materia1, carrera);
        assertNotNull(resultado);
        assertTrue(resultado.getMaterias().contains(materia1));
        verify(materiaDaoMock, times(1)).buscarMateriaPorId(1);
    }
    
}
