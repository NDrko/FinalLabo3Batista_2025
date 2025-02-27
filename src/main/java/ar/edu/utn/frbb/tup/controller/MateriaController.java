package ar.edu.utn.frbb.tup.controller;
import ar.edu.utn.frbb.tup.business.MateriaService; 
import ar.edu.utn.frbb.tup.model.Materia; 
import ar.edu.utn.frbb.tup.model.dto.MateriaDto; 
import ar.edu.utn.frbb.tup.persistence.exception.MateriaBadRequestException; 
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException; 
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.*;
import java.util.List; 
import java.util.Map;

@RestController
@RequestMapping("materias")
public class MateriaController {

    private final MateriaService materiaService;

    public MateriaController(MateriaService materiaService) {
        this.materiaService = materiaService;
    }
    
    @GetMapping
    public ResponseEntity<List<Materia>> getMaterias() throws MateriaNotFoundException {
        List<Materia> materias = materiaService.getAllMaterias();
        return ResponseEntity.ok(materias);
    }
    
    @GetMapping("/ordenadas")
    public ResponseEntity<List<Materia>> getMateriasOrdenadas(@RequestParam String ordenamiento) throws MateriaBadRequestException {
        List<Materia> materiasOrdenadas = materiaService.ordenarMaterias(ordenamiento);
        return ResponseEntity.ok(materiasOrdenadas);
    }
    
    @GetMapping("/filtro")
    public ResponseEntity<Materia> filtrarPorNombre(@RequestParam String nombre) throws MateriaNotFoundException {
        Materia materia = materiaService.filtrarPorNombre(nombre);
        return ResponseEntity.ok(materia);
    }
    
    @GetMapping("/{idMateria}")
    public ResponseEntity<Materia> getMateriaById(@PathVariable Integer idMateria) throws MateriaNotFoundException {
        Materia materia = materiaService.getMateriaPorId(idMateria);
        return ResponseEntity.ok(materia);
    }
    
    @PostMapping
    public ResponseEntity<Materia> crearMateria(@RequestBody MateriaDto materiaDto) throws MateriaBadRequestException, IllegalArgumentException {
        Materia materia = materiaService.crearMateria(materiaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(materia);
    }
    
    @DeleteMapping("/{idMateria}")
    public ResponseEntity<Materia> borrarMateria(@PathVariable Integer idMateria) throws MateriaNotFoundException {
        Materia materia = materiaService.borrarMateria(idMateria);
        return ResponseEntity.ok(materia);
    }
    
    @PatchMapping("/{idMateria}")
    public ResponseEntity<Materia> modificarMateria(@PathVariable int idMateria,
                                                     @RequestBody Map<String, Object> nuevosDatos) throws MateriaBadRequestException, MateriaNotFoundException {
        Materia materia = materiaService.modificarMateria(nuevosDatos, idMateria);
        return ResponseEntity.ok(materia);
    }    
}
