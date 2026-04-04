package co.ue.edu.huertaconectaapp.model;

public class UserLogin {
    private String correo, contrasena;
    public UserLogin(String correo, String contrasena){
        this.correo = correo;
        this.contrasena = contrasena;
    }
    public String getCorreo() {return correo;}
    public void setCorreo(String correo) {this.correo = correo;}
    public String getContrasena() {return contrasena;}
    public void setContrasena(String contrasena) {this.contrasena = contrasena;}

    @Override
    public String toString() {
        return "UserLogin{" +
                "correo='" + correo + '\'' +
                ", contrasena='" + contrasena + '\'' +
                '}';
    }
}
