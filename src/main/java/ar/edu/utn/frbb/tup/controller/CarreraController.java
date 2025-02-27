package ar.edu.utn.frbb.tup.controller;
import ar.edu.utn.frbb.tup.business.CarreraService; 
import ar.edu.utn.frbb.tup.business.MateriaService; 
import ar.edu.utn.frbb.tup.model.Carrera; 
import ar.edu.utn.frbb.tup.model.Materia; 
import ar.edu.utn.frbb.tup.model.dto.CarreraDto; 
import ar.edu.utn.frbb.tup.persistence.exception.CarreraBadRequestException; 
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException; 
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.*;
import java.util.List; import java.util.Map;
@RestController
@RequestMapping("carreras")
public class CarreraController {
    @Autowired
private CarreraService carreraService;
@Autowired
private MateriaService materiaService;

@PostMapping("/")
public ResponseEntity<Carrera> crearCarrera(@RequestBody CarreraDto carreraDto) 
        throws MateriaNotFoundException, CarreraBadRequestException, CarreraNotFoundException {
    Carrera carrera = carreraService.crearCarrera(carreraDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(carrera);
}

@GetMapping
public ResponseEntity<List<Carrera>> getAllCarreras() throws CarreraNotFoundException {
    List<Carrera> carreras = carreraService.getAllCarreras();
    return ResponseEntity.ok(carreras);
}

@GetMapping("/{idCarrera}")
public ResponseEntity<Carrera> getCarreraId(@PathVariable int idCarrera) throws CarreraNotFoundException {
    Carrera carrera = carreraService.getCarreraPorId(idCarrera);
    return ResponseEntity.ok(carrera);
}

@PatchMapping("/{idCarrera}")
public ResponseEntity<Carrera> modificarCarrera(@PathVariable int idCarrera, 
                                                @RequestBody Map<String, Object> datosNuevos) 
        throws MateriaNotFoundException, CarreraBadRequestException, CarreraNotFoundException {
    Carrera carrera = carreraService.modificarCarrera(datosNuevos, idCarrera);
    return ResponseEntity.ok(carrera);
}

@DeleteMapping("/{idCarrera}")
public ResponseEntity<Carrera> eliminarCarrera(@PathVariable int idCarrera) 
        throws CarreraNotFoundException, MateriaNotFoundException {
    Carrera carrera = carreraService.eliminarCarrera(idCarrera);
    return ResponseEntity.ok(carrera);
}

@PutMapping("/{idCarrera}/materias/{idMateria}")
public ResponseEntity<Carrera> agregarMateria(@PathVariable int idCarrera, @PathVariable int idMateria) 
        throws CarreraNotFoundException, MateriaNotFoundException, CarreraBadRequestException {
    Carrera carrera = carreraService.getCarreraPorId(idCarrera);
    Materia materia = materiaService.getMateriaPorId(idMateria);
    Carrera carreraActualizada = carreraService.agregarMateria(carrera, materia);
    return ResponseEntity.ok(carreraActualizada);
}

}