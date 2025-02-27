package ar.edu.utn.frbb.tup.persistence;
import ar.edu.utn.frbb.tup.model.Profesor;
import java.util.HashMap;
import java.util.Map;

public class ProfesorDaoMemoryImpl implements ProfesorDao {

    private Map<Long, Profesor> profesorMap = new HashMap<>();

    public ProfesorDaoMemoryImpl() {
        // Se pueden agregar profesores de ejemplo.
        Profesor p1 = new Profesor("Juan", "Pérez", "Licenciado en Informática");
        p1.setId(1);
        profesorMap.put(p1.getId(), p1);

        Profesor p2 = new Profesor("María", "González", "Ingeniera en Sistemas");
        p2.setId(2);
        profesorMap.put(p2.getId(), p2);
    }

    @Override
    public Profesor buscarProfesorPorId(long id) {
        return profesorMap.get(id);
    }
}
