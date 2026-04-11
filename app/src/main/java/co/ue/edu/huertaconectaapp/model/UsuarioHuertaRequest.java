package co.ue.edu.huertaconectaapp.model;

public class UsuarioHuertaRequest {
    private int id_usuario;
    private int id_huerta;
    private String fecha_vinculacion;

    public UsuarioHuertaRequest(int id_usuario, int id_huerta, String fecha_vinculacion) {
        this.id_usuario = id_usuario;
        this.id_huerta = id_huerta;
        this.fecha_vinculacion = fecha_vinculacion;
    }

    public int getId_usuario() { return id_usuario; }
    public int getId_huerta() { return id_huerta; }
    public String getFecha_vinculacion() { return fecha_vinculacion; }
}
