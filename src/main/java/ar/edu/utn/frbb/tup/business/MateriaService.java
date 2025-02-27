package ar.edu.utn.frbb.tup.business;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaBadRequestException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import java.util.List;
import java.util.Map;

public interface MateriaService {
    Materia crearMateria(MateriaDto inputData) throws MateriaBadRequestException, IllegalArgumentException;
    List<Materia> getAllMaterias() throws MateriaNotFoundException;
    Materia getMateriaPorId(int idMateria)throws MateriaNotFoundException;
    Materia borrarMateria(Integer idMateria) throws MateriaNotFoundException;
    Materia modificarMateria(Map<String, Object> nuevosDatos, int idMateria)throws MateriaNotFoundException, MateriaBadRequestException;
    List<Materia> ordenarMaterias(String ordenamiento)throws MateriaBadRequestException;
    Materia filtrarPorNombre(String nombre) throws MateriaNotFoundException;
    Materia asignarCarrera(Carrera carrera, Materia materia)throws MateriaBadRequestException;
}