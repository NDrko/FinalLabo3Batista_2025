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
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaBadRequestException; 
import ar.edu.utn.frbb.tup.persistence.exception.MateriaBadRequestException; 
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController
@RequestMapping("alumnos")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;
    
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Alumno crearAlumno(@RequestBody AlumnoDto alumnoDto) throws ALumnoNotFoundException {
        return alumnoService.crearAlumno(alumnoDto);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Alumno> borrarAlumno(@PathVariable int id) throws ALumnoNotFoundException {
        Alumno alumno = alumnoService.borrarAlumno(id);
        return ResponseEntity.ok(alumno);  
    }
    
    @PatchMapping("/editar")
    public ResponseEntity<Alumno> editarAlumno(@RequestParam int id, @RequestBody Map<String, Object> nuevosDatos) throws ALumnoNotFoundException {
        Alumno alumno = alumnoService.editarAlumno(id, nuevosDatos);
        return ResponseEntity.ok(alumno);
    }
    
    @GetMapping 
    public ResponseEntity<Alumno> buscarAlumno(@RequestParam String apellido) throws ALumnoNotFoundException, AlumnoBadRequestException {
        Alumno alumno = alumnoService.buscarAlumno(apellido);
        return ResponseEntity.ok(alumno);
    }
    
    @PutMapping("/{idAlumno}/asignatura/{idAsignatura}")
    public ResponseEntity<Asignatura> pasarNota(@PathVariable int idAlumno, 
                                                @PathVariable int idAsignatura,
                                                @RequestParam(required = false, defaultValue = "6") int nota,
                                                @RequestParam String estadoAsignatura)
            throws CorrelatividadesNoAprobadasException, EstadoIncorrectoException, ALumnoNotFoundException, 
                   CorrelatividadException, MateriaBadRequestException, AsignaturaNotFoundException, 
                   AlumnoBadRequestException, AsignaturaBadRequestException {
    
        switch (estadoAsignatura.toUpperCase()){
            case "A":
                return ResponseEntity.ok(alumnoService.aprobarAsignatura(idAsignatura, nota, idAlumno));
            case "C":
                return ResponseEntity.ok(alumnoService.cursarAsignatura(idAlumno, idAsignatura));
            default:
                return ResponseEntity.ok(alumnoService.recursarAsignatura(idAlumno, idAsignatura));
        }
    }
    
}
