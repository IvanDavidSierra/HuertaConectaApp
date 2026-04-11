package co.ue.edu.huertaconectaapp.model;

public class DashboardResponse {
    private int usuarios;
    private int tiposUsuario;
    private int huertas;
    private int publicaciones;
    private int cultivos;

    public int getUsuarios() { return usuarios; }
    public void setUsuarios(int usuarios) { this.usuarios = usuarios; }

    public int getTiposUsuario() { return tiposUsuario; }
    public void setTiposUsuario(int tiposUsuario) { this.tiposUsuario = tiposUsuario; }

    public int getHuertas() { return huertas; }
    public void setHuertas(int huertas) { this.huertas = huertas; }

    public int getPublicaciones() { return publicaciones; }
    public void setPublicaciones(int publicaciones) { this.publicaciones = publicaciones; }

    public int getCultivos() { return cultivos; }
    public void setCultivos(int cultivos) { this.cultivos = cultivos; }
}
