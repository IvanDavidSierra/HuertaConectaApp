package co.ue.edu.huertaconectaapp.model;

public class UserResponse {
    private int id_usuario;
    private String nombre;
    private String apellido;
    private String correo;
    private String fecha_creacion;
    // id_tipo_usuario llega como objeto anidado desde TypeORM — se ignora en el parseo

    public int getIdUsuario() { return id_usuario; }
    public void setIdUsuario(int id_usuario) { this.id_usuario = id_usuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getFechaCreacion() { return fecha_creacion; }
    public void setFechaCreacion(String fecha_creacion) { this.fecha_creacion = fecha_creacion; }
}
