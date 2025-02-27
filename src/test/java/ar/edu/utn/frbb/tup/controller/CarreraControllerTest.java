package ar.edu.utn.frbb.tup.controller;
import ar.edu.utn.frbb.tup.business.CarreraService;
import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraBadRequestException;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarreraController.class)
public class CarreraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarreraService carreraService;

    @MockBean
    private MateriaService materiaService; 

    @Autowired
    private ObjectMapper objectMapper;

    // Crear una carrera
    @Test
    public void testCrearCarrera_Success() throws Exception {
        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setNombre("Ingeniería en Sistemas");
        carreraDto.setDepartamentoId(1);
        carreraDto.setCantidadCuatrimestres(10);
        carreraDto.setMateriaIds(Arrays.asList(1, 2));

        Carrera carrera = new Carrera();
        carrera.setId(1);
        carrera.setNombre("Ingeniería en Sistemas");
        carrera.setCantidadCuatrimestres(10);
        carrera.setIdDepartamento(1);

        Mockito.when(carreraService.crearCarrera(any(CarreraDto.class))).thenReturn(carrera);

        mockMvc.perform(post("/carreras/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carreraDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Ingeniería en Sistemas"));
    }

    // Caso de error: Crear carrera con datos inválidos
    @Test
    public void testCrearCarrera_Failure_InvalidData() throws Exception {
        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setNombre("");
        carreraDto.setDepartamentoId(0);
        carreraDto.setCantidadCuatrimestres(0);
        carreraDto.setMateriaIds(Collections.emptyList());

        Mockito.when(carreraService.crearCarrera(any(CarreraDto.class)))
                .thenThrow(new CarreraBadRequestException("Datos inválidos para la carrera"));

        mockMvc.perform(post("/carreras/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carreraDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Datos inválidos para la carrera"));
    }

    //  Obtener todas las carreras
    @Test
    public void testGetAllCarreras_Success() throws Exception {
        Carrera carrera1 = new Carrera();
        carrera1.setId(1);
        carrera1.setNombre("Ingeniería en Sistemas");
        Carrera carrera2 = new Carrera();
        carrera2.setId(2);
        carrera2.setNombre("Ingeniería Industrial");
        Mockito.when(carreraService.getAllCarreras()).thenReturn(Arrays.asList(carrera1, carrera2));

        mockMvc.perform(get("/carreras"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    //Obtener carrera por ID
    @Test
    public void testGetCarreraById_Success() throws Exception {
        Carrera carrera = new Carrera();
        carrera.setId(1);
        carrera.setNombre("Ingeniería en Sistemas");
        Mockito.when(carreraService.getCarreraPorId(1)).thenReturn(carrera);

        mockMvc.perform(get("/carreras/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Ingeniería en Sistemas"));
    }

    // Caso de error: Obtener carrera por ID inexistente
    @Test
    public void testGetCarreraById_NotFound() throws Exception {
        Mockito.when(carreraService.getCarreraPorId(99))
                .thenThrow(new CarreraNotFoundException("No se encontró la carrera con ID 99"));

        mockMvc.perform(get("/carreras/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("No se encontró la carrera con ID 99"));
    }

    //  Modificar carrera
    @Test
    public void testModificarCarrera_Success() throws Exception {
        Map<String, Object> nuevosDatos = new HashMap<>();
        nuevosDatos.put("nombre", "Ingeniería en Computación");

        Carrera carrera = new Carrera();
        carrera.setId(1);
        carrera.setNombre("Ingeniería en Computación");

        Mockito.when(carreraService.modificarCarrera(eq(nuevosDatos), eq(1))).thenReturn(carrera);

        mockMvc.perform(patch("/carreras/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevosDatos)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Ingeniería en Computación"));
    }

    // Eliminar carrera
    @Test
    public void testEliminarCarrera_Success() throws Exception {
        Carrera carrera = new Carrera();
        carrera.setId(1);
        carrera.setNombre("Ingeniería en Sistemas");
        Mockito.when(carreraService.eliminarCarrera(1)).thenReturn(carrera);

        mockMvc.perform(delete("/carreras/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    //  Agregar materia a una carrera
    @Test
    public void testAgregarMateria_Success() throws Exception {
        Carrera carrera = new Carrera();
        carrera.setId(1);
        carrera.setNombre("Ingeniería en Sistemas");

        Materia materia = new Materia("Matemáticas", 2023, 1, null);
        materia.setId(2);

        Carrera carreraActualizada = new Carrera();
        carreraActualizada.setId(1);
        carreraActualizada.setNombre("Ingeniería en Sistemas");

        Mockito.when(carreraService.getCarreraPorId(1)).thenReturn(carrera);
        Mockito.when(materiaService.getMateriaPorId(2)).thenReturn(materia);
        Mockito.when(carreraService.agregarMateria(carrera, materia)).thenReturn(carreraActualizada);

        mockMvc.perform(put("/carreras/1/materias/2"))
                .andExpect(status().isOk());
    }
}
