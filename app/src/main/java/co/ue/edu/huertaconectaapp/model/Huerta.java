package co.ue.edu.huertaconectaapp.model;

import java.io.Serializable;

public class Huerta implements Serializable {
    private int id_huerta;
    private String nombre_huerta;
    private String direccion_huerta;
    private String descripcion;
    private String fecha_creacion;

    public int getIdHuerta() { return id_huerta; }
    public void setIdHuerta(int id_huerta) { this.id_huerta = id_huerta; }

    public String getNombreHuerta() { return nombre_huerta; }
    public void setNombreHuerta(String nombre_huerta) { this.nombre_huerta = nombre_huerta; }

    public String getDireccionHuerta() { return direccion_huerta; }
    public void setDireccionHuerta(String direccion_huerta) { this.direccion_huerta = direccion_huerta; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFechaCreacion() { return fecha_creacion; }
    public void setFechaCreacion(String fecha_creacion) { this.fecha_creacion = fecha_creacion; }
}
