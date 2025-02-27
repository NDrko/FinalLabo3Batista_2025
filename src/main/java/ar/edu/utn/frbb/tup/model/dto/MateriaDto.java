package ar.edu.utn.frbb.tup.model.dto;

public class MateriaDto {
    private String nombre;
    private int anio;
    private int cuatrimestre;
    private long profesorId;
    private Integer carreraId; 

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public int getAnio() {
        return anio;
    }
    public void setAnio(int anio) {
        this.anio = anio;
    }
    
    public int getCuatrimestre() {
        return cuatrimestre;
    }
    public void setCuatrimestre(int cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }
    
    public long getProfesorId() {
        return profesorId;
    }
    public void setProfesorId(long profesorId) {
        this.profesorId = profesorId;
    }
    
    public Integer getCarreraId() {
        return carreraId;
    }
    public void setCarreraId(Integer carreraId) {
        this.carreraId = carreraId;
    }
}
