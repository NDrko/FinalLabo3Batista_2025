package ar.edu.utn.frbb.tup.persistence;
import ar.edu.utn.frbb.tup.model.Asignatura;
import java.util.HashMap;
import java.util.Map;

public class AsignaturaDaoMemoryImpl implements AsignaturaDao {

    private Map<String, Asignatura> asignaturaMap = new HashMap<>();
    private String generateKey(int materiaId, long dni) {
        return materiaId + "_" + dni;
    }

    @Override
    public Asignatura buscarAsignatura(int materiaId, long dni) {
        return asignaturaMap.get(generateKey(materiaId, dni));
    }

    @Override
    public void updateAsignatura(Asignatura asignatura) {
        for (Map.Entry<String, Asignatura> entry : asignaturaMap.entrySet()) {
            if (entry.getValue().equals(asignatura)) {
                entry.setValue(asignatura);
                break;
            }
        }
    }
    
    public void saveAsignatura(int materiaId, long dni, Asignatura asignatura) {
        asignaturaMap.put(generateKey(materiaId, dni), asignatura);
    }
}
