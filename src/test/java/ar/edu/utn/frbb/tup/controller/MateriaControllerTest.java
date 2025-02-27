package ar.edu.utn.frbb.tup.controller;
import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaBadRequestException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MateriaController.class)
public class MateriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MateriaService materiaService;

    @Autowired
    private ObjectMapper objectMapper;

    // Obtener todas las materias
    @Test
    public void testGetMaterias_Success() throws Exception {
        Materia materia1 = new Materia("Matematica", 2023, 1, null);
        materia1.setId(1);
        Materia materia2 = new Materia("Fisica", 2023, 1, null);
        materia2.setId(2);

        Mockito.when(materiaService.getAllMaterias()).thenReturn(Arrays.asList(materia1, materia2));

        mockMvc.perform(get("/materias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    // Obtener materias ordenadas
    @Test
    public void testGetMateriasOrdenadas_Success() throws Exception {
        Materia materia1 = new Materia("Fisica", 2023, 1, null);
        materia1.setId(2);
        Materia materia2 = new Materia("Matematica", 2023, 1, null);
        materia2.setId(1);

        Mockito.when(materiaService.ordenarMaterias("nombre"))
                .thenReturn(Arrays.asList(materia1, materia2));

        mockMvc.perform(get("/materias/ordenadas").param("ordenamiento", "nombre"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[1].id").value(1));
    }

    // Filtrar materia por nombre
    @Test
    public void testFiltrarPorNombre_Success() throws Exception {
        Materia materia = new Materia("Quimica", 2023, 1, null);
        materia.setId(3);

        Mockito.when(materiaService.filtrarPorNombre("Quimica")).thenReturn(materia);

        mockMvc.perform(get("/materias/filtro").param("nombre", "Quimica"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.nombre").value("Quimica"));
    }

    // Obtener materia por ID
    @Test
    public void testGetMateriaById_Success() throws Exception {
        Materia materia = new Materia("Historia", 2023, 1, null);
        materia.setId(4);

        Mockito.when(materiaService.getMateriaPorId(4)).thenReturn(materia);

        mockMvc.perform(get("/materias/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.nombre").value("Historia"));
    }

    // Caso de error: Obtener materia inexistente
    @Test
    public void testGetMateriaById_NotFound() throws Exception {
        Mockito.when(materiaService.getMateriaPorId(99))
                .thenThrow(new MateriaNotFoundException("Materia no encontrada"));

        mockMvc.perform(get("/materias/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("Materia no encontrada"));
    }

    //  Crear materia
    @Test
    public void testCrearMateria_Success() throws Exception {
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setNombre("Geografia");
        materiaDto.setAnio(2023);
        materiaDto.setCuatrimestre(1);
        materiaDto.setProfesorId(1L);
        materiaDto.setCarreraId(null);

        Materia materia = new Materia("Geografia", 2023, 1, null);
        materia.setId(5);

        Mockito.when(materiaService.crearMateria(any(MateriaDto.class))).thenReturn(materia);

        mockMvc.perform(post("/materias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(materiaDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.nombre").value("Geografia"));
    }

    // Caso de error: Crear materia con datos inválidos
    @Test
    public void testCrearMateria_Failure_InvalidData() throws Exception {
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setNombre("");
        materiaDto.setAnio(0);
        materiaDto.setCuatrimestre(0);

        Mockito.when(materiaService.crearMateria(any(MateriaDto.class)))
                .thenThrow(new MateriaBadRequestException("Datos inválidos"));

        mockMvc.perform(post("/materias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(materiaDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Datos inválidos"));
    }

    //  Borrar materia
    @Test
    public void testBorrarMateria_Success() throws Exception {
        Materia materia = new Materia("Economia", 2023, 1, null);
        materia.setId(6);

        Mockito.when(materiaService.borrarMateria(6)).thenReturn(materia);

        mockMvc.perform(delete("/materias/6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(6));
    }

    //  Modificar materia
    @Test
    public void testModificarMateria_Success() throws Exception {
        Map<String, Object> nuevosDatos = new HashMap<>();
        nuevosDatos.put("nombre", "Biologia");

        Materia materia = new Materia("Biologia", 2023, 1, null);
        materia.setId(7);

        Mockito.when(materiaService.modificarMateria(eq(nuevosDatos), eq(7))).thenReturn(materia);

        mockMvc.perform(patch("/materias/7")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevosDatos)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Biologia"));
    }
}
