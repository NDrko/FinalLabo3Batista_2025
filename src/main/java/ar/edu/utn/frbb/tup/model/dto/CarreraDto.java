package ar.edu.utn.frbb.tup.model.dto;
import java.util.List;

public class CarreraDto {
    private String nombre;
    private int departamentoId;
    private int cantidadCuatrimestres;
    private List<Integer> materiaIds; 

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public int getDepartamentoId() {
        return departamentoId;
    }
    public void setDepartamentoId(int departamentoId) {
        this.departamentoId = departamentoId;
    }
    
    public int getCantidadCuatrimestres() {
        return cantidadCuatrimestres;
    }
    public void setCantidadCuatrimestres(int cantidadCuatrimestres) {
        this.cantidadCuatrimestres = cantidadCuatrimestres;
    }
    
    public List<Integer> getMateriaIds() {
        return materiaIds;
    }
    public void setMateriaIds(List<Integer> materiaIds) {
        this.materiaIds = materiaIds;
    }
}
