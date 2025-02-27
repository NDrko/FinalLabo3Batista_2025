package ar.edu.utn.frbb.tup.persistence; 
import ar.edu.utn.frbb.tup.model.Carrera; 
import ar.edu.utn.frbb.tup.model.Materia; 
import ar.edu.utn.frbb.tup.persistence.exception.MateriaBadRequestException; 
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException; 
import java.util.List; 
import java.util.Map;
public interface MateriaDao {
Materia guardarMateria(Materia materia) throws MateriaBadRequestException; 
Materia buscarMateriaPorId(int idMateria) throws MateriaNotFoundException; 
Materia deleteMateria(Materia materia) throws MateriaNotFoundException; 
Materia updateMateria(int idMateria, Map<String, Object> nuevosDatos) throws MateriaNotFoundException, MateriaBadRequestException; 
Materia buscarMateriaPorNombre(String nombre) throws MateriaNotFoundException;
Materia asignarCarrera(Materia materia, Carrera carrera) throws MateriaBadRequestException;
List<Materia> getMateriasOrdenadas(String ordenamiento) throws MateriaBadRequestException; 
List<Materia> getAllMaterias() throws MateriaNotFoundException;
String generarCodigo(String nombreMateria); 
}