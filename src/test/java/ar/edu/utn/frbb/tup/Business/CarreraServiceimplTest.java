package ar.edu.utn.frbb.tup.Business;
import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.business.impl.CarreraServiceImpl;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.persistence.CarreraDao;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraBadRequestException;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarreraServiceimplTest {

    @InjectMocks
    private CarreraServiceImpl carreraService;

    @Mock
    private CarreraDao carreraDao;

    @Mock
    private MateriaService materiaService;

    private Carrera carrera;
    private Materia materia1;
    private Materia materia2;

    @BeforeEach
    public void setUp() {
        carrera = new Carrera();
        carrera.setId(1);
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

    // Crear carrera sin materias (lista vacía)
    @Test
    public void testCrearCarrera_Success_WithEmptyMaterias() 
            throws MateriaNotFoundException, CarreraBadRequestException, CarreraNotFoundException {
        CarreraDto dto = new CarreraDto();
        dto.setNombre("Ingeniería en Sistemas");
        dto.setDepartamentoId(1);
        dto.setCantidadCuatrimestres(10);
        dto.setMateriaIds(Collections.emptyList());

        when(carreraDao.crearCarrera(any(Carrera.class))).thenReturn(carrera);

        Carrera resultado = carreraService.crearCarrera(dto);

        assertNotNull(resultado);
        assertEquals("Ingeniería en Sistemas", resultado.getNombre());
        verify(carreraDao, times(1)).crearCarrera(any(Carrera.class));
    }

    // Crear carrera con materias
    @Test
    public void testCrearCarrera_Success_WithMaterias() 
            throws MateriaNotFoundException, CarreraBadRequestException, CarreraNotFoundException {
        CarreraDto dto = new CarreraDto();
        dto.setNombre("Ingeniería en Sistemas");
        dto.setDepartamentoId(1);
        dto.setCantidadCuatrimestres(10);
        dto.setMateriaIds(Arrays.asList(1, 2));

        // Stub de materiaService para retornar las materias solicitadas
        when(materiaService.getMateriaPorId(1)).thenReturn(materia1);
        when(materiaService.getMateriaPorId(2)).thenReturn(materia2);

        // Se simula que al crear la carrera con materias se retorna un objeto actualizado
        Carrera carreraConMaterias = new Carrera();
        carreraConMaterias.setId(1);
        carreraConMaterias.setNombre("Ingeniería en Sistemas");
        carreraConMaterias.setCantidadCuatrimestres(10);
        carreraConMaterias.setIdDepartamento(1);
        Set<Materia> materiasSet = new HashSet<>();
        materiasSet.add(materia1);
        materiasSet.add(materia2);
        carreraConMaterias.setMaterias(materiasSet);

        when(carreraDao.crearCarreraConMaterias(any(List.class), any(Carrera.class))).thenReturn(carreraConMaterias);

        Carrera resultado = carreraService.crearCarrera(dto);

        assertNotNull(resultado);
        assertEquals("Ingeniería en Sistemas", resultado.getNombre());
        assertEquals(2, resultado.getMaterias().size());
        verify(carreraDao, times(1)).crearCarreraConMaterias(any(List.class), any(Carrera.class));
    }

    // Obtener todas las carreras
    @Test
    public void testGetAllCarreras_Success() throws CarreraNotFoundException {
        Carrera carrera2 = new Carrera();
        carrera2.setId(2);
        carrera2.setNombre("Ingeniería Industrial");

        when(carreraDao.getAllCarreras()).thenReturn(Arrays.asList(carrera, carrera2));

        List<Carrera> resultados = carreraService.getAllCarreras();

        assertNotNull(resultados);
        assertEquals(2, resultados.size());
        verify(carreraDao, times(1)).getAllCarreras();
    }

    // Modificar una carrera
    @Test
    public void testModificarCarrera_Success() 
            throws CarreraNotFoundException, CarreraBadRequestException, MateriaNotFoundException {
        Map<String, Object> nuevosDatos = new HashMap<>();
        nuevosDatos.put("nombre", "Ingeniería en Computación");

        Carrera carreraModificada = new Carrera();
        carreraModificada.setId(1);
        carreraModificada.setNombre("Ingeniería en Computación");

        when(carreraDao.updateCarrera(eq(1), any(Map.class))).thenReturn(carreraModificada);

        Carrera resultado = carreraService.modificarCarrera(nuevosDatos, 1);

        assertNotNull(resultado);
        assertEquals("Ingeniería en Computación", resultado.getNombre());
        verify(carreraDao, times(1)).updateCarrera(eq(1), any(Map.class));
    }

    // Obtener carrera por ID
    @Test
    public void testGetCarreraPorId_Success() throws CarreraNotFoundException {
        when(carreraDao.getCarreraPorId(1)).thenReturn(carrera);

        Carrera resultado = carreraService.getCarreraPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        verify(carreraDao, times(1)).getCarreraPorId(1);
    }

    // Caso de error: Obtener carrera por ID inexistente
    @Test
    public void testGetCarreraPorId_NotFound() throws CarreraNotFoundException {
        when(carreraDao.getCarreraPorId(99))
                .thenThrow(new CarreraNotFoundException("No se encontró la carrera con ID 99"));

        assertThrows(CarreraNotFoundException.class, () -> carreraService.getCarreraPorId(99));
        verify(carreraDao, times(1)).getCarreraPorId(99);
    }

    // Eliminar una carrera
    @Test
    public void testEliminarCarrera_Success() throws CarreraNotFoundException, MateriaNotFoundException {
        
        Carrera carreraConMaterias = new Carrera();
        carreraConMaterias.setId(1);
        carreraConMaterias.setNombre("Ingeniería en Sistemas");
        Set<Materia> materiasSet = new HashSet<>();
        materiasSet.add(materia1);
        materiasSet.add(materia2);
        carreraConMaterias.setMaterias(materiasSet);

        when(carreraDao.getCarreraPorId(1)).thenReturn(carreraConMaterias);
        when(carreraDao.eliminarCarrera(carreraConMaterias)).thenReturn(carreraConMaterias);
        when(materiaService.borrarMateria(anyInt())).thenReturn(null);

        Carrera resultado = carreraService.eliminarCarrera(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        verify(carreraDao, times(1)).getCarreraPorId(1);
        verify(carreraDao, times(1)).eliminarCarrera(carreraConMaterias);
        // Verificamos que se haya llamado borrarMateria para cada materia
        verify(materiaService, times(materiasSet.size())).borrarMateria(anyInt());
    }

    // Caso de error: Eliminar carrera inexistente
    @Test
    public void testEliminarCarrera_NotFound() throws CarreraNotFoundException {
        when(carreraDao.getCarreraPorId(99))
                .thenThrow(new CarreraNotFoundException("No se encontró la carrera con ID: 99"));

        assertThrows(CarreraNotFoundException.class, () -> carreraService.eliminarCarrera(99));
        verify(carreraDao, times(1)).getCarreraPorId(99);
    }

    // Agregar materia a una carrera
@Test
public void testAgregarMateria_Success() throws CarreraNotFoundException, CarreraBadRequestException, MateriaNotFoundException {
    Carrera carreraSinMaterias = new Carrera();
    carreraSinMaterias.setId(1);
    carreraSinMaterias.setNombre("Ingeniería en Sistemas");

    Carrera carreraConMateria = new Carrera();
    carreraConMateria.setId(1);
    carreraConMateria.setNombre("Ingeniería en Sistemas");
    Set<Materia> materiasSet = new HashSet<>();
    materiasSet.add(materia1);
    carreraConMateria.setMaterias(materiasSet);

    when(carreraDao.agregarMateria(materia1, carreraSinMaterias)).thenReturn(carreraConMateria);

    Carrera resultado = carreraService.agregarMateria(carreraSinMaterias, materia1);

    assertNotNull(resultado);
    assertEquals(1, resultado.getMaterias().size());
    verify(carreraDao, times(1)).agregarMateria(materia1, carreraSinMaterias);
}

}
