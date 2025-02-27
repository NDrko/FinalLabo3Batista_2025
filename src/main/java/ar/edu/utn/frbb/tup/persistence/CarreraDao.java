package ar.edu.utn.frbb.tup.persistence; 
import ar.edu.utn.frbb.tup.model.Carrera; 
import ar.edu.utn.frbb.tup.model.Materia; 
import ar.edu.utn.frbb.tup.persistence.exception.CarreraBadRequestException; 
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException; 
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException; 
import java.util.List; 
import java.util.Map;
public interface CarreraDao { 
List<Carrera> getAllCarreras(); 
Carrera updateCarrera(int idCarrera, Map<String, Object> nuevosDatos) throws CarreraNotFoundException, CarreraBadRequestException, MateriaNotFoundException;
Carrera agregarMateria(Materia materia, Carrera carrera) throws CarreraNotFoundException, CarreraBadRequestException, MateriaNotFoundException;
Carrera crearCarreraConMaterias(List<Materia> materias, Carrera carrera) throws CarreraBadRequestException, CarreraNotFoundException, MateriaNotFoundException; 
Carrera crearCarrera(Carrera carrera) throws CarreraBadRequestException; 
Carrera getCarreraPorId(int idCarrera) throws CarreraNotFoundException; 
Carrera eliminarCarrera(Carrera carrera) throws CarreraNotFoundException; 
String generarCodigo(); 
}
 