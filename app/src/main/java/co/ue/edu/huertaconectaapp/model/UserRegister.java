package co.ue.edu.huertaconectaapp.model;

public class UserRegister {
    private String nombre,apellido,correo,contrasena;
    private int id_tipo_usuario;

    public UserRegister(String nombre, String apellido, String correo,String contrasena, int id_tipo_usuario){
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasena = contrasena;
        this.id_tipo_usuario = id_tipo_usuario;
    }
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getApellido() {return apellido;}
    public void setApellido(String apellido) {this.apellido = apellido;}
    public String getCorreo() {return correo;}
    public void setCorreo(String correo) {this.correo = correo;}
    public String getContrasena() {return contrasena;}
    public void setContraseña(String contrasena) {this.contrasena = contrasena;}
    public int getId_tipo_usuario() {return id_tipo_usuario;}
    public void setId_tipo_usuario(int id_tipo_usuario) {this.id_tipo_usuario = id_tipo_usuario;}
    @Override
    public String toString() {
        return "UserRegister{" +
                "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", correo='" + correo + '\'' +
                ", contraseña='" + contrasena + '\'' +
                ", id_tipo_usuario=" + id_tipo_usuario +
                '}';
    }
}
