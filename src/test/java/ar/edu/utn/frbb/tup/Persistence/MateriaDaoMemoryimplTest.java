package ar.edu.utn.frbb.tup.Persistence;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.MateriaDaoMemoryImpl;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaBadRequestException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class MateriaDaoMemoryimplTest {
    private MateriaDaoMemoryImpl materiaDao;
    private Materia materia;
    
    @BeforeEach
    public void setUp() {
        materiaDao = new MateriaDaoMemoryImpl();
        materia = new Materia();
        materia.setNombre("Matemáticas");
        materia.setAnio(2023);
        materia.setCuatrimestre(1);
    }

    // Test para guardar materia exitosamente
    @Test
    public void testGuardarMateria_Success() throws MateriaBadRequestException, MateriaNotFoundException {
        Materia guardada = materiaDao.guardarMateria(materia);
        assertNotNull(guardada);
        assertTrue(guardada.getId() > 0);
        assertNotNull(guardada.getCodigo());
        
        // Verificamos que se pueda recuperar mediante buscarMateriaPorId
        Materia recuperada = materiaDao.buscarMateriaPorId(guardada.getId());
        assertEquals("Matemáticas", recuperada.getNombre());
    }
    
    // Caso de error:guardar materia con nombre inválido
    @Test
    public void testGuardarMateria_Failure() {
        Materia m = new Materia();
        m.setNombre("   "); // nombre vacío o solo espacios
        
        Exception exception = assertThrows(MateriaBadRequestException.class, () -> {
            materiaDao.guardarMateria(m);
        });
        assertEquals("El nombre de la materia no puede estar vacío.", exception.getMessage());
    }
    
    //  buscar materia por ID exitosamente
    @Test
    public void testBuscarMateriaPorId_Success() throws MateriaBadRequestException, MateriaNotFoundException {
        Materia guardada = materiaDao.guardarMateria(materia);
        Materia encontrada = materiaDao.buscarMateriaPorId(guardada.getId());
        assertNotNull(encontrada);
        assertEquals(guardada.getNombre(), encontrada.getNombre());
    }
    
    // Caso de error:buscar materia por ID inexistente
    @Test
    public void testBuscarMateriaPorId_NotFound() {
        assertThrows(MateriaNotFoundException.class, () -> materiaDao.buscarMateriaPorId(999));
    }
    
    //eliminar materia exitosamente
    @Test
    public void testDeleteMateria_Success() throws MateriaBadRequestException, MateriaNotFoundException {
        Materia guardada = materiaDao.guardarMateria(materia);
        Materia eliminada = materiaDao.deleteMateria(guardada);
        assertNotNull(eliminada);
        assertEquals(guardada.getId(), eliminada.getId());
        // Al buscar nuevamente debe lanzar excepción
        assertThrows(MateriaNotFoundException.class, () -> materiaDao.buscarMateriaPorId(guardada.getId()));
    }
     
    // Test para actualizar materia exitosamente
    @Test
    public void testUpdateMateria_Success() throws MateriaBadRequestException, MateriaNotFoundException {
        Materia guardada = materiaDao.guardarMateria(materia);
        int id = guardada.getId();
        
        Map<String, Object> nuevosDatos = new HashMap<>();
        nuevosDatos.put("nombre", "Física");
        nuevosDatos.put("anio", 2024);
        nuevosDatos.put("cuatrimestre", 2);
        
        Materia actualizada = materiaDao.updateMateria(id, nuevosDatos);
        assertNotNull(actualizada);
        assertEquals("Física", actualizada.getNombre());
        assertEquals(2024, actualizada.getAnio());
        assertEquals(2, actualizada.getCuatrimestre());
    }
    
    // Caso de error: actualizar materia con campo inválido
    @Test
    public void testUpdateMateria_Failure() throws MateriaBadRequestException, MateriaNotFoundException {
        Materia guardada = materiaDao.guardarMateria(materia);
        int id = guardada.getId();
        Map<String, Object> nuevosDatos = new HashMap<>();
        nuevosDatos.put("invalidField", "valor");
        
        Exception exception = assertThrows(MateriaBadRequestException.class, () -> {
            materiaDao.updateMateria(id, nuevosDatos);
        });
        assertTrue(exception.getMessage().contains("Campo 'invalidField' no válido"));
    }
    
    // buscar materia por nombre exitosamente
    @Test
    public void testBuscarMateriaPorNombre_Success() throws MateriaBadRequestException, MateriaNotFoundException {
        Materia guardada = materiaDao.guardarMateria(materia);
        Materia encontrada = materiaDao.buscarMateriaPorNombre("Matemáticas");
        assertNotNull(encontrada);
        assertEquals(guardada.getId(), encontrada.getId());
    }
    
    // Caso de error: buscar materia por nombre inexistente
    @Test
    public void testBuscarMateriaPorNombre_Failure() {
        assertThrows(MateriaNotFoundException.class, () -> materiaDao.buscarMateriaPorNombre("Historia"));
    }
    
    // Test para asignar carrera a una materia
    @Test
    public void testAsignarCarrera_Success() throws MateriaBadRequestException {
        Carrera carrera = new Carrera();
        carrera.setId(1);
        carrera.setNombre("Ingeniería en Sistemas");
        
        Materia result = materiaDao.asignarCarrera(materia, carrera);
        assertNotNull(result);
        assertEquals(carrera, result.getCarrera());
    }
    
    // Caso de error: asignar carrera con materia o carrera nula
    @Test
    public void testAsignarCarrera_Failure() {
        Carrera carrera = new Carrera();
        carrera.setId(1);
        carrera.setNombre("Ingeniería en Sistemas");
        assertThrows(MateriaBadRequestException.class, () -> materiaDao.asignarCarrera(null, carrera));
        assertThrows(MateriaBadRequestException.class, () -> materiaDao.asignarCarrera(materia, null));
    }
    
    // obtener materias ordenadas
    @Test
    public void testGetMateriasOrdenadas_Success() throws MateriaBadRequestException, MateriaBadRequestException, MateriaNotFoundException {
        // Guardamos dos materias con nombres y años distintos
        Materia m1 = new Materia();
        m1.setNombre("Algebra");
        m1.setAnio(2023);
        m1.setCuatrimestre(1);
        materiaDao.guardarMateria(m1);
        
        Materia m2 = new Materia();
        m2.setNombre("Cálculo");
        m2.setAnio(2022);
        m2.setCuatrimestre(2);
        materiaDao.guardarMateria(m2);
        
        // Orden por nombre
        List<Materia> ordenadas = materiaDao.getMateriasOrdenadas("nombre");
        assertNotNull(ordenadas);
        assertTrue(ordenadas.get(0).getNombre().compareToIgnoreCase(ordenadas.get(1).getNombre()) <= 0);
        
        // Orden por anio
        List<Materia> ordenadasAnio = materiaDao.getMateriasOrdenadas("anio");
        assertNotNull(ordenadasAnio);
        assertTrue(ordenadasAnio.get(0).getAnio() <= ordenadasAnio.get(1).getAnio());
        
        // Orden por cuatrimestre
        List<Materia> ordenadasCuat = materiaDao.getMateriasOrdenadas("cuatrimestre");
        assertNotNull(ordenadasCuat);
        assertTrue(ordenadasCuat.get(0).getCuatrimestre() <= ordenadasCuat.get(1).getCuatrimestre());
    }
    
    // Caso de error: obtener materias ordenadas con criterio inválido
    @Test
    public void testGetMateriasOrdenadas_Failure() throws MateriaBadRequestException {
        materiaDao.guardarMateria(materia);
        Exception exception = assertThrows(MateriaBadRequestException.class, () -> materiaDao.getMateriasOrdenadas("invalid"));
        assertTrue(exception.getMessage().contains("El orden no es válido"));
    }
    
    // Traer todas las materias
    @Test
    public void testGetAllMaterias_Success() throws MateriaBadRequestException, MateriaNotFoundException {
        materiaDao.guardarMateria(materia);
        List<Materia> lista = materiaDao.getAllMaterias();
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }
    
    // Caso de error: obtener todas las materias cuando no hay ninguna registrada
    @Test
    public void testGetAllMaterias_Empty() {
        Exception exception = assertThrows(MateriaNotFoundException.class, () -> materiaDao.getAllMaterias());
        assertTrue(exception.getMessage().contains("No hay materias registradas"));
    }  
}
