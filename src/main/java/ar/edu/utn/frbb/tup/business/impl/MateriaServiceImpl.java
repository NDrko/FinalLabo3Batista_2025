package ar.edu.utn.frbb.tup.business.impl;
import ar.edu.utn.frbb.tup.business.MateriaService; 
import ar.edu.utn.frbb.tup.model.Carrera; 
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto; 
import ar.edu.utn.frbb.tup.persistence.MateriaDao; 
import ar.edu.utn.frbb.tup.persistence.exception.MateriaBadRequestException; 
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service; 
import java.util.List; 
import java.util.Map;

@Service
public class MateriaServiceImpl implements MateriaService {
    @Autowired
    private MateriaDao materiaDao;
    @Override
    public Materia crearMateria(MateriaDto materiaDto) throws IllegalArgumentException, MateriaBadRequestException {
        Materia materia = new Materia(materiaDto.getNombre(), materiaDto.getAnio(), materiaDto.getCuatrimestre(), null);
        return materiaDao.guardarMateria(materia);
    }
    
    @Override
    public List<Materia> getAllMaterias() throws MateriaNotFoundException {
        return materiaDao.getAllMaterias();
    }
    
    @Override
    public Materia getMateriaPorId(int idMateria) throws MateriaNotFoundException {
        return materiaDao.buscarMateriaPorId(idMateria);
    }
    
    @Override
    public Materia borrarMateria(Integer idMateria) throws MateriaNotFoundException {
        Materia materia = materiaDao.buscarMateriaPorId(idMateria);
        if (materia == null) {
            throw new MateriaNotFoundException("Materia no encontrada");
        }
        return materiaDao.deleteMateria(materia);
    }
    
    @Override
    public Materia modificarMateria(Map<String, Object> nuevosDatos, int idMateria) 
            throws MateriaNotFoundException, MateriaBadRequestException {
        return materiaDao.updateMateria(idMateria, nuevosDatos);
    }
    
    @Override
    public List<Materia> ordenarMaterias(String ordenamiento) throws MateriaBadRequestException {
        return materiaDao.getMateriasOrdenadas(ordenamiento);
    }
    
    @Override
    public Materia filtrarPorNombre(String nombre) throws MateriaNotFoundException {
        return materiaDao.buscarMateriaPorNombre(nombre);
    }
    
    @Override
    public Materia asignarCarrera(Carrera carrera, Materia materia) throws MateriaBadRequestException {
        return materiaDao.asignarCarrera(materia, carrera);
    }
}
