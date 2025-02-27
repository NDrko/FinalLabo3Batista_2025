package ar.edu.utn.frbb.tup.persistence;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaBadRequestException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MateriaDaoMemoryImpl implements MateriaDao {

    private final Map<Integer, Materia> materiasDB = new HashMap<>();
    private static final AtomicInteger idGenerator = new AtomicInteger(1);
    
    @Override
    public Materia guardarMateria(Materia materia) throws MateriaBadRequestException {
        if (materia == null || materia.getNombre() == null || materia.getNombre().trim().isEmpty()) {
            throw new MateriaBadRequestException("El nombre de la materia no puede estar vacío.");
        }
        materia.setId(idGenerator.getAndIncrement());
        materia.setCodigo(generarCodigo(materia.getNombre()));
        materiasDB.put(materia.getId(), materia);
        return materia;
    }
    
    @Override
    public Materia buscarMateriaPorId(int idMateria) throws MateriaNotFoundException {
        return Optional.ofNullable(materiasDB.get(idMateria))
                .orElseThrow(() -> new MateriaNotFoundException("No se encontró la materia con ID " + idMateria));
    }
    
    @Override
    public Materia deleteMateria(Materia materia) throws MateriaNotFoundException {
        if (!materiasDB.containsKey(materia.getId())) {
            throw new MateriaNotFoundException("No se encontró la materia con ID " + materia.getId());
        }
        return materiasDB.remove(materia.getId());
    }
    
    @Override
    public Materia updateMateria(int idMateria, Map<String, Object> nuevosDatos) throws MateriaNotFoundException, MateriaBadRequestException {
        Materia materia = buscarMateriaPorId(idMateria);
        for (Map.Entry<String, Object> entry : nuevosDatos.entrySet()) {
            String campo = entry.getKey();
            Object valor = entry.getValue();
            switch (campo) {
                case "nombre":
                    if (!(valor instanceof String) || ((String) valor).trim().isEmpty()) {
                        throw new MateriaBadRequestException("El nombre no puede estar vacío.");
                    }
                    materia.setNombre((String) valor);
                    break;
                case "anio":
                    if (!(valor instanceof Integer) || (Integer) valor < 1) {
                        throw new MateriaBadRequestException("El año debe ser mayor a 0.");
                    }
                    materia.setAnio((Integer) valor);
                    break;
                case "cuatrimestre":
                    if (!(valor instanceof Integer) || (Integer) valor < 1 || (Integer) valor > 2) {
                        throw new MateriaBadRequestException("El cuatrimestre debe ser 1 o 2.");
                    }
                    materia.setCuatrimestre((Integer) valor);
                    break;
                case "profesor":
                    materia.setProfesor((ar.edu.utn.frbb.tup.model.Profesor) valor);
                    break;
                case "carrera":
                    materia.setCarrera((Carrera) valor);
                    break;
                default:
                    throw new MateriaBadRequestException("Campo '" + campo + "' no válido para actualización.");
            }
        }
        return materia;
    }
    
    @Override
    public Materia buscarMateriaPorNombre(String nombre) throws MateriaNotFoundException {
        return materiasDB.values().stream()
                .filter(m -> m.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElseThrow(() -> new MateriaNotFoundException("No se encontró la materia con nombre: " + nombre));
    }
    
    @Override
    public Materia asignarCarrera(Materia materia, Carrera carrera) throws MateriaBadRequestException {
        if (materia == null || carrera == null) {
            throw new MateriaBadRequestException("La materia y la carrera no pueden ser nulas.");
        }
        materia.setCarrera(carrera);
        return materia;
    }
    
    @Override
    public List<Materia>  getMateriasOrdenadas(String ordenamiento) throws MateriaBadRequestException {
        List<Materia> materiasOrdenadas = new ArrayList<>(materiasDB.values());
        switch (ordenamiento.toLowerCase()) {
            case "nombre":
                materiasOrdenadas.sort(Comparator.comparing(Materia::getNombre));
                break;
            case "anio":
                materiasOrdenadas.sort(Comparator.comparingInt(Materia::getAnio));
                break;
            case "cuatrimestre":
                materiasOrdenadas.sort(Comparator.comparingInt(Materia::getCuatrimestre));
                break;
            default:
                throw new MateriaBadRequestException("El orden no es válido");
        }
        return materiasOrdenadas;
    }
    
    @Override
    public List<Materia> getAllMaterias() throws MateriaNotFoundException {
        if (materiasDB.isEmpty()) {
            throw new MateriaNotFoundException("No hay materias registradas.");
        }
        return new ArrayList<>(materiasDB.values());
    }
    
    @Override
    public String generarCodigo(String nombreMateria) {
        return "MAT-" + nombreMateria.toUpperCase().replace(" ", "_") + "-" + idGenerator.get();
    }
    
}
