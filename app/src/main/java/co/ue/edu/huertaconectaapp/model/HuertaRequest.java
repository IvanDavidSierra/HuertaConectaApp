package co.ue.edu.huertaconectaapp.model;

public class HuertaRequest {
    private String nombre_huerta;
    private String descripcion;
    private String direccion_huerta;
    private String fecha_creacion;

    public HuertaRequest(String nombre_huerta, String descripcion,
                         String direccion_huerta, String fecha_creacion) {
        this.nombre_huerta = nombre_huerta;
        this.descripcion = descripcion;
        this.direccion_huerta = direccion_huerta;
        this.fecha_creacion = fecha_creacion;
    }

    public String getNombre_huerta() { return nombre_huerta; }
    public String getDescripcion() { return descripcion; }
    public String getDireccion_huerta() { return direccion_huerta; }
    public String getFecha_creacion() { return fecha_creacion; }
}
