package co.ue.edu.huertaconectaapp.model;

public class Publicacion {
    private int idPublicacion;
    private String titulo;
    private String contenido;
    private String fechaPost;
    private int idUsuariosHuertas;
    private byte[] imagenBlob;
    private String autorCorreo;

    public Publicacion() {}

    public Publicacion(int idPublicacion, String titulo, String contenido, String fechaPost,
                       int idUsuariosHuertas, byte[] imagenBlob, String autorCorreo) {
        this.idPublicacion = idPublicacion;
        this.titulo = titulo;
        this.contenido = contenido;
        this.fechaPost = fechaPost;
        this.idUsuariosHuertas = idUsuariosHuertas;
        this.imagenBlob = imagenBlob;
        this.autorCorreo = autorCorreo;
    }

    public int getIdPublicacion() { return idPublicacion; }
    public void setIdPublicacion(int idPublicacion) { this.idPublicacion = idPublicacion; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public String getFechaPost() { return fechaPost; }
    public void setFechaPost(String fechaPost) { this.fechaPost = fechaPost; }

    public int getIdUsuariosHuertas() { return idUsuariosHuertas; }
    public void setIdUsuariosHuertas(int idUsuariosHuertas) { this.idUsuariosHuertas = idUsuariosHuertas; }

    public byte[] getImagenBlob() { return imagenBlob; }
    public void setImagenBlob(byte[] imagenBlob) { this.imagenBlob = imagenBlob; }

    public String getAutorCorreo() { return autorCorreo; }
    public void setAutorCorreo(String autorCorreo) { this.autorCorreo = autorCorreo; }
}
