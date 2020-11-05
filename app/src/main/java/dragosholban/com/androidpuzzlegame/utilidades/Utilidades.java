package uoc.appdroid8.utilidades;

public class Utilidades {

    public static final String CAMPO_ID="id";

    //Usuarios
    public static final String TABLA_USUARIO="usuario";
    public static final String CAMPO_NOMBRE="nombre";
    public static final String CREAR_TABLA_USUARIO="CREATE TABLE "+TABLA_USUARIO+" ("+CAMPO_ID+" INTEGER, "+CAMPO_NOMBRE+" TEXT)";

    //Puntuaciones
    public static final String TABLA_PUNTUACIONES="puntuaciones";
    public static final String CAMPO_PUNTUACION="puntuacion";
    public static final String CREAR_TABLA_PUNTUACIONES="CREATE TABLE "+TABLA_PUNTUACIONES+" ("+CAMPO_ID+" INTEGER, "+CAMPO_PUNTUACION+" INTEGER)";

}
