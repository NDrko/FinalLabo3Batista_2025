package ar.edu.utn.frbb.tup.persistence; 
import ar.edu.utn.frbb.tup.business.MateriaService; 
import ar.edu.utn.frbb.tup.model.Carrera; 
import ar.edu.utn.frbb.tup.model.Materia; 
import ar.edu.utn.frbb.tup.persistence.exception.CarreraBadRequestException; 
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException; 
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException; 
import java.util.*; 
import java.util.concurrent.atomic.AtomicInteger;

public class CarreraDaoMemoryImpl implements CarreraDao {
private final MateriaDao materiaDao = new MateriaDaoMemoryImpl();
private final Map<Integer, Carrera> carreraMap = new HashMap<>();
private static final Set<String> codigosUsados = new HashSet<>();
private static final AtomicInteger idGenerator = new AtomicInteger(1);

private MateriaService materiaService;

public CarreraDaoMemoryImpl(MateriaService materiaService) {
    this.materiaService = materiaService;
}

@Override
public List<Carrera> getAllCarreras() {
    return new ArrayList<>(carreraMap.values());
}

@Override
public Carrera updateCarrera(int idCarrera, Map<String, Object> nuevosDatos) throws CarreraNotFoundException, CarreraBadRequestException, MateriaNotFoundException {
    Carrera carrera = getCarreraPorId(idCarrera);
    for (Map.Entry<String, Object> entry : nuevosDatos.entrySet()) {
        String campo = entry.getKey();
        Object valor = entry.getValue();
        switch (campo) {
            case "nombre":
                carrera.setNombre((String) valor);
                break;
            case "departamentoId":
                carrera.setIdDepartamento((Integer) valor);
                break;
            case "cantidadCuatrimestres":
                carrera.setCantidadCuatrimestres((Integer) valor);
                break;
            case "materiaIds":
                if (valor instanceof List<?>) {
                    List<Integer> materiaIds = (List<Integer>) valor;
                    for (Integer idMateria : materiaIds) {
                        Materia materia = materiaService.getMateriaPorId(idMateria);
                        agregarMateria(materia, carrera);
                    }
                } else {
                    throw new CarreraBadRequestException("La carrera no .");
                }
                break;
            default:
                throw new CarreraBadRequestException("El campo '" + campo + "' no es válido para modificar.");
        }
    }
    return carrera;
}

@Override
public Carrera getCarreraPorId(int idCarrera) throws CarreraNotFoundException {
    Carrera carrera = carreraMap.get(idCarrera);
    if (carrera == null) {
        throw new CarreraNotFoundException("No se encontró la carrera deseada");
    }
    return carrera;
}

@Override
public Carrera agregarMateria(Materia materia, Carrera carrera) throws CarreraBadRequestException, CarreraNotFoundException, MateriaNotFoundException {
    Carrera carreraEncontrada = getCarreraPorId(carrera.getId());
    carreraEncontrada.agregarMateria(materiaDao.buscarMateriaPorId(materia.getId()));
    return carreraEncontrada;
}

@Override
public Carrera crearCarreraConMaterias(List<Materia> materias, Carrera carrera) throws CarreraBadRequestException, CarreraNotFoundException, MateriaNotFoundException {
    carrera.setId(idGenerator.getAndIncrement());
    carrera.setCodigo(generarCodigo());
    carreraMap.put(carrera.getId(), carrera);
    for (Materia materia : materias) {
        agregarMateria(materia, carrera);
    }
    return carrera;
}

@Override
public Carrera crearCarrera(Carrera carrera) throws CarreraBadRequestException {
    if (carreraMap.containsValue(carrera)) {
        throw new CarreraBadRequestException("Ya existe dicha carrera.");
    }
    carrera.setCodigo(generarCodigo());
    carrera.setId(idGenerator.getAndIncrement());
    carreraMap.put(carrera.getId(), carrera);
    return carrera;
}

@Override
public Carrera eliminarCarrera(Carrera carrera) throws CarreraNotFoundException {
    if (!carreraMap.containsKey(carrera.getId())) {
        throw new CarreraNotFoundException("No se encontró la carrera que busca eliminar.");
    }
    return carreraMap.remove(carrera.getId());
}

@Override
public String generarCodigo() {
    String caracteres = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ0123456789";
    Random r = new Random();
    String codigo;
    do {
        StringBuilder codigoBuilder = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            codigoBuilder.append(caracteres.charAt(r.nextInt(caracteres.length())));
        }
        codigo = codigoBuilder.toString();
    } while (!codigosUsados.add(codigo));
    return codigo;
}
}