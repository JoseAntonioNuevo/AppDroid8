package uoc.appdroid8.entidades;

public class Puntuacion {

    private Integer id;
    private String puntuacion;

    public Puntuacion(Integer id, String puntuacion) {
        this.id = id;
        this.puntuacion = puntuacion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(String puntuacion) {
        this.puntuacion = puntuacion;
    }
}
