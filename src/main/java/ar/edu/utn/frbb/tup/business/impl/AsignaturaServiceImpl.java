package ar.edu.utn.frbb.tup.business.impl;
import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.persistence.AsignaturaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AsignaturaServiceImpl implements AsignaturaService {
    @Autowired
    private AsignaturaDao asignaturaDao;
    @Override
    public Asignatura getAsignatura(int materiaId, long dni) {
        return asignaturaDao.buscarAsignatura(materiaId, dni);
    }
    @Override
    public void actualizarAsignatura(Asignatura a) {
        asignaturaDao.updateAsignatura(a);
    }
}
