package co.ue.edu.huertaconectaapp.model;

public class UsuarioHuertaDetalle {
    private int id_usuarios_huertas;
    private Huerta id_huerta;      // TypeORM devuelve el objeto huerta completo
    private String fecha_vinculacion;

    public int getIdUsuariosHuertas() { return id_usuarios_huertas; }
    public void setIdUsuariosHuertas(int id_usuarios_huertas) { this.id_usuarios_huertas = id_usuarios_huertas; }

    public Huerta getHuerta() { return id_huerta; }
    public void setHuerta(Huerta id_huerta) { this.id_huerta = id_huerta; }

    public String getFechaVinculacion() { return fecha_vinculacion; }
    public void setFechaVinculacion(String fecha_vinculacion) { this.fecha_vinculacion = fecha_vinculacion; }
}
