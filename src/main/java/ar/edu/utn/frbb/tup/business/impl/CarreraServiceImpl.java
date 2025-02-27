package ar.edu.utn.frbb.tup.business.impl;
import ar.edu.utn.frbb.tup.business.CarreraService;
import ar.edu.utn.frbb.tup.business.MateriaService; 
import ar.edu.utn.frbb.tup.model.Carrera; 
import ar.edu.utn.frbb.tup.model.Materia; 
import ar.edu.utn.frbb.tup.model.dto.CarreraDto; 
import ar.edu.utn.frbb.tup.persistence.CarreraDao; 
import ar.edu.utn.frbb.tup.persistence.exception.CarreraBadRequestException; 
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException; 
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service; 
import java.util.Collections; 
import java.util.List; 
import java.util.Map; 
import java.util.Optional; 
import java.util.stream.Collectors;
@Service
public class CarreraServiceImpl implements CarreraService {
    @Autowired
    private CarreraDao carreraDao;
    @Autowired
    private MateriaService materiaService;
    
    @Override
    public Carrera crearCarrera(CarreraDto carreraDto) 
            throws MateriaNotFoundException, CarreraBadRequestException, CarreraNotFoundException {
    
        Carrera carrera = new Carrera();
        carrera.setNombre(carreraDto.getNombre());
        carrera.setCantidadCuatrimestres(carreraDto.getCantidadCuatrimestres());
        carrera.setIdDepartamento(carreraDto.getDepartamentoId());
    
        List<Materia> materias = Optional.ofNullable(carreraDto.getMateriaIds())
            .orElse(Collections.emptyList())
            .stream()
            .map(id -> {
                try {
                    return materiaService.getMateriaPorId(id);
                } catch (MateriaNotFoundException e) {
                    throw new RuntimeException("La materia que busca no se encontró", e);
                }
            })
            .collect(Collectors.toList());
        return materias.isEmpty() ? carreraDao.crearCarrera(carrera) : carreraDao.crearCarreraConMaterias(materias, carrera);
    }
    
    @Override
    public List<Carrera> getAllCarreras() {
        return carreraDao.getAllCarreras();
    }
    
    @Override
    public Carrera modificarCarrera(Map<String, Object> datosNuevos, int idCarrera) 
            throws CarreraNotFoundException, CarreraBadRequestException, MateriaNotFoundException {
        return carreraDao.updateCarrera(idCarrera, datosNuevos);
    }
    
    @Override
    public Carrera getCarreraPorId(int idCarrera) throws CarreraNotFoundException {
        return carreraDao.getCarreraPorId(idCarrera);
    }
    
    @Override
    public Carrera agregarMateria(Carrera carrera, Materia materia) 
            throws CarreraNotFoundException, CarreraBadRequestException, MateriaNotFoundException {
        Carrera carreraEncontrada = carreraDao.agregarMateria(materia, carrera);
        if (carreraEncontrada == null) {
            throw new CarreraNotFoundException("Carrera no encontrada");
        }
        return carreraEncontrada;
    }
    
    @Override
    public Carrera eliminarCarrera(int idCarrera) throws MateriaNotFoundException, CarreraNotFoundException {
        Carrera carrera = carreraDao.getCarreraPorId(idCarrera);
        if(carrera == null){
            throw new CarreraNotFoundException("No se encontró la carrera con el ID: " + idCarrera);
        }
        eliminarMateriasDeCarrera(carrera);
        return carreraDao.eliminarCarrera(carrera);
    }
    
    private void eliminarMateriasDeCarrera(Carrera carrera){
        carrera.getMaterias().forEach(materia -> {
            try {
                materiaService.borrarMateria(materia.getId());
            } catch (MateriaNotFoundException e) {
                // Manejar o registrar la excepción según convenga
                e.printStackTrace();
            }
        });
    }    
}
