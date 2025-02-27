package ar.edu.utn.frbb.tup.controller;
import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadException;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadesNoAprobadasException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.exception.ALumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoBadRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import java.util.HashMap;
import java.util.Map;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlumnoController.class)
public class AlumnoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlumnoService alumnoService;

    @Autowired
    private ObjectMapper objectMapper;

    //  creación de alumno
    @Test
    public void testCrearAlumno_Success() throws Exception {
        AlumnoDto dto = new AlumnoDto("Juan", "Perez", 12345678L);
        Alumno alumno = new Alumno();
        alumno.setId(1);
        alumno.setNombre("Juan");
        alumno.setApellido("Perez");
        alumno.setDni(12345678L);

        Mockito.when(alumnoService.crearAlumno(Mockito.any(AlumnoDto.class))).thenReturn(alumno);

        mockMvc.perform(post("/alumnos/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    // Caso de error: creación forzando excepción (por ejemplo, datos inválidos)
    @Test
    public void testCrearAlumno_Failure_InvalidData() throws Exception {
        AlumnoDto dto = new AlumnoDto("", "", 0L);

        Mockito.when(alumnoService.crearAlumno(Mockito.any(AlumnoDto.class)))
                .thenThrow(new IllegalArgumentException("Datos inválidos para crear alumno"));

        mockMvc.perform(post("/alumnos/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorMessage").value("Datos inválidos para crear alumno"));
    }

    //  borrado de alumno
    @Test
    public void testBorrarAlumno_Success() throws Exception {
        Alumno alumno = new Alumno();
        alumno.setId(1);
        alumno.setNombre("Juan");
        alumno.setApellido("Perez");
        alumno.setDni(12345678L);

        Mockito.when(alumnoService.borrarAlumno(1)).thenReturn(alumno);

        mockMvc.perform(delete("/alumnos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    // editar alumno
    @Test
    public void testEditarAlumno_Success() throws Exception {
        Map<String, Object> nuevosDatos = new HashMap<>();
        nuevosDatos.put("nombre", "Carlos");

        Alumno alumno = new Alumno();
        alumno.setId(1);
        alumno.setNombre("Carlos");
        alumno.setApellido("Perez");
        alumno.setDni(12345678L);

        Mockito.when(alumnoService.editarAlumno(Mockito.eq(1), Mockito.anyMap())).thenReturn(alumno);

        mockMvc.perform(patch("/alumnos/editar")
                .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevosDatos)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Carlos"));
    }

    // buscar alumno por apellido
    @Test
    public void testBuscarAlumno_Success() throws Exception {
        Alumno alumno = new Alumno();
        alumno.setId(1);
        alumno.setNombre("Juan");
        alumno.setApellido("Perez");
        alumno.setDni(12345678L);

        Mockito.when(alumnoService.buscarAlumno("Perez")).thenReturn(alumno);

        mockMvc.perform(get("/alumnos")
                .param("apellido", "Perez"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.apellido").value("Perez"));
    }

    // Caso de error: buscar alumno con apellido vacío (debe dar Bad Request)
    @Test
    public void testBuscarAlumno_Failure_BlankApellido() throws Exception {
        Mockito.when(alumnoService.buscarAlumno("")).thenThrow(
            new ResponseStatusException(HttpStatus.BAD_REQUEST, "Apellido no puede ser nulo o vacío")
        );
        mockMvc.perform(get("/alumnos")
                .param("apellido", ""))
                .andExpect(status().isBadRequest());
    }
    

    // aprobar asignatura (estado "A")
    @Test
    public void testPasarNota_Aprobar_Success() throws Exception, EstadoIncorrectoException, CorrelatividadesNoAprobadasException, CorrelatividadException {
        // Simulamos el caso de aprobar asignatura con nota válida
        Asignatura asignatura = new Asignatura();
        Mockito.when(alumnoService.aprobarAsignatura(Mockito.eq(1), Mockito.eq(7), Mockito.eq(1))).thenReturn(asignatura);

        mockMvc.perform(put("/alumnos/1/asignatura/1")
                .param("nota", "7")
                .param("estadoAsignatura", "A"))
                .andExpect(status().isOk());
    }

    // cursar asignatura (estado "C")
    @Test
    public void testPasarNota_Cursar_Success() throws Exception {
        Asignatura asignatura = new Asignatura();
        Mockito.when(alumnoService.cursarAsignatura(Mockito.eq(1), Mockito.eq(1))).thenReturn(asignatura);

        mockMvc.perform(put("/alumnos/1/asignatura/1")
                .param("estadoAsignatura", "C"))
                .andExpect(status().isOk());
    }

    //  recursar asignatura (estado distinto a "A" y "C")
    @Test
    public void testPasarNota_Recursar_Success() throws Exception {
        Asignatura asignatura = new Asignatura();
        Mockito.when(alumnoService.recursarAsignatura(Mockito.eq(1), Mockito.eq(1))).thenReturn(asignatura);

        mockMvc.perform(put("/alumnos/1/asignatura/1")
                .param("estadoAsignatura", "R"))
                .andExpect(status().isOk());
    }
}

